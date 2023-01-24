package ru.swat1x.pipikaka.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DeathMessageListener implements Listener {

  Plugin plugin;

  @EventHandler
  public void onBreak(BlockBreakEvent event) {
    if (event.getBlock().hasMetadata("death-sign")) {
      event.setDropItems(false);
      var player = event.getPlayer();
      player.sendTitle("§cБу!", "Зачем надругался над надгробьем!?", 0, 40, 15);
      player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1, 1);
    }
  }

  @EventHandler
  public void onDeath(PlayerDeathEvent event) {
    var player = event.getEntity();
    Location loc = player.getLocation().add(0, -1, 0);
    for (int i = 0; i < 5; i++) {
      loc = loc.add(0, 1, 0);
      var block = loc.getBlock();
      if (block.getType() == Material.AIR) {
        setSign(loc, player);
        player.sendMessage("§aНа месте вашей смерти осталась табличка");
        break;
      }
    }
  }

  private void setSign(Location location, Player player) {
    var block = location.getBlock();
    block.setType(Material.OAK_SIGN);
    Sign sign = (Sign) location.getBlock().getState();
    sign.setEditable(false);
    sign.setGlowingText(true);
    sign.setColor(DyeColor.WHITE);
    sign.setLine(1, "Тут погиб");
    sign.setLine(2, player.getName());
    sign.update(true);
    block.setMetadata("death-sign", new FixedMetadataValue(plugin, ""));
  }

}
