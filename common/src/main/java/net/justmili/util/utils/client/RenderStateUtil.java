package net.justmili.util.utils.client;

import net.justmili.corelibs.CoreLibs;

public class RenderStateUtil {

    // RenderStateUtil code by BluSpring
//    public static <S1, S2> void copyTo(S1 source, S2 destination) {
//        var sourceClass = source.getClass();
//        var destClass = destination.getClass();
//
//        for (var field : sourceClass.getFields()) {
//            try {
//                if (!Modifier.isPublic(field.getModifiers()) || Modifier.isStatic(field.getModifiers())) // avoid syncing stuff that we normally can't modify
//                    continue;
//
//                if (!field.getDeclaringClass().isAssignableFrom(sourceClass)) // if it's not from similar classes, don't.
//                    continue;
//
//                Field otherField = destClass.getField(field.getName());
//
//                if (otherField.getDeclaringClass() != field.getDeclaringClass()) // if these fields don't come from the same class, they're not logically copiable.
//                    continue;
//
//                if (otherField.getType() != field.getType()) // they're not equivalent, don't.
//                    continue;
//
//                // Normally, these final fields are RenderStates themselves. I highly recommend you try to sync those via a recursive copyTo, or at least gate them.
//                if (Modifier.isFinal(field.getModifiers())) {
//                    otherField.setAccessible(true); // it's probably fine
//                }
//
//                var data = field.get(source);
//                otherField.set(destination, data);
//            } catch (Throwable ignored) {}
//        }
//    }
}