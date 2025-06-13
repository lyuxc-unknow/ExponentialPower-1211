package io.github.mosadie.exponentialpower.items;

import io.github.mosadie.exponentialpower.EnergyLevelConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class GeneratorItem extends BlockItem {
    private final EnergyLevelConfig config;

    public GeneratorItem(Block block, EnergyLevelConfig config) {
        super(block, new Item.Properties().fireResistant());
        this.config = config;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        double maxEnergy = config.calculateEnergy(config.getGeneratorMaxStack());
        int count = config.getGeneratorTransmissionCount();
        double maxOutput = (double) Integer.MAX_VALUE * count;
        int distance = config.getGeneratorMaxDistance();
        distance = distance * 2 + 1;
        tooltip.add(Component.translatable("item.exponentialpower.generator.tooltip.generated_max", maxEnergy));
        tooltip.add(Component.translatable("item.exponentialpower.generator.tooltip.output_max", maxOutput));
        tooltip.add(Component.translatable("item.exponentialpower.generator.tooltip.transmission_range", distance, distance, distance));
        tooltip.add(Component.translatable("item.exponentialpower.generator.tooltip.transmission_tips"));
    }
}