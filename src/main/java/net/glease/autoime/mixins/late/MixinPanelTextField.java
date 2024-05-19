package net.glease.autoime.mixins.late;

import betterquesting.api2.client.gui.controls.PanelTextField;
import net.glease.autoime.ImmUtil;
import net.glease.autoime.TextFieldFocusTracker;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PanelTextField.class)
public class MixinPanelTextField {
    @Shadow(remap = false)
    private boolean isFocused;
    @Inject(method = "/.*/", remap = false, at = @At(remap = false, value = "FIELD", target = "Lbetterquesting/api2/client/gui/controls/PanelTextField;isFocused:Z", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER, by = 1))
    public void autoime$onFocusChanged(CallbackInfo ci) {
        if (isFocused) {
            TextFieldFocusTracker.markFocused(this);
        } else {
            TextFieldFocusTracker.markUnfocused(this);
        }
    }
}
