package net.glease.autoime;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

@Mod(modid = "autoime", name = "autoime", version = Tags.VERSION)
public class AutoIME {
    private static final List<Class<?>> forceEnableClasses = new ArrayList<>();
    private static final List<Class<?>> autoFocusedClasses = new ArrayList<>();

    public AutoIME() {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
        FMLCommonHandler.instance().bus().register(new FMLEventHandler());
    }

    public static boolean isGuiWhitelisted(GuiScreen screen) {
        if (screen == null) return false;
        return forceEnableClasses.stream().anyMatch(c -> c.isInstance(screen));
    }

    private static boolean isGuiHasAutoFocus(GuiScreen screen) {
        if (screen == null) return false;
        return autoFocusedClasses.stream().anyMatch(c -> c.isInstance(screen));
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        // sign doesn't even use a text box
        addForceEnableGUI(GuiEditSign.class.getName());
        // cba to work with something I don't use at all, yet we should not just break this mod outright
        addForceEnableGUI("vswe.stevesfactory.interfaces.GuiManager");
        // gtnh interface terminal autofocus on interface name field BEFORE gui open event is sent
        addAutoFocusGUI("com.glodblock.github.client.gui.GuiInterfaceWireless");
        addAutoFocusGUI("appeng.client.gui.implementations.GuiInterfaceTerminal");
    }

    private static void addForceEnableGUI(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ReflectiveOperationException ignore) {
            return;
        }
        forceEnableClasses.add(clazz);
    }

    private static void addAutoFocusGUI(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ReflectiveOperationException ignore) {
            return;
        }
        autoFocusedClasses.add(clazz);
    }

    public static class ForgeEventHandler {
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onGuiOpen(GuiOpenEvent event) {
            TextFieldFocusTracker.clear();
            if (isGuiWhitelisted(event.gui)) {
                ImmUtil.enable();
            } else if (isGuiHasAutoFocus(event.gui)) {
                Object lastFocusedTextBox = TextFieldFocusTracker.getLastFocusedTextBox();
                if (lastFocusedTextBox != null) {
                    TextFieldFocusTracker.markFocused(lastFocusedTextBox);
                }
            }
        }
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
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + "IME Reset Completed."));
            }
        }
    }
}
