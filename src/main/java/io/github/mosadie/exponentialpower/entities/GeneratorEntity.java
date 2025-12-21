package io.github.mosadie.exponentialpower.entities;

import io.github.mosadie.exponentialpower.EnergyLevelConfig;
import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.container.GeneratorContainerMenu;
import io.github.mosadie.exponentialpower.energy.GeneratorEnergyConnection;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class GeneratorEntity extends BaseContainerBlockEntity {
    private final EnergyLevelConfig config;
    private double currentOutput = 0;
    private double energy = 0;
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    private final GeneratorEnergyConnection energyStorage = new GeneratorEnergyConnection(this);
    private final List<BlockPos> posList = new ArrayList<>();
    private int tickCount = 100;

    public GeneratorEntity(BlockPos pos, BlockState state, EnergyLevelConfig config) {
        super(config.getGeneratorBlockEntityType(), pos, state);
        this.config = config;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(nbt, registries);
        ListTag nbtTagList = new ListTag();
        int slotsSize = getContainerSize();
        for (int i = 0; i < slotsSize; i++) {
            ItemStack stack = getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            CompoundTag itemTag = new CompoundTag();
            itemTag.putInt("Slot", i);
            stack.save(registries, itemTag);
            nbtTagList.add(itemTag);
        }
        nbt.put("Items", nbtTagList);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(nbt, registries);
        ListTag tagList;
        if (nbt.contains("Items", Tag.TAG_COMPOUND)) { // Load older NBT item structure.
            ExponentialPower.LOGGER.warn("Upgrading old NBT item tag on save!");
            tagList = nbt.getCompound("Items").getList("Items", Tag.TAG_COMPOUND);
        } else if (nbt.contains("Items", Tag.TAG_LIST)) {
            tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        } else {
            return;
        }
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");
            if (slot >= 0 && slot < getContainerSize()) {
                setItem(slot, ItemStack.parse(registries, itemTags).orElse(ItemStack.EMPTY));
            }
        }
    }

    public static <T extends BlockEntity> void tick(T tile) {
        if (tile instanceof GeneratorEntity generator) {
            if (generator.getItem(0).getItem() == Registration.ENDER_CELL.get()) {
                generator.currentOutput = generator.calculateEnergy(generator.getItem(0).getCount());
                if (generator.currentOutput <= 0) {
                    generator.currentOutput = 1;
                }
            } else {
                generator.currentOutput = 0;
            }
            generator.energy = generator.currentOutput;
            if (generator.energy > 0) {
                generator.handleSendingEnergy();
            }
        }
    }

    private void handleSendingEnergy() {
        Level level = this.level;
        if (level == null) {
            return;
        }
        if (level.isClientSide) {
            return;
        }
        if (sendingToNoEnergy(getBlockPos(), level)) {//nearest first
            return;
        }
        tickCount++;
        if (tickCount >= 100) {//5s
            tickCount = 0;
            refreshStoragePosList();
        }
        for (BlockPos pos : posList) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof StorageEntity storage) {
                if (sendingToNoEnergy(pos, level)) {
                    return;
                }
                energy -= storage.acceptEnergy(energy);
                if (energy <= 0) {
                    return;
                }
            }
        }
    }

    private void refreshStoragePosList() {
        posList.clear();
        int distance = config.getGeneratorMaxDistance();
        for (int x = -distance; x <= distance; x++) {
            for (int y = -distance; y <= distance; y++) {
                for (int z = -distance; z <= distance; z++) {
                    refreshStoragePosList(x, y, z);
                }
            }
        }
    }

    private void refreshStoragePosList(int x, int y, int z) {
        if (x == 0 && y == 0 && z == 0) {
            return;
        }
        BlockPos targetBlock = getBlockPos().offset(x, y, z);
        assert level != null;
        BlockEntity entity = level.getBlockEntity(targetBlock);
        if (entity == null) {
            return;
        }
        if (entity instanceof StorageEntity) {
            posList.add(targetBlock);
        }
    }

    private boolean sendingToNoEnergy(BlockPos blockPos, Level level) {
        for (Direction direction : Direction.values()) {
            BlockPos pos = blockPos.relative(direction);
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity == null) {
                continue;
            }
            if (entity instanceof StorageEntity storage) {
                energy -= storage.acceptEnergy(energy);
                if (energy <= 0) {
                    return true;
                }
                continue;
            }
            IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction.getOpposite());
            if (storage == null || !storage.canReceive()) {
                continue;
            }
            for (int i = 0; i < config.getGeneratorTransmissionCount(); i++) {
                energy -= storage.receiveEnergy((int) energy, false);
                if (energy <= 0) {
                    return true;
                }
            }
        }
        return energy <= 0;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            this.setItem(i, ItemStack.EMPTY);
        }
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable(config.getGeneratorBlock().getDescriptionId());
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int windowId, @NotNull Inventory playerInv) {
        return new GeneratorContainerMenu(windowId, playerInv, this);
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).getCount() == 0;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return slot <= getContainerSize() && slot >= 0 ? inventory.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int count) {
        if (slot < getContainerSize() && slot >= 0) {
            ItemStack stack = getItem(slot);
            if (count > stack.getCount()) {
                setItem(slot, ItemStack.EMPTY);
                return stack;
            } else {
                ItemStack newStack = stack.copy();
                newStack.setCount(count);
                stack.setCount(stack.getCount() - count);
                return newStack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = getItem(slot);
        setItem(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack item) {
        if (slot < getContainerSize() && slot >= 0) {
            inventory.set(slot, item);
        }
    }

    @Override
    public void setItems(NonNullList<ItemStack> items) {
        for (int i = 0; i < items.size() && i < inventory.size(); i++) {
            inventory.set(i, items.get(i));
        }
    }

    @Override
    public @NotNull NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    public int getMaxStack() {
        return config.getGeneratorMaxStack();
    }

    public double calculateEnergy(int itemStackCount) {
        return config.calculateEnergy(itemStackCount);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.getItem() == Registration.ENDER_CELL.get();
    }

    public Component getTitle() {
        return hasCustomName() ? getCustomName() : getDefaultName();
    }

    public double getCurrentOutput() {
        return currentOutput;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public GeneratorEnergyConnection getEnergyStorage() {
        return energyStorage;
    }
}
