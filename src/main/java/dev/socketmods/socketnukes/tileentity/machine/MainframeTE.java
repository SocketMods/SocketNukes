package dev.socketmods.socketnukes.tileentity.machine;

import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/**
 * @brief The Tile Entity for the Mainframe.
 * Has a do-nothing animation by default.
 * This should be the only thing in the Launch System that ticks.
 *
 * TODO: document how the Launch System works.
 *
 * @author Curle
 */
public class MainframeTE extends TileEntity implements ITickableTileEntity, IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);

    public MainframeTE() {
        super(SNRegistry.MAINFRAME_TE.get());
    }

    @Override
    public void tick() {
        // TODO
    }


    /******************
     * Geckolib stuff.
     */

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mainframe.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

}
