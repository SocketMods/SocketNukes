package dev.socketmods.socketnukes.entity;

import java.util.HashMap;
import java.util.Map;

import dev.socketmods.socketnukes.client.render.bolb.BolbEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;

public class BolbEntity extends SlimeEntity {

    public BolbEntity(EntityType<? extends SlimeEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap setupAttributes() {
        Map<Attribute, ModifiableAttributeInstance> map = new HashMap<>();
        map.put(Attributes.MAX_HEALTH, new ModifiableAttributeInstance(Attributes.MAX_HEALTH, inst -> inst.setBaseValue(6.0)));
        map.put(Attributes.ATTACK_DAMAGE, new ModifiableAttributeInstance(Attributes.ATTACK_DAMAGE, inst -> inst.setBaseValue(3)));
        map.put(Attributes.MOVEMENT_SPEED, new ModifiableAttributeInstance(Attributes.MOVEMENT_SPEED, inst -> inst.setBaseValue(0.5)));
        map.put(Attributes.FOLLOW_RANGE, new ModifiableAttributeInstance(Attributes.FOLLOW_RANGE, inst -> inst.setBaseValue(20)));
        map.put(Attributes.ARMOR, new ModifiableAttributeInstance(Attributes.ARMOR, inst -> inst.setBaseValue(0)));
        map.put(Attributes.ARMOR_TOUGHNESS, new ModifiableAttributeInstance(Attributes.ARMOR_TOUGHNESS, inst -> inst.setBaseValue(0)));
        map.put(Attributes.KNOCKBACK_RESISTANCE, new ModifiableAttributeInstance(Attributes.KNOCKBACK_RESISTANCE, inst -> inst.setBaseValue(0)));
        map.put(ForgeMod.SWIM_SPEED.get(), new ModifiableAttributeInstance(ForgeMod.SWIM_SPEED.get(), inst -> inst.setBaseValue(1)));
        map.put(ForgeMod.ENTITY_GRAVITY.get(), new ModifiableAttributeInstance(ForgeMod.ENTITY_GRAVITY.get(), inst -> inst.setBaseValue(1)));
        return new AttributeModifierMap(map);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
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
