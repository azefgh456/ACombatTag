package fr.acore.spigot.listener;

import fr.acore.spigot.manager.ACombatTagManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;



public class CombattagListener implements Listener{
	
	private ACombatTagManager combatM;
	
	public CombattagListener(ACombatTagManager combatM) {
		this.combatM = combatM;
	}
	
	@EventHandler
	public void onPvP(EntityDamageByEntityEvent event){
		
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player){
			Player player = (Player)event.getEntity();
			Player attacker = (Player)event.getDamager();
			if (event.isCancelled()){return;}
			
			if (!player.getGameMode().equals(GameMode.CREATIVE) && !attacker.getGameMode().equals(GameMode.CREATIVE) ){
				if (player.hasPermission(combatM.getPermission()) || attacker.hasPermission(combatM.getPermission()) ){
					return;
				}
				if (combatM.containPlayer(player)){
					setCombatTimePlayer(player, attacker, true);
				}else{
					setCombatTimePlayer(player, attacker, false);
				}
				if (combatM.containPlayer(attacker)){
					setCombatTimePlayer(attacker, player, true);
				}else{
					setCombatTimePlayer(attacker, player, false);
				}
			}
		}
	}
	
	@EventHandler
	public void onPunch(EntityDamageByEntityEvent event){
		
		if(event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
	        if (event.isCancelled() || event.getDamage() == 0.0) {
	            return;
	        }
	        Entity dmgr = event.getDamager();
	        if (dmgr instanceof Projectile && ((Projectile)dmgr).getShooter() instanceof Entity) {
	            dmgr = (Entity)((Projectile)dmgr).getShooter();
	        }
	        if(dmgr instanceof Player){
	        	Player attacker = (Player)dmgr;
				if (!player.getGameMode().equals(GameMode.CREATIVE) && !attacker.getGameMode().equals(GameMode.CREATIVE) ){
					if (player.hasPermission(combatM.getPermission()) || attacker.hasPermission(combatM.getPermission()) ){
						return;
					}
					if (combatM.containPlayer(player)){
						setCombatTimePlayer(player, attacker, true);
					}else{
						setCombatTimePlayer(player, attacker, false);
					}
					if (combatM.containPlayer(attacker)){
						setCombatTimePlayer(attacker, player, true);
					}else{
						setCombatTimePlayer(attacker, player, false);
					}
				}
	        }
		}
	}
	
	
	@EventHandler
	public void quit(PlayerQuitEvent e){
		Player player = e.getPlayer();
		if (combatM.containPlayer(player) && !player.hasPermission(combatM.getPermission())){
			Bukkit.broadcastMessage(combatM.getDeadMessage().replace("%prefix%", combatM.getPrefix()).replace("%player%", player.getName()));
			player.damage(40.0);
			combatM.removePlayer(player);
		}
	}
	
	@EventHandler
	public void command(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
		if (combatM.containPlayer(player) && !player.hasPermission(combatM.getPermission())){
			for(String cmd : combatM.getCommands()){
				if (e.getMessage().equalsIgnoreCase(cmd)){
					return;
				}
			}
			e.setCancelled(true);
			player.sendMessage(combatM.getMessageCmdBloked().replace("%prefix%", combatM.getPrefix()));
			return;
		}
	}
	
	private void setCombatTimePlayer(Player player, Player tager, boolean combat){
		if(combat){
			combatM.refreshPlayer(player);
		}else{
			combatM.addPlayer(player);
			player.sendMessage(combatM.getMessageInCombat().replace("%prefix%", combatM.getPrefix()).replace("%player%", tager.getName()).replace("%time%", ""+combatM.getCombatTimeToSecondes()));
		}
		//((APlugin)combatM.getPlugin()).sendPacket(new SPacketCombatTag(combatM), player, tager);
	}

}
