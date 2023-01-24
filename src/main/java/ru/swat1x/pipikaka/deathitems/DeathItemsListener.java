package ru.swat1x.pipikaka.deathitems;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DeathItemsListener implements Listener {

  DeathItemsManager manager;

  @EventHandler
  public void onDeath(PlayerDeathEvent event) {
    if (!event.getKeepInventory()) {
      var inv = event.getPlayer().getInventory();
      dropItems(event.getPlayer(), event.getDrops(), event.getEntity().getLocation());
    }
  }

  private void dropItems(Player owner, List<ItemStack> items, Location location) {

  }

}
