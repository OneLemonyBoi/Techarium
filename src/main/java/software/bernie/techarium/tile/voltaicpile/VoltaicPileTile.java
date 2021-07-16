package software.bernie.techarium.tile.voltaicpile;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import software.bernie.techarium.block.voltaicpile.Charge;
import software.bernie.techarium.block.voltaicpile.VoltaicPileBlock;
import software.bernie.techarium.machine.sideness.FaceConfig;
import software.bernie.techarium.machine.sideness.Side;
import software.bernie.techarium.tile.base.TechariumTileBase;
import software.bernie.techarium.trait.tile.TileBehaviours;

import static software.bernie.techarium.registry.BlockRegistry.VOLTAIC_PILE;

public class VoltaicPileTile extends TechariumTileBase {
    public VoltaicPileTile() {
        super(VOLTAIC_PILE.getTileEntityType(), TileBehaviours.voltaicPile);

        for (Side side: Side.values()) {
            getFaceConfigs().put(side, FaceConfig.PUSH_ONLY);
        }
    }

    @Override
    public ActionResultType onTileActivated(PlayerEntity player) {
        return ActionResultType.PASS;
    }

    @Override
    public void tick() {
        getPowerTrait().ifPresent(trait -> {
            if (level.getDayTime() % 40 == 0 && !level.isClientSide()) {
                for (Direction d : Direction.values()) {
                    if (level.getBlockState(worldPosition.relative(d)).getBlock() instanceof FireBlock && trait.getEnergyStorage().getEnergyStored() > 0) {
                        explode();
                    }
                }
            }
        });
        super.tick();
    }

    @Override
    protected void updateMachineTile() {
        getPowerTrait().ifPresent(trait -> {
            float percentStored = (float) trait.getEnergyStorage().getEnergyStored() / trait.getEnergyStorage().getMaxEnergyStored();
            if (percentStored == 0) {
                if (getBlockState().getValue(VoltaicPileBlock.CHARGE) != Charge.EMPTY) {
                    this.getLevel().setBlockAndUpdate(worldPosition, this.getBlockState().setValue(VoltaicPileBlock.CHARGE, Charge.EMPTY));
                }
            }
            else if (percentStored <= 0.33) {
                if (getBlockState().getValue(VoltaicPileBlock.CHARGE) != Charge.ONE_THIRD) {
                    this.getLevel().setBlockAndUpdate(worldPosition, this.getBlockState().setValue(VoltaicPileBlock.CHARGE, Charge.ONE_THIRD));
                }
            }
            else if (percentStored <= 0.66) {
                if (getBlockState().getValue(VoltaicPileBlock.CHARGE) != Charge.TWO_THIRD) {
                    this.getLevel().setBlockAndUpdate(worldPosition, this.getBlockState().setValue(VoltaicPileBlock.CHARGE, Charge.TWO_THIRD));
                }            }
            else {
                if (getBlockState().getValue(VoltaicPileBlock.CHARGE) != Charge.FULL) {
                    this.getLevel().setBlockAndUpdate(worldPosition, this.getBlockState().setValue(VoltaicPileBlock.CHARGE, Charge.FULL));
                }
            }
        });
        super.updateMachineTile();
    }

    public void explode() {
        getPowerTrait().ifPresent(trait -> {
            BlockPos pos = getBlockPos();
            float explosionPower = trait.getEnergyStorage().getEnergyStored() / 100F;
            level.destroyBlock(pos, false);
            level.explode(null, pos.getX(), pos.getY(), pos.getZ(), explosionPower, Explosion.Mode.DESTROY);
        });
    }
}