package fr.acore.spigot.commands;

import fr.acore.spigot.api.command.CommandStats;
import fr.acore.spigot.api.command.sender.ICommandSender;
import fr.acore.spigot.api.player.impl.CorePlayer;
import fr.acore.spigot.commands.sender.CorePlayerSender;
import fr.acore.spigot.manager.ACombatTagManager;
import org.bukkit.entity.Player;

import java.util.Arrays;


public class CommandCombat extends APlayerCommand{

	private ACombatTagManager combatM;
	
	public CommandCombat(ACombatTagManager combatM) {
		super("combattag");
		this.combatM = combatM;
		setAlliases(Arrays.asList("combat", "ct"));
	}

	@Override
	public CommandStats performAPlayerCommand(CorePlayerSender sender, String... args){
		if (combatM.containPlayer(sender.getSender().getPlayer())){
			sender.getSender().sendMessage(combatM.convertColor(combatM.getMessageCTEnCombat().replace("%prefix%", combatM.getPrefix()).replace("%time%", combatM.getTimer(sender.getSender().getPlayer()).getFromatedRemainingTime())));
		}else{
			sender.getSender().sendMessage(combatM.getMessageCTPasEnCombat().replace("%prefix%", combatM.getPrefix()).replaceAll("&", "�"));
		}
		return CommandStats.SUCCESS;
	}
	
	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return null;
	}
}
