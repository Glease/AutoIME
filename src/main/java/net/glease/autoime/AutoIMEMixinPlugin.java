package net.glease.autoime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class AutoIMEMixinPlugin implements IMixinConfigPlugin {

    public static final Logger LOG = LogManager.getLogger("AutoIME");

    @Override
    public void onLoad(String mixinPackage) {
        File dllPath = new File(Launch.minecraftHome, "autoime" + Tags.VERSION + ".dll");
        if (!Tags.VERSION.contains("dirty") && dllPath.exists() && checkDLL(dllPath)) {
            return;
        }
        try {
            InputStream dll = this.getClass().getClassLoader().getResourceAsStream("autoime.dll");
            if (dll == null) {
                return;
            }
            Files.copy(dll, dllPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOG.warn("Failed to release autoime dll", e);
        }
        if (!checkDLL(dllPath)) {
            LOG.warn("Released DLL doesn't work!");
        }
    }

    private static boolean checkDLL(File dllPath) {
        try {
            System.load(dllPath.getAbsolutePath());
        } catch (LinkageError e) {
            return false;
        }
        return true;
    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        if (Util.getOSType() == Util.EnumOS.WINDOWS && FMLLaunchHandler.side().isClient())
            return ImmutableList.of("MixinGuiTextField", "MixinMouseHelper");
//            return ImmutableList.of("MixinMouseHelper");
        return Collections.emptyList();
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
