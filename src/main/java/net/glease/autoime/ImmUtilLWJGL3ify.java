package net.glease.autoime;

import org.lwjglx.opengl.Display;

public class ImmUtilLWJGL3ify {
    public static long getHwnd() {
        return ImmUtilLWJGL3.getHwnd(Display.getWindow());
    }
}
