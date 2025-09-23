package net.glease.autoime;

import me.eigenraven.lwjgl3ify.api.Lwjgl3Aware;
import org.lwjgl.glfw.GLFWNativeWin32;


@Lwjgl3Aware
public class ImmUtilLWJGL3 {
    public static long getHwnd(long windowHandle) {
        return GLFWNativeWin32.glfwGetWin32Window(windowHandle);
    }
}
