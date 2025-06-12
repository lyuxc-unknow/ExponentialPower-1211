package io.github.mosadie.exponentialpower.items;

import io.github.mosadie.exponentialpower.EnergyLevelConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class StorageItem extends BlockItem {
    private final EnergyLevelConfig config;

    public StorageItem(Block block, EnergyLevelConfig config) {
        super(block, new Properties().stacksTo(1).fireResistant());
        this.config = config;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        double energy = 0;
        if (stack.hasTag()) {
            CompoundTag blockEntityTag = stack.getTagElement("BlockEntityTag");
            if (blockEntityTag != null && blockEntityTag.contains("energy")) {
                energy = blockEntityTag.getDouble("energy");
            }
        }
        double maxEnergy = config.getMaxEnergy();
        tooltip.add(Component.translatable("item.exponentialpower.storage.tooltip.stored"));
        tooltip.add(Component.literal(energy + "/" + maxEnergy));
        double percent = ((int) (energy / maxEnergy * 10000.00)) / 100.00;
        tooltip.add(Component.literal("(" + percent + "%)"));
    }
}