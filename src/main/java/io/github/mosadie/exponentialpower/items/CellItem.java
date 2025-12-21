package io.github.mosadie.exponentialpower.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CellItem extends Item {
    public CellItem() {
        super(new Item.Properties().fireResistant().stacksTo(64));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        tooltip.add(Component.translatable("item.exponentialpower.ender_cell.tooltip1"));
        tooltip.add(Component.translatable("item.exponentialpower.ender_cell.tooltip2"));
    }
}
