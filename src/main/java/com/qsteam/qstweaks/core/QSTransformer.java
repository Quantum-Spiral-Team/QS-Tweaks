package com.qsteam.qstweaks.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Opcodes;

public class QSTransformer implements IClassTransformer, Opcodes {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        return basicClass;
    }
}