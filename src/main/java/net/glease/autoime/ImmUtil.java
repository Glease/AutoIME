package net.glease.autoime;

import java.lang.reflect.Method;

import org.lwjgl.opengl.Display;

public class ImmUtil {
    static long getHwnd() {
        try {
            Method getImplementation_method = Display.class.getDeclaredMethod("getImplementation");
            getImplementation_method.setAccessible(true);
            Object display_impl = getImplementation_method.invoke(null);
            Class<?> WindowsDisplay_class = Class.forName("org.lwjgl.opengl.WindowsDisplay");
            Method getHwnd_method = WindowsDisplay_class.getDeclaredMethod("getHwnd");
            getHwnd_method.setAccessible(true);
            return (Long) getHwnd_method.invoke(display_impl);
        } catch (ReflectiveOperationException ex) {
            AutoIMEMixinPlugin.LOG.error("Failed to get HWND", ex);
            return 0;
        }
    }

    public interface ImmUtilImpl {
        boolean init();

        void enable();

        void disable();

        void reinit();
    }

    private static volatile ImmUtilImpl instance;

    static boolean init() {
        if (instance == null) {
            synchronized (ImmUtil.class) {
                if (instance == null) {
                    try {
                        instance = new ImmUtilJNI();
                    } catch (Throwable ignore) {
                        try {
                            instance = new ImmUtilJNA();
                        } catch (Throwable e) {
                            AutoIMEMixinPlugin.LOG.error("Cannot initialize ImmUtil. Mod will NOT WORK", e);
                            return false;
                        }
                    }
                }
            }
        }
        return instance.init();
    }

    public static void disable() {
        if (!init()) {
            return;
        }
        instance.disable();
    }

    public static void enable() {
        if (!init()) {
            return;
        }
        instance.enable();
    }

    public static synchronized void reinit() {
        if (instance == null) return;
        instance.reinit();
    }

}
