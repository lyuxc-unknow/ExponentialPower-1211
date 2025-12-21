package io.github.mosadie.exponentialpower.blocks;

import io.github.mosadie.exponentialpower.EnergyLevelConfig;
import io.github.mosadie.exponentialpower.entities.StorageEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StorageBlock extends Block implements EntityBlock {
    private final EnergyLevelConfig config;

    public StorageBlock(Properties properties, EnergyLevelConfig config) {
        super(properties);
        this.config = config;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new StorageEntity(pos, state, config);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return type == config.getStorageBlockEntityType() ? (l, p, s, tile) -> StorageEntity.tick(tile) : null;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (!level.isClientSide) {
            StorageEntity te = (StorageEntity) level.getBlockEntity(pos);
            if (te == null) {
                return InteractionResult.FAIL;
            }
            double energy = te.getEnergy();
            double maxEnergy = config.getStorageMaxEnergy();
            double percent = ((int) (energy / maxEnergy * 10000.00)) / 100.00;
            player.sendSystemMessage(Component.translatable("screen.exponentialpower.storage_total_percent", percent));
            player.sendSystemMessage(Component.translatable("screen.exponentialpower.storage_total_current", energy));
            player.sendSystemMessage(Component.translatable("screen.exponentialpower.storage_total_max", maxEnergy));
        }
        return InteractionResult.SUCCESS;
    }
}