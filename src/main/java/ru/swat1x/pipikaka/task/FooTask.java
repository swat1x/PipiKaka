package ru.swat1x.pipikaka.task;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.swat1x.pipikaka.EffectType;
import ru.swat1x.pipikaka.user.EffectSession;
import ru.swat1x.pipikaka.user.UserManager;

import java.util.HashSet;
import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FooTask extends BukkitRunnable {

  UserManager userManager;
  EffectType type;
  EffectSession session;
  Player player;

  Set<Item> itemSet = new HashSet<>();

  @NonFinal
  int amount = 20;

  @NonFinal
  int stayTime = 30;

  @Override
  public void run() {
    if (amount-- <= 0) {
      if (stayTime-- <= 0) {
        itemSet.forEach(item -> {
          userManager.getSessionsMap().remove(item);
          item.remove();
        });
        cancel();
      }
    } else {
      var loc = player.getLocation();
      var vec = player.getEyeLocation().getDirection().setY(0.25).multiply(0.20);
      if (type == EffectType.KAKA) {
        loc.add(0, -1, 0);
        vec.multiply(-1);
      }
      // Проверка на пипи кака
      var item = loc.getWorld().dropItem(loc.add(0, 1.25, 0), type.getItemSupplier().get());
      item.setVelocity(vec);
      if (player.hasPermission("pipi.glow")) {
        item.setGlowing(true);
      }
      userManager.getSessionsMap().put(item, session);
      itemSet.add(item);
//      item.
      // Спавн
    }
  }
}
