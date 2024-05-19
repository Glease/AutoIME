package net.glease.autoime;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

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
        if (!focusedTextBox.isEmpty()) {
            ImmUtil.enable();
        } else if (!AutoIME.isCurrentGuiWhitelisted()) {
            ImmUtil.disable();
        }
    }
}
