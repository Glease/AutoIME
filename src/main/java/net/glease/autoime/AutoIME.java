package net.glease.autoime;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import vswe.stevesfactory.interfaces.GuiManager;

@Mod(modid = "autoime", name = "autoime", version = Tags.VERSION)
public class AutoIME {
    public AutoIME() {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
        FMLCommonHandler.instance().bus().register(new FMLEventHandler());
    }

    public static boolean isCurrentGuiWhitelisted() {
        return Minecraft.getMinecraft().currentScreen instanceof GuiEditSign;
    }

    public static class ForgeEventHandler {
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onGuiOpen(GuiOpenEvent event) {
            TextFieldFocusTracker.clear();
            if (isWhitelistedGUI(event)) {
                ImmUtil.enable();
            }
        }
    }

    private static boolean isWhitelistedGUI(GuiOpenEvent event) {
        if (event.gui instanceof GuiEditSign) return true;
        if (Loader.isModLoaded("StevesFactoryManager") && event.gui instanceof GuiManager) return true;
        return false;
    }

    public static class FMLEventHandler {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                TextFieldFocusTracker.onTickEnd();
            }
        }

        @SubscribeEvent
        public void onKeyInput(InputEvent.KeyInputEvent event) {
            if (Minecraft.getMinecraft().currentScreen == null &&
                Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_C)) {
                ImmUtil.reinit();
                ImmUtil.disable();
            }
        }
    }
}
