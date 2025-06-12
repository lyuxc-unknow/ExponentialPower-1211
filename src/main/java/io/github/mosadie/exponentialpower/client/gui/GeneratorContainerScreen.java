package io.github.mosadie.exponentialpower.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.mosadie.exponentialpower.ExponentialPower;
import io.github.mosadie.exponentialpower.container.GeneratorContainerMenu;
import io.github.mosadie.exponentialpower.entities.GeneratorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GeneratorContainerScreen extends AbstractContainerScreen<GeneratorContainerMenu> {
    private final GeneratorEntity be;

    private final ResourceLocation GUI = ResourceLocation.fromNamespaceAndPath(ExponentialPower.MODID, "textures/gui/containerendergeneratorbe.png");

    public GeneratorContainerScreen(GeneratorContainerMenu container, Inventory playerInv, Component title) {
        super(container, playerInv, title);
        be = container.getBlockEntity();
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int p_97809_, int p_97810_) {
        super.renderLabels(graphics, p_97809_, p_97810_);
        graphics.drawString(Minecraft.getInstance().font, Component.translatable("screen.exponentialpower.generator_rate"), 10, 53, 0xffffff);
        graphics.drawString(Minecraft.getInstance().font, be.energy + " RF/t", 10, 63, 0xffffff);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTicks, int x, int y) {
        renderBackground(graphics);
        RenderSystem.setShaderTexture(0, GUI);
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(GUI, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
