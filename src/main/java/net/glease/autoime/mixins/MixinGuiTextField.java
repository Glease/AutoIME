package net.glease.autoime.mixins;

import net.glease.autoime.ImmUtil;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiTextField.class)
public class MixinGuiTextField {
    @Inject(method = "setFocused", at = @At(value = "HEAD"))
    public void autoime$onSetFocused(boolean focus, CallbackInfo ci) {
        if (focus) {
            ImmUtil.enable();
        } else {
            ImmUtil.disable();
        }
    }
}
