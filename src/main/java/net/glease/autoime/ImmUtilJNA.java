package net.glease.autoime;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;

public class ImmUtilJNA implements ImmUtil.ImmUtilImpl {

    private WinDef.HWND hwnd;
    private HIMC himc;
    private Imm32 IMM32 = Native.load("Imm32", Imm32.class);

    private void postAssociate(HIMC toSet) {
        AutoIMEMixinPlugin.LOG.debug("toSet {} saved {}", toSet, himc);
    }

    private WinDef.HWND getHwnd() {
        long hwnd = ImmUtil.getHwnd();
        if (hwnd == 0L) {
            return null;
        }
        return new WinDef.HWND(Pointer.createConstant(hwnd));
    }

    @Override
    public boolean init() {
        if (hwnd != null) return true;
        AutoIMEMixinPlugin.LOG.info("Using JNA Impl");
        hwnd = getHwnd();
        AutoIMEMixinPlugin.LOG.debug("HWND {}", hwnd);
        if (hwnd == null) {
            AutoIMEMixinPlugin.LOG.warn("Did not find active window. Wrong thread?");
        }
        return hwnd != null;
    }

    @Override
    public synchronized void enable() {
        HIMC curCtx = IMM32.ImmGetContext(hwnd);
        if (curCtx != null) return;
        HIMC toSet = himc;
        if (toSet == null) {
            toSet = IMM32.ImmCreateContext();
        }
        himc = IMM32.ImmAssociateContext(hwnd, toSet);
        postAssociate(toSet);
    }

    @Override
    public synchronized void disable() {
        HIMC curCtx = IMM32.ImmGetContext(hwnd);
        if (curCtx == null) return;
        himc = IMM32.ImmAssociateContext(hwnd, null);
        postAssociate(null);
    }

    public interface Imm32 extends StdCallLibrary {

        HIMC ImmGetContext(WinDef.HWND hwnd);

        HIMC ImmCreateContext();

        HIMC ImmAssociateContext(WinDef.HWND hwnd, HIMC newCtx);
    }

    public static class HIMC extends WinNT.HANDLE {
    }

    @Structure.FieldOrder({"dwStyle", "ptCurrentPos", "rcArea"})
    public static class CompositionForm extends Structure {
        public WinDef.DWORD dwStyle;
        public WinDef.POINT ptCurrentPos;
        public WinDef.RECT rcArea;

        public static CompositionForm create(WinDef.DWORD dwStyle, WinDef.POINT ptCurrentPos, WinDef.RECT rcArea) {
            CompositionForm form = new CompositionForm();
            form.dwStyle = dwStyle;
            form.ptCurrentPos = ptCurrentPos;
            form.rcArea = rcArea;
            return form;
        }
    }
}
