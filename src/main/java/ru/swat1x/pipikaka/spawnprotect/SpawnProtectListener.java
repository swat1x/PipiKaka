package ru.swat1x.pipikaka.spawnprotect;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SpawnProtectListener implements Listener {

  SpawnProtectManager manager;

  @EventHandler(ignoreCancelled = true)
  public void onBlockBreak(PlayerInteractEvent event){
    if(!manager.canInteract(event.getPlayer())){
      event.setCancelled(true);
    }
  }

}
