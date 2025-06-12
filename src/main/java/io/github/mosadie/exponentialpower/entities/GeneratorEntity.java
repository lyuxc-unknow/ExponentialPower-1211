package io.github.mosadie.exponentialpower.entities;

import io.github.mosadie.exponentialpower.EnergyLevelConfig;
import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.container.GeneratorContainerMenu;
import io.github.mosadie.exponentialpower.energy.GeneratorEnergyConnection;
import io.github.mosadie.exponentialpower.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class GeneratorEntity extends BaseContainerBlockEntity implements ICapabilityProvider {
    private final EnergyLevelConfig config;
    public double currentOutput = 0;
    public double energy = 0;

    public NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

    private GeneratorEnergyConnection energyConnection;
    private final LazyOptional<GeneratorEnergyConnection> fecOptional = LazyOptional.of(() -> energyConnection);

    public GeneratorEntity(BlockPos pos, BlockState state, EnergyLevelConfig config) {
        super(config.getGeneratorBlockEntityType(), pos, state);
        this.config = config;
        energyConnection = new GeneratorEnergyConnection(this, true, false);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        return capability == ForgeCapabilities.ENERGY ? fecOptional.cast() : super.getCapability(capability, direction);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        ListTag nbtTagList = new ListTag();
        int slotsSize = getContainerSize();
        for (int i = 0; i < slotsSize; i++) {
            ItemStack stack = getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            CompoundTag itemTag = new CompoundTag();
            itemTag.putInt("Slot", i);
            stack.save(itemTag);
            nbtTagList.add(itemTag);
        }
        nbt.put("Items", nbtTagList);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
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
                setItem(slot, ItemStack.of(itemTags));
            }
        }
    }

    public static <T extends BlockEntity> void tick(T tile) {
        if (tile instanceof GeneratorEntity generator) {
            if (generator.getItem(0).getItem() == Registration.ENDER_CELL.get()) {
                generator.currentOutput = generator.calculateEnergy(generator.getItem(0).getCount());
                if (generator.currentOutput == 0) {
                    generator.currentOutput = 1;
                }
            } else {
                generator.currentOutput = 0;
            }
            generator.energy = generator.currentOutput;
            generator.handleSendingEnergy();
        }
    }

    private void handleSendingEnergy() {
        if (level == null) {
            return;
        }
        if (level.isClientSide) {
            return;
        }
        if (energy <= 0) {
            return;
        }
        int distance = config.getGeneratorMaxDistance();
        for (int i = -distance; i <= distance; i++) {
            for (int j = -distance; j <= distance; j++) {
                for (int k = -distance; k <= distance; k++) {
                    if (i == 0 && j == 0 && k == 0) {
                        continue;
                    }
                    BlockPos targetBlock = getBlockPos().offset(i, j, k);
                    BlockEntity blockEntity = level.getBlockEntity(targetBlock);
                    if (blockEntity == null) {
                        continue;
                    }
                    if (blockEntity instanceof StorageEntity storage) {
                        energy -= storage.acceptEnergy(energy);
                        continue;
                    }
                    blockEntity.getCapability(ForgeCapabilities.ENERGY, Direction.getNearest(i, j, k).getOpposite()).resolve().
                            filter(IEnergyStorage::canReceive)
                            .ifPresent((cap) -> energy -= cap.receiveEnergy((int) (energy > Integer.MAX_VALUE ? Integer.MAX_VALUE : energy), false));

                }
            }
        }
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
}
