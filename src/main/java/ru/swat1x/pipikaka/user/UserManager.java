package ru.swat1x.pipikaka.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import ru.swat1x.pipikaka.EffectType;
import ru.swat1x.pipikaka.PipiKakaPlugin;
import ru.swat1x.pipikaka.command.EffectsCommand;
import ru.swat1x.pipikaka.common.Manager;
import ru.swat1x.pipikaka.task.FooTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserManager implements Manager {

  PipiKakaPlugin plugin;

  Map<EffectType, Cache<String, Long>> cachesMap = new HashMap<>();
  Map<String, UserStatistic> statisticMap = new HashMap<>();
  Map<Item, EffectSession> sessionsMap = new HashMap<>();

  public boolean hasCooldown(Player player, EffectType type) {
    return cachesMap.get(type).asMap().containsKey(player.getName());
  }

  public void putCooldown(Player player, EffectType type) {
    cachesMap.get(type).put(player.getName(), System.currentTimeMillis());
  }

  public void initialize() {
    for (EffectType type : EffectType.values()) {
      cachesMap.put(type, CacheBuilder.newBuilder()
              .expireAfterWrite(Duration.ofSeconds(15))
              .build());
    }
  }

  public void warnByItem(Item item) {
    EffectSession session = sessionsMap.get(item);
    if (item != null && !session.isWarned()) {
      session.setWarned(true);
      sendMessage(session.getOwner(), "§cХватит ссать в воронки! Довели!!");
    }
  }

  public void handleEffectExecute(Player player, EffectType type) {
    if (hasCooldown(player, type)) {
      player.sendMessage("§cПодожди немного...");
    } else {
      UserStatistic statistic = getStatistic(player.getName());
      EffectSession session = new EffectSession(
              player.getName(),
              false,
              type,
              new HashSet<>(),
              new HashSet<>());
      statistic.getSessions().put(type, session);
      new FooTask(
              this,
              type,
              session,
              player
      ).runTaskTimer(plugin, 0, 5);
      putCooldown(player, type);
    }
  }

  public void sendMessage(String username, String message) {
    Player player = Bukkit.getPlayerExact(username);
    if (player != null) {
      player.sendMessage(message);
    }
  }

  public UserStatistic getStatistic(String owner) {
    UserStatistic statistic = statisticMap.containsKey(owner) ? statisticMap.get(owner) :
            new UserStatistic(owner, new HashMap<>());
    statisticMap.put(owner, statistic);
    return statistic;
  }

  public boolean isEffectItem(Item item) {
    return sessionsMap.containsKey(item);
  }

  public boolean handleStep(Player player, Item item) {
    if (sessionsMap.containsKey(item)) {
      EffectSession session = sessionsMap.get(item);
      if (session.getOwner().equals(player.getName())) return true;
      boolean contains = !session.getSteppedPlayers().add(player.getName());
      if (!contains) {
        player.sendMessage(String.format(session.getType().getStepMessage(), session.getOwner()));
        sendMessage(session.getOwner(), String.format(session.getType().getStepToOwnerMessage(), player.getName()));
        EffectsCommand.SPY.forEach(name -> {
          if (!name.equals(session.getOwner()) && !name.equals(player.getName())) {
            sendMessage(name, "Игрок " + player.getName() + " наступил в " + session.getType().name() + " игрока " + session.getOwner());
          }
        });
      }
      handlePlayerStep(session.getOwner(), player);
      return true;
    } else return false;
  }

  public void handlePlayerStep(String owner, Player stepper) {
    UserStatistic statistic = statisticMap.containsKey(owner) ? statisticMap.get(owner) :
            new UserStatistic(owner, new HashMap<>());
    statisticMap.put(owner, statistic);
//    statistic.
  }

}
