package net.glease.autoime.mixins;

import net.glease.autoime.ImmUtil;
import net.minecraft.util.MouseHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHelper.class)
public class MixinMouseHelper {
    @Inject(method = "grabMouseCursor",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Mouse;setGrabbed(Z)V", remap = false)
    )
    public void autoime$onGrab(CallbackInfo ci) {
        ImmUtil.disable();
    }

    @Inject(method = "ungrabMouseCursor",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Mouse;setGrabbed(Z)V", remap = false))
    public void autoime$onUngrab(CallbackInfo ci) {
//        ImmUtil.enable();
    }
}
