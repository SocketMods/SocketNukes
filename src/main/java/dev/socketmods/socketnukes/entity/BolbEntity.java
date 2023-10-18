package dev.socketmods.socketnukes.entity;

import dev.socketmods.socketnukes.client.render.bolb.BolbEntityRenderer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;

import java.util.HashMap;
import java.util.Map;

public class BolbEntity extends Slime {

    public BolbEntity(EntityType<? extends Slime> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier setupAttributes() {
        Map<Attribute, AttributeInstance> map = new HashMap<>();
        map.put(Attributes.MAX_HEALTH, new AttributeInstance(Attributes.MAX_HEALTH, inst -> inst.setBaseValue(6.0)));
        map.put(Attributes.ATTACK_DAMAGE, new AttributeInstance(Attributes.ATTACK_DAMAGE, inst -> inst.setBaseValue(3)));
        map.put(Attributes.MOVEMENT_SPEED, new AttributeInstance(Attributes.MOVEMENT_SPEED, inst -> inst.setBaseValue(0.5)));
        map.put(Attributes.FOLLOW_RANGE, new AttributeInstance(Attributes.FOLLOW_RANGE, inst -> inst.setBaseValue(20)));
        map.put(Attributes.ARMOR, new AttributeInstance(Attributes.ARMOR, inst -> inst.setBaseValue(0)));
        map.put(Attributes.ARMOR_TOUGHNESS, new AttributeInstance(Attributes.ARMOR_TOUGHNESS, inst -> inst.setBaseValue(0)));
        map.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeInstance(Attributes.KNOCKBACK_RESISTANCE, inst -> inst.setBaseValue(0)));
        map.put(ForgeMod.SWIM_SPEED.get(), new AttributeInstance(ForgeMod.SWIM_SPEED.get(), inst -> inst.setBaseValue(1)));
        map.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeInstance(ForgeMod.ENTITY_GRAVITY.get(), inst -> inst.setBaseValue(1)));
        return new AttributeSupplier(map);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void setSize(int size, boolean resetHealth) {
        // Overridden to raise the visibility
        super.setSize(size, resetHealth);
    }

    public ResourceLocation getEntityTexture() {
        return BolbEntityRenderer.getEntityTextureLocation(this);
    }
}
