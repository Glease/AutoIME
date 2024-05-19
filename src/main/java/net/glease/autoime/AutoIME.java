package net.glease.autoime;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "autoime", name = "autoime", version = Tags.VERSION)
public class AutoIME {
    public AutoIME() {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
    }

    public static class ForgeEventHandler {
        @SubscribeEvent
        public void onGuiOpen(GuiOpenEvent event) {
            if (event.gui instanceof GuiEditSign) {
                ImmUtil.enable();
            }
        }
    }
}
