package com.tyrellplayz.cdmcalculator;

import com.mrcrayfish.device.api.ApplicationManager;
import com.tyrellplayz.cdmcalculator.app.CalculatorApp;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod (modid=Reference.MOD_ID, name=Reference.MOD_NAME, version=Reference.MOD_VERSION, acceptedMinecraftVersions=Reference.ACCEPTED_MC_VERSIONS, dependencies=Reference.DEPENDS)
public class CDMCalculator {

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ApplicationManager.registerApplication(new ResourceLocation(Reference.MOD_ID, "calculator_app"), CalculatorApp.class);
	}
	
}
