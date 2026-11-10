package net.justmili.mcl.v2.lib.codecs

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.minecraft.util.StringRepresentable

object EnumCodec {

    @JvmStatic
    fun <E : Enum<E>> of(type: Class<E>): Codec<E> {
        val constants = type.enumConstants
        for (i in constants.indices) {
            for (j in i + 1 until constants.size) {
                require(!nameOf(constants[i]).equals(nameOf(constants[j]), true)) { "Enum '${type.simpleName}' has constants with the same name." }
            }
        }

        return Codec.STRING.comapFlatMap<E>({ name ->
            val constant = type.enumConstants.firstOrNull { nameOf(it).equals(name, true) }
            if (constant != null) DataResult.success(constant) else DataResult.error<E> { "Unknown value '$name', allowed values: " + names(type) }
        }, { constant -> nameOf(constant) })
    }

    @JvmStatic
    fun nameOf(constant: Enum<*>): String {
        return if (constant is StringRepresentable) constant.serializedName else constant.name
    }

    @JvmStatic
    fun names(type: Class<out Enum<*>>): String {
        return type.enumConstants.joinToString(", ") { nameOf(it) }
    }
}