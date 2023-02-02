package ru.swat1x.pipikaka.spawnprotect;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import ru.swat1x.pipikaka.PipiKakaPlugin;
import ru.swat1x.pipikaka.common.Manager;
import ru.swat1x.pipikaka.util.TextUtil;
import ru.swat1x.pipikaka.util.TimeUtil;

import java.time.Duration;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SpawnProtectManager implements Manager {

  Duration dur = Duration.ofHours(2);

  @Override
  public void initialize() {
//    var map = Bukkit.createMap(Bukkit.getWorld("world"));
  }


  public boolean canInteract(Player player) {
    if (player.hasPermission("pipi.notime") || TimeUtil.playEnough(player, dur)) {
      return true;
    }
    var loc = player.getLocation();
    var x = loc.getX();
    var z = loc.getZ();
    if (Math.abs(x) <= 1000 && Math.abs(z) <= 1000) {
      player.sendMessage("§cВы должны отыграть 2 часа (ещё " +
              (TextUtil.getTimeLabel(player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 50L, dur.toMillis())) +
              ") чтобы ломать в зоне 1000x1000\n§7§oЕсли не хотите ждать или бежать, можете купить обход на сайте");
      return false;
    } else return true;
  }

}
