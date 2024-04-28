package net.glease.autoime;

public class ImmUtilJNI implements ImmUtil.ImmUtilImpl {

    private long hwnd;
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
    public void enable() {
        postAssociate(enable0());
    }

    private native long enable0();

    @Override
    public void disable() {
        postAssociate(disable0());
    }

    private native long disable0();
    private void postAssociate(long toSet) {
        AutoIMEMixinPlugin.LOG.debug("toSet {} saved {}", toSet, himc);
    }
}
