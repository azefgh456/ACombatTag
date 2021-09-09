package fr.acore.spigot.utils;

import fr.acore.spigot.utils.time.TimerBuilder;
import org.bukkit.entity.Player;

public class PlayerTimerBuilder extends TimerBuilder {

	private String player;
	
	public PlayerTimerBuilder(String player, Long current, Long delay) {
		super(current, delay);
		this.player = player;
	}
	
	public PlayerTimerBuilder(Player player, Long delay) {
		super(System.currentTimeMillis(), delay);
		this.player = player.getUniqueId().toString();
	}
	
	public String getPlayer() {
		return this.player;
	}
	
	public boolean isPlayer(Player p) {
		if(p.getUniqueId().toString().equals(player)) return true;
		
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PlayerTimerBuilder) {
			PlayerTimerBuilder target = (PlayerTimerBuilder)obj;
			
			if(target.getDelay() == getDelay() && target.getCurrent() == getCurrent() && target.getPlayer().equals(getPlayer())) return true;
		}
		return false;
	}
}
