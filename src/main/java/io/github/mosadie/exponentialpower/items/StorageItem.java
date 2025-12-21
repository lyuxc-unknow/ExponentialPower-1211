package io.github.mosadie.exponentialpower.items;

import io.github.mosadie.exponentialpower.EnergyLevelConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StorageItem extends BlockItem {
    private final EnergyLevelConfig config;

    public StorageItem(Block block, EnergyLevelConfig config) {
        super(block, new Properties().stacksTo(1).fireResistant());
        this.config = config;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        double energy = 0;
        CustomData customData = stack.getOrDefault(DataComponents.BLOCK_ENTITY_DATA, CustomData.EMPTY);
        if (!customData.isEmpty()) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("energy")) {
                energy = tag.getDouble("energy");
            }
        }
        double maxEnergy = config.getStorageMaxEnergy();
        double percent = ((int) (energy / maxEnergy * 10000)) / 100.00;
        tooltip.add(Component.translatable("item.exponentialpower.storage.tooltip.stored_percent", percent));
        tooltip.add(Component.translatable("item.exponentialpower.storage.tooltip.stored_current", energy));
        tooltip.add(Component.translatable("item.exponentialpower.storage.tooltip.stored_max", maxEnergy));
    }
}