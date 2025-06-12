package io.github.mosadie.exponentialpower.blocks;

import io.github.mosadie.exponentialpower.EnergyLevelConfig;
import io.github.mosadie.exponentialpower.entities.StorageEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn, BlockHitResult hit) {
        if (!level.isClientSide) {
            StorageEntity te = (StorageEntity) level.getBlockEntity(pos);
            if (te == null) {
                return InteractionResult.FAIL;
            }
            double energy = te.getEnergy();
            double maxEnergy = config.getMaxEnergy();
            double percent = ((int) (energy / maxEnergy * 10000.00)) / 100.00;
            player.sendSystemMessage(Component.translatable("screen.exponentialpower.storage_total", energy, maxEnergy, percent));
        }
        return InteractionResult.SUCCESS;
    }
}