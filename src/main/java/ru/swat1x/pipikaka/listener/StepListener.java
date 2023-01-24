package ru.swat1x.pipikaka.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import ru.swat1x.pipikaka.user.UserManager;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StepListener implements Listener {

  UserManager userManager;

  @EventHandler
  public void onStep(InventoryPickupItemEvent event) {
    if (event.getInventory().getType() == InventoryType.HOPPER) {
      var item = event.getItem();
      if (userManager.isEffectItem(item)) {
        userManager.warnByItem(item);
        event.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onStep(EntityPickupItemEvent event) {
    if (event.getEntity() instanceof Player player) {
      var item = event.getItem();
      var state = userManager.handleStep(player, item);
      if (state) {
        event.setCancelled(true);
      }
    }
  }

}
