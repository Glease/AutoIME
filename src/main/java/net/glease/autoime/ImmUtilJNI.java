package net.glease.autoime;

public class ImmUtilJNI implements ImmUtil.ImmUtilImpl {

    private volatile long hwnd;
    private long himc;

    @Override
    public boolean init() {
        if (hwnd != 0) return true;
        AutoIMEMixinPlugin.LOG.info("Using JNI Impl");
        hwnd = ImmUtil.getHwnd();
        AutoIMEMixinPlugin.LOG.debug("HWND {}", hwnd);
        if (hwnd == 0) {
            AutoIMEMixinPlugin.LOG.warn("Did not find active window. Wrong thread?");
        }
        return hwnd != 0;
    }

    @Override
    public synchronized void enable() {
        postAssociate(enable0());
    }

    private native long enable0();

    @Override
    public synchronized void disable() {
        postAssociate(disable0());
    }

    @Override
    public synchronized void reinit() {
        hwnd = 0;
        if (himc != 0) {
            destroyContext(himc);
            himc = 0;
        }
    }

    private static native void destroyContext(long himc);

    private native long disable0();
    private void postAssociate(long toSet) {
        AutoIMEMixinPlugin.LOG.debug("toSet {} saved {}", toSet, himc);
    }
}
