package net.justmili.mcl.v2.lib.codecs

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.minecraft.util.StringRepresentable

object EnumCodec {

    @JvmStatic
    fun <E : Enum<E>> of(enum: Class<E>): Codec<E> {
        val constants = enum.enumConstants
        for (i in constants.indices) {
            for (j in i + 1 until constants.size) {
                require(!nameOf(constants[i]).equals(nameOf(constants[j]), true)) { "Enum '${enum.simpleName}' has constants with the same name." }
            }
        }
        return Codec.STRING.comapFlatMap<E>({ name ->
            val constant = enum.enumConstants.firstOrNull { nameOf(it).equals(name, true) }
            if (constant != null) DataResult.success(constant) else DataResult.error<E> { "Unknown value '$name', allowed values: " + names(enum) }
        }, { constant -> nameOf(constant) })
    }

    @JvmStatic
    fun nameOf(constant: Enum<*>): String {
        return if (constant is StringRepresentable) constant.serializedName else constant.name
    }

    @JvmStatic
    fun names(enum: Class<out Enum<*>>): String {
        return enum.enumConstants.joinToString(", ") { nameOf(it) }
    }
}