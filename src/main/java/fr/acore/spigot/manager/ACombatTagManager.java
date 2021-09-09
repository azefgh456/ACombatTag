package fr.acore.spigot.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import fr.acore.spigot.api.plugin.IPlugin;
import fr.acore.spigot.api.runnable.IRunnable;
import fr.acore.spigot.commands.CommandCombat;
import fr.acore.spigot.listener.CombattagListener;
import fr.acore.spigot.module.AManager;
import fr.acore.spigot.module.AModule;
import fr.acore.spigot.utils.PlayerTimerBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;



public class ACombatTagManager extends AManager implements IRunnable {

	private AModule main;
	
	private List<PlayerTimerBuilder> combatTag;
	
	private String prefix;
	public String getPrefix() { return this.prefix;}
	private String permission;
	public String getPermission() { return this.permission;}
	private String version;
	public String getVersion() { return this.version;}
	private Long combatTime;
	public Long getCombatTime() { return this.combatTime;}
	private String messageInCombat;
	public String getMessageInCombat() { return this.messageInCombat;}
	private String messageCombatFinish;
	public String getMessageCombatFinish() { return this.messageCombatFinish;}
	private String messageCTEnCombat;
	public String getMessageCTEnCombat() { return this.messageCTEnCombat;}
	private String messageCTPasEnCombat;
	public String getMessageCTPasEnCombat() { return this.messageCTPasEnCombat;}
	private String messageCmdBloked;
	public String getMessageCmdBloked() { return this.messageCmdBloked;}
	private String deadMessage;
	public String getDeadMessage() { return this.deadMessage;}
	private List<String> commands;
	public List<String> getCommands(){ return this.commands;}
	
	public ACombatTagManager(AModule main) {
		super(main, true);
		this.main = main;
		combatTag = new ArrayList<>();
		registerListener(new CombattagListener(this));
		registerCommand(new CommandCombat(this));
		registerSyncRunnable(this);
		//main.registerPacket(SPacketCombatTag.class, 174, Sender.SERVER);
	}
	
	@Override
	public void setup(FileConfiguration config) {
		prefix = convertColor(config.getString("combatTag.prefix"));
		permission = config.getString("combatTag.permission");
		version = config.getString("combatTag.version");
		combatTime = config.getLong("combatTag.combatTime");
		messageInCombat = convertColor(config.getString("combatTag.messageInCombat"));
		messageCombatFinish = convertColor(config.getString("combatTag.messageCombatFinish"));
		messageCTEnCombat = convertColor(config.getString("combatTag.messageCTEnCombat"));
		messageCTPasEnCombat = convertColor(config.getString("combatTag.messageCTPasEnCombat"));
		messageCmdBloked = convertColor(config.getString("combatTag.messageCmdBloked"));
		deadMessage = convertColor(config.getString("combatTag.deadMessage"));
		
		commands = new ArrayList<>();
		
		for(String str : config.getStringList("combatTag.nonBlokedCmd")){
			commands.add("/"+convertColor(str));
		}
	}
	
	public void addPlayer(Player player) {
		combatTag.add(new PlayerTimerBuilder(player.getUniqueId().toString(), System.currentTimeMillis(), this.combatTime));
	}
	
	public void refreshPlayer(Player player) {
		getTimer(player).setCurrent(System.currentTimeMillis());
	}
	
	public void removePlayer(Player player) {
		if(containPlayer(player)) {
			combatTag.remove(getTimer(player));
		}
	}
	
	public void removeTimer(PlayerTimerBuilder builder) {
		combatTag.remove(builder);
	}
	
	public boolean containPlayer(Player player) {
		for(PlayerTimerBuilder bd : combatTag) {
			if(bd.getPlayer().equals(player.getUniqueId().toString()))return true;
		}
		return false;
	}
	
	public PlayerTimerBuilder getTimer(Player player) {
		for(PlayerTimerBuilder bd : combatTag) {
			if(bd.getPlayer().equals(player.getUniqueId().toString()))return bd;
		}
		return null;
	}

	@Override
	public void ticks() {
		Iterator<PlayerTimerBuilder> pl = combatTag.iterator();
		while(pl.hasNext()) {
			PlayerTimerBuilder timer = pl.next();
			if(timer.isFinish()) {
				Player p = Bukkit.getPlayer(UUID.fromString(timer.getPlayer()));
				if(p.isOnline()) p.sendMessage(messageCombatFinish.replace("%prefix%", prefix).replaceAll("&", "�"));
				pl.remove();
			}
		}
	}

	public int getCombatTimeToSecondes() {
		return (int) (combatTime / 1000);
	}

	@Override
	public IPlugin getPlugin() {
		return this.main;
	}

}
