package com.qsteam.qstweaks.core.transformer.modintegration;

import com.qsteam.qstweaks.core.QSLoadingPlugin;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class MoarTConPluginTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("com.existingeevee.moretcon.mixinext.MoarTConCorePlugin")) {
            QSLoadingPlugin.LOGGER.info("[{}] Transforming MoarTConCorePlugin to strip constructor and inject IEarlyMixinLoader...", MoarTConPluginTransformer.class.getSimpleName());
            return transformPlugin(basicClass);
        }
        return basicClass;
    }

    private byte[] transformPlugin(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        String interfaceName = "zone/rong/mixinbooter/IEarlyMixinLoader";
        if (!classNode.interfaces.contains(interfaceName)) {
            classNode.interfaces.add(interfaceName);
        }

        for (MethodNode method : classNode.methods) {
            if (method.name.equals("<init>") && method.desc.equals("()V")) {
                method.instructions.clear();
                method.localVariables.clear();

                method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                method.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false));
                method.instructions.add(new InsnNode(Opcodes.RETURN));

                method.maxStack = 1;
                method.maxLocals = 1;
            }
        }

        classNode.methods.removeIf(m -> m.name.equals("getMixinConfigs") && m.desc.equals("()Ljava/util/List;"));

        MethodNode getMixinConfigsMethod = new MethodNode(
                Opcodes.ACC_PUBLIC,
                "getMixinConfigs",
                "()Ljava/util/List;",
                "()Ljava/util/List<Ljava/lang/String;>;",
                null
        );

        InsnList il = getMixinConfigsMethod.instructions;

        il.add(new TypeInsnNode(Opcodes.NEW, "java/util/ArrayList"));
        il.add(new InsnNode(Opcodes.DUP));

        il.add(new FieldInsnNode(
                Opcodes.GETSTATIC,
                "com/existingeevee/moretcon/mixinext/MoarTConCorePlugin",
                "vanillaMixins",
                "Ljava/lang/String;"
        ));

        il.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/util/Collections", "singleton", "(Ljava/lang/Object;)Ljava/util/Set;", false));
        il.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "(Ljava/util/Collection;)V", false));
        il.add(new InsnNode(Opcodes.ARETURN));

        getMixinConfigsMethod.maxStack = 4;
        getMixinConfigsMethod.maxLocals = 1;
        classNode.methods.add(getMixinConfigsMethod);

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
