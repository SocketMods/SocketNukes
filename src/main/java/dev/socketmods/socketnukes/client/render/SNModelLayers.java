package dev.socketmods.socketnukes.client.render;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class SNModelLayers {

    //------------------------------------------------------------------------------------------------------------------
    // Bolbs
    //------------------------------------------------------------------------------------------------------------------

    public static final ModelLayerLocation BOLB = create("bolb");
    public static final ModelLayerLocation BOLB_OUTER = create("bolb", "outer");
    public static final ModelLayerLocation BOLB_HAT = create("bolb", "hat");

    //------------------------------------------------------------------------------------------------------------------

    private static ModelLayerLocation create(String entity) {
        return create(entity, "main");
    }

    private static ModelLayerLocation create(String entity, String layer) {
        return new ModelLayerLocation(new ResourceLocation(SocketNukes.MODID, entity), layer);
    }

}
