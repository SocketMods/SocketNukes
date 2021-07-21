package dev.socketmods.socketnukes.client.render.machine;

import dev.socketmods.socketnukes.client.models.BasicGeckolibModel;
import dev.socketmods.socketnukes.tileentity.machine.MainframeTE;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

/**
 * @brief Barebones TERenderer for the Mainframe.
 *
 * @author Curle
 */
public class MainframeRenderer extends GeoBlockRenderer<MainframeTE> {

    public MainframeRenderer(TileEntityRendererDispatcher renderManager) {
        super(renderManager, new BasicGeckolibModel("geo/mainframe.geo.json", "textures/blocks/mainframe.png", "animations/mainframe.anim.json"));
    }

}
