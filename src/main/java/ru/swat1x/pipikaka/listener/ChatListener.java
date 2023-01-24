package ru.swat1x.pipikaka.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatListener implements Listener {

  Plugin plugin;

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    var player = e.getPlayer();
    if (!player.hasPlayedBefore()) {
      player.sendMessage("§eВнимание!§f Пока вы не наиграете 2 часа, вы не сможете ломать постройки в радиусе 1000 блоков от спавна");
      player.sendMessage("""
              §eКоманды:
              §f /pipi - пописать
              §f /kaka - покакать
              §f /sn - пропустить ночь §7(от Plus)
              §f /bed - вернуться на кровать
              §f /puk - пукнуть на весь сервер §7(от Plus)
              """);
    }
  }

  @EventHandler
  public void onPhantom(EntitySpawnEvent e) {
    if (e.getEntity().getType() == EntityType.PHANTOM) {
      e.getEntity().remove();
      e.setCancelled(true);
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onChat(AsyncPlayerChatEvent e) {
    String message = e.getMessage().replace("%", "%%");
    var player = e.getPlayer();
    e.setCancelled(true);
    boolean local = !message.startsWith("!");
    if (!local) {
      message = message.substring(1);
    }
    String color;
    if (player.isOp()) {
      color = "§c";
    } else if (player.hasPermission("plus.perm")) {
      color = "§x§0§0§f§f§c§7";
    } else {
      color = "§e";
    }
    String format = color + e.getPlayer().getName() + "§7: " + (local ? "§f" : "§e") + message;
    if (!local) {
      Bukkit.broadcastMessage(format);
    } else {
      Bukkit.getConsoleSender().sendMessage(format);
      Bukkit.getScheduler().runTask(plugin, () -> {
        player.getLocation().getWorld()
                .getNearbyEntities(player.getLocation(), 100, 100, 100)
                .stream()
                .filter(ent -> ent.getType() == EntityType.PLAYER)
                .map(ent -> (Player) ent)
                .forEach(pl -> pl.sendMessage(format));

      });
    }
//    if (message)
//      e.setFormat((player.isOp() ? "§c" : "§a") + e.getPlayer().getName() + "§7: §f" + message);
  }

}
