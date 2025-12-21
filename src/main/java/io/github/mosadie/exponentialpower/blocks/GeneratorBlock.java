package io.github.mosadie.exponentialpower.blocks;

import io.github.mosadie.exponentialpower.EnergyLevelConfig;
import io.github.mosadie.exponentialpower.container.GeneratorContainerMenu;
import io.github.mosadie.exponentialpower.entities.GeneratorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class GeneratorBlock extends Block implements EntityBlock {
    private final EnergyLevelConfig config;

    public GeneratorBlock(Properties properties, EnergyLevelConfig config) {
        super(properties);
        this.config = config;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new GeneratorEntity(pos, state, config);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return type == config.getGeneratorBlockEntityType() ? (l, p, s, tile) -> GeneratorEntity.tick(tile) : null;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof GeneratorEntity entity) {
                player.openMenu(new GUIMenuProvider(entity), entity.getBlockPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }


    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        // Custom name handling is done automatically by parent class in 1.21.1
    }

    @Override
    public void onRemove(BlockState oldState, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean bool) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof Container) {
                Containers.dropContents(level, pos, (Container) blockentity);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(oldState, level, pos, newState, bool);
        }
    }

    private record GUIMenuProvider(GeneratorEntity entity) implements MenuProvider {
        @Override
        public @NotNull Component getDisplayName() {
            return entity.getTitle();
        }

        @Override
        public AbstractContainerMenu createMenu(int i, @NotNull Inventory playerInventory, @NotNull Player playerEntity) {
            return new GeneratorContainerMenu(i, playerInventory, entity);
        }
    }
}
