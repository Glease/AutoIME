package net.glease.autoime.mixins;

import net.glease.autoime.ImmUtil;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Display.class)
public class MixinDisplay {
    @Inject(method = "createWindow", at = @At("TAIL"), remap = false)
    public void onWindowCreated(CallbackInfo ci) {
        ImmUtil.reinit();
    }
}
