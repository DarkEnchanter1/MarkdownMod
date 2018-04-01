package com.darkenchanter.markdownchat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, clientSideOnly = true, canBeDeactivated = true)
public class Main
{
	public static final String MODID = "markdownchat";
	public static final String NAME = "Markdown Chat";
	public static final String VERSION = "1.0";
	private static com.darkenchanter.markdownchat.EventHandler handler = new com.darkenchanter.markdownchat.EventHandler();
	protected static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(handler);

	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
	@EventHandler
	public void deactivate(FMLModDisabledEvent event) {
		logger.info("It ran it!");
		MinecraftForge.EVENT_BUS.unregister(handler);
	}
}
