package ru.swat1x.pipikaka.util;

import lombok.experimental.UtilityClass;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.time.Duration;

@UtilityClass
public class TimeUtil {

  public boolean playEnough(OfflinePlayer player, Duration duration) {
    long millis = player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 50L;
    return millis > duration.toMillis();
  }

}
