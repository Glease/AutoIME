package net.glease.autoime;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import cpw.mods.fml.common.Loader;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.fullscreen.MapChat;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.reflect.FieldUtils;

public class TextFieldFocusTracker {
    private static final Set<Object> focusedTextBox = Collections.newSetFromMap(new WeakHashMap<>());

    public static void markFocused(Object obj) {
        focusedTextBox.add(obj);
    }

    public static void markUnfocused(Object obj) {
        focusedTextBox.remove(obj);
    }

    public static void clear() {
        focusedTextBox.clear();
    }

    public static void onTickEnd() {
        if (checkJM()) return;
        if (!focusedTextBox.isEmpty()) {
            ImmUtil.enable();
        } else if (!AutoIME.isGuiWhitelisted(Minecraft.getMinecraft().currentScreen)) {
            ImmUtil.disable();
        }
    }

    public static boolean checkJM() {
        if (!Loader.isModLoaded("journeymap")) return false;
        if (Minecraft.getMinecraft().currentScreen instanceof Fullscreen) {
            JMCompat.check((Fullscreen) Minecraft.getMinecraft().currentScreen);
            return true;
        }
        return false;
    }

    private static class JMCompat {
        static Field fieldChat = FieldUtils.getDeclaredField(Fullscreen.class, "chat", true);

        public static void check(Fullscreen jm) {
            try {
                MapChat o = (MapChat) fieldChat.get(jm);
                if (o != null && !o.isHidden()) {
                    ImmUtil.enable();
                } else {
                    ImmUtil.disable();
                }
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
