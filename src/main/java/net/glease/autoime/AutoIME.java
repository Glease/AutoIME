package net.glease.autoime;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;

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
            if (event.gui instanceof GuiEditSign) {
                ImmUtil.enable();
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
    }
}
