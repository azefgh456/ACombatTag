package fr.acore.spigot;


import fr.acore.spigot.manager.ACombatTagManager;
import fr.acore.spigot.module.AModule;

public class ACombatTag extends AModule {
	
	@Override
	public void onEnable() {
		super.onEnable();
		registerManager(new ACombatTagManager(this));
		log("ACombatTag Enabled");
	}

}
