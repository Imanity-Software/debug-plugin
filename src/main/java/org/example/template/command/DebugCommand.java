package org.example.template.command;

import io.fairyproject.bukkit.command.event.BukkitCommandContext;
import io.fairyproject.bukkit.reflection.wrapper.ObjectWrapper;
import io.fairyproject.command.BaseCommand;
import io.fairyproject.command.annotation.Command;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.mc.scheduler.MCSchedulers;
import io.fairyproject.util.CC;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.example.template.listener.DebugListener;

import java.util.concurrent.ThreadLocalRandom;

@InjectableComponent
@Command("test")
public class DebugCommand extends BaseCommand {

    @Command("#")
    public void debug(BukkitCommandContext context) {
        Player player = context.getPlayer();

        soupMulti(player);
    }

    private static void soupMulti(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.setHeldItemSlot(8);
        inventory.setItem(0, new ItemStack(Material.MUSHROOM_SOUP));
        inventory.setItem(1, new ItemStack(Material.MUSHROOM_SOUP));
        inventory.setItem(2, new ItemStack(Material.MUSHROOM_SOUP));
        inventory.setItem(3, new ItemStack(Material.MUSHROOM_SOUP));
        inventory.setItem(4, new ItemStack(Material.MUSHROOM_SOUP));
        inventory.setItem(5, new ItemStack(Material.MUSHROOM_SOUP));
        inventory.setItem(6, new ItemStack(Material.MUSHROOM_SOUP));
        inventory.setItem(7, new ItemStack(Material.MUSHROOM_SOUP));
        inventory.setItem(8, new ItemStack(Material.MUSHROOM_SOUP));
        player.updateInventory();

        Thread thread = new Thread(() -> {
            int millis = ThreadLocalRandom.current().nextInt(0, 50);
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            player.sendMessage("Start Packet manipulation, " + player.getName() + "! (" + CC.RED +  millis + CC.WHITE + ")");

            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
            PlayerConnection playerConnection = entityPlayer.playerConnection;

            for (int i = 0; i < 9; i++) {
                PacketPlayInHeldItemSlot packetplayinhelditemslot = new PacketPlayInHeldItemSlot();
                new ObjectWrapper(packetplayinhelditemslot).setField("itemInHandIndex", i);
                new ObjectWrapper(packetplayinhelditemslot).setField("nano", System.nanoTime());
                try {
                    playerConnection.a(packetplayinhelditemslot);
                } catch (Throwable throwable) {
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }

                try {
                    net.minecraft.server.v1_8_R3.ItemStack itemInHand = entityPlayer.inventory.getItemInHand();
                    PacketPlayInBlockPlace packetplayinblockplace = new PacketPlayInBlockPlace(itemInHand);
                    new ObjectWrapper(packetplayinblockplace).setField("timestamp", System.currentTimeMillis());
                    new ObjectWrapper(packetplayinblockplace).setField("nano", System.nanoTime());
                    playerConnection.a(packetplayinblockplace);
                } catch (CancelledPacketHandleException throwable) {
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

                for (int j = 0; j < ThreadLocalRandom.current().nextInt(1, 4); j++) {
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(1, 3));
                    } catch (Throwable throwable){
                    }

                    try {
                        net.minecraft.server.v1_8_R3.ItemStack itemInHand = entityPlayer.inventory.getItemInHand();
                        PacketPlayInBlockPlace packetplayinblockplace = new PacketPlayInBlockPlace(itemInHand);
                        new ObjectWrapper(packetplayinblockplace).setField("timestamp", System.currentTimeMillis());
                        new ObjectWrapper(packetplayinblockplace).setField("nano", System.nanoTime());
                        playerConnection.a(packetplayinblockplace);
                    } catch (CancelledPacketHandleException throwable) {
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }

                PacketPlayInBlockDig drop = new PacketPlayInBlockDig();
                new ObjectWrapper(drop).setField("c", PacketPlayInBlockDig.EnumPlayerDigType.DROP_ITEM);
                new ObjectWrapper(drop).setField("nano", System.nanoTime());

                try {
                    playerConnection.a(drop);
                } catch (Throwable throwable) {
                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

            int i = DebugListener.count.get();
            if (i > 8) {
                DebugListener.count.set(0);
                MCSchedulers.getGlobalScheduler().schedule(() -> {
                    player.performCommand("test");
                }, 10L);
            } else {
                player.sendMessage(CC.RED + CC.BOLD + "Caught no soup!");
            }
        });
        thread.setUncaughtExceptionHandler((t, throwable) -> throwable.printStackTrace());
        thread.start();
    }

    private static void soupSingle(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.setHeldItemSlot(8);
        inventory.setItem(0, new ItemStack(Material.MUSHROOM_SOUP));
        inventory.setItem(1, new ItemStack(Material.DIAMOND_SWORD));
        player.updateInventory();

        Thread thread = new Thread(() -> {
            int millis = ThreadLocalRandom.current().nextInt(0, 50);
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            player.sendMessage("Start Packet manipulation, " + player.getName() + "! (" + CC.RED +  millis + CC.WHITE + ")");

            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
            PlayerConnection playerConnection = entityPlayer.playerConnection;

            PacketPlayInHeldItemSlot packetplayinhelditemslot = new PacketPlayInHeldItemSlot();
            new ObjectWrapper(packetplayinhelditemslot).setField("itemInHandIndex", 0);
            new ObjectWrapper(packetplayinhelditemslot).setField("nano", System.nanoTime());
            try {
                playerConnection.a(packetplayinhelditemslot);
            } catch (Throwable throwable) {
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }

            try {
                net.minecraft.server.v1_8_R3.ItemStack itemInHand = entityPlayer.inventory.getItemInHand();
                System.out.println(itemInHand);
                PacketPlayInBlockPlace packetplayinblockplace = new PacketPlayInBlockPlace(itemInHand);
                new ObjectWrapper(packetplayinblockplace).setField("timestamp", System.currentTimeMillis());
                new ObjectWrapper(packetplayinblockplace).setField("nano", System.nanoTime());
                playerConnection.a(packetplayinblockplace);
            } catch (CancelledPacketHandleException throwable) {
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }

            PacketPlayInBlockDig drop = new PacketPlayInBlockDig();
            new ObjectWrapper(drop).setField("c", PacketPlayInBlockDig.EnumPlayerDigType.DROP_ITEM);
            new ObjectWrapper(drop).setField("nano", System.nanoTime());

            try {
                playerConnection.a(drop);
            } catch (Throwable throwable) {
            }

            packetplayinhelditemslot = new PacketPlayInHeldItemSlot();
            new ObjectWrapper(packetplayinhelditemslot).setField("itemInHandIndex", 1);
            new ObjectWrapper(packetplayinhelditemslot).setField("nano", System.nanoTime());
            try {
                playerConnection.a(packetplayinhelditemslot);
            } catch (Throwable throwable) {
            }

            MCSchedulers.getGlobalScheduler().schedule(player::updateInventory, 1L);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            int i = DebugListener.count.get();
            if (i > 0) {
                DebugListener.count.set(0);
                MCSchedulers.getGlobalScheduler().schedule(() -> {
                    player.performCommand("test");
                }, 1L);
            } else {
                player.sendMessage(CC.RED + CC.BOLD + "Caught no soup!");
            }
//            player.sendMessage("Sent packets.");
        });
        thread.setUncaughtExceptionHandler((t, throwable) -> throwable.printStackTrace());
        thread.start();
    }

}
