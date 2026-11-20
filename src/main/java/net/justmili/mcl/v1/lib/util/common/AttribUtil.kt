package net.justmili.mcl.v1.lib.util.common

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.*
import java.util.*
import net.minecraft.core.Holder

object AttribUtil {

    @JvmStatic
    fun create(id: ResourceLocation, value: Double, operation: AttributeModifier.Operation) = AttributeModifier(/*? if >= 1.21 {*//*id*//*?} else {*/UUID.nameUUIDFromBytes(id.toString().toByteArray()), id.toString()/*?}*/, value, operation)

    @JvmStatic
    fun get(entity: LivingEntity, attribute: /*? if >= 1.20.5 {*//*Holder<Attribute>*//*?} else {*/Attribute/*?}*/): AttributeInstance? {
        return entity.getAttribute(attribute)
    }

    @JvmStatic
    fun getValue(entity: LivingEntity, attribute: /*? if >= 1.20.5 {*//*Holder<Attribute>*//*?} else {*/Attribute/*?}*/): Double {
        return entity.getAttributeValue(attribute)
    }

    @JvmStatic
    fun addTransient(instance: AttributeInstance?, modifier: AttributeModifier) {
        instance?.addTransientModifier(modifier)
    }

    @JvmStatic
    fun addPermanent(instance: AttributeInstance?, modifier: AttributeModifier) {
        instance?.addPermanentModifier(modifier)
    }

    @JvmStatic
    fun addTransient(instance: AttributeInstance?, id: ResourceLocation, value: Double, operation: AttributeModifier.Operation) {
        addTransient(instance, create(id, value, operation))
    }

    @JvmStatic
    fun addPermanent(instance: AttributeInstance?, id: ResourceLocation, value: Double, operation: AttributeModifier.Operation) {
        addPermanent(instance, create(id, value, operation))
    }

    @JvmStatic
    fun addOrUpdate(instance: AttributeInstance?, modifier: AttributeModifier) {
        if (instance == null) return
        //? if >= 1.20.5 {
        /*instance.addOrUpdateTransientModifier(modifier)
         *///?} else {
        instance.removeModifier(modifier)
        instance.addTransientModifier(modifier)
        //?}
    }

    @JvmStatic
    fun addOrReplace(instance: AttributeInstance?, modifier: AttributeModifier) {
        if (instance == null) return
        //? if >= 1.20.5 {
        /*instance.addOrReplacePermanentModifier(modifier)
         *///?} else {
        instance.removeModifier(modifier)
        instance.addPermanentModifier(modifier)
        //?}
    }

    @JvmStatic
    fun addOrUpdate(instance: AttributeInstance?, id: ResourceLocation, value: Double, operation: AttributeModifier.Operation) {
        addOrUpdate(instance, create(id, value, operation))
    }

    @JvmStatic
    fun addOrReplace(instance: AttributeInstance?, id: ResourceLocation, value: Double, operation: AttributeModifier.Operation) {
        addOrReplace(instance, create(id, value, operation))
    }
}