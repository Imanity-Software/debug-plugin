package org.example.template.listener;

import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.atomic.AtomicInteger;

@InjectableComponent
@RegisterAsListener
public class DebugListener implements Listener {

    public static AtomicInteger count = new AtomicInteger(0);

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        System.out.println(player.getItemInHand());
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getItemInHand().getType() == Material.MUSHROOM_SOUP) {
                player.getItemInHand().setType(Material.BOWL);
                player.playSound(player.getLocation(), org.bukkit.Sound.BURP, 1.0F, 1.0F);
                player.sendMessage("You ate the soup!");
                count.incrementAndGet();
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile instanceof FishHook) {
            Player shooter = (Player) event.getEntity().getShooter();

            shooter.sendMessage("You have launched a fishing rod!");
            count.incrementAndGet();
        }
    }

}
