package ru.swat1x.pipikaka.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.swat1x.pipikaka.PipiKakaPlugin;
import ru.swat1x.pipikaka.user.UserManager;
import ru.swat1x.pipikaka.util.TextUtil;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import static ru.swat1x.pipikaka.EffectType.KAKA;
import static ru.swat1x.pipikaka.EffectType.PIPI;

@CommandAlias("effects")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EffectsCommand extends BaseCommand {

  public static final Set<String> SPY = new HashSet<>();

  PipiKakaPlugin plugin;
  UserManager userManager;
  Cache<String, Long> pukCooldown = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(30)).build();

  private void playPuk(Player player) {
    player.sendMessage("§aЗвук пука все услышали");
    Bukkit.getOnlinePlayers().forEach(p -> {
      p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
      if (p != player) {
        p.sendMessage("§eИгрок " + player.getName() + " пукнул вам прям в уши через §a/puk\n§7§oВы можете так же если купите такую возможность на сайте");
      }
    });
  }

  @CommandAlias("puk")
  public void executePuk(Player player, @Optional String isNow) {
    if (!player.hasPermission("pipi.globalpuk")) {
      player.sendMessage("§cВы не можете так делать. Купите эту возможность на сайте");
      return;
    }
    boolean now = player.hasPermission("pipi.globalpuk.now") && isNow != null && isNow.equals("now");
    if (pukCooldown.asMap().containsKey(player.getName())) {
      long end = pukCooldown.asMap().get(player.getName());
      player.sendMessage("§aВы сможете пукать снова через " + TextUtil.getTimeLabel(System.currentTimeMillis(), end));
      return;
    }
    if(now){
      playPuk(player);
    } else {
      player.sendMessage("§aВы активировали глобальный пук. Через 15 секунд он проиграется");
      Bukkit.getScheduler().runTaskLater(plugin, () -> playPuk(player), 20 * 15);
    }
    if (!player.hasPermission("puk.globalpuk.nocooldown")) {
      long end = System.currentTimeMillis() + Duration.ofMinutes(30).toMillis();
      pukCooldown.put(player.getName(), end);
    }
  }

  @CommandAlias("pipi")
  public void executePipi(Player player) {
    userManager.handleEffectExecute(player, PIPI);
  }

  @CommandAlias("kaka")
  public void executeKaka(Player player) {
    userManager.handleEffectExecute(player, KAKA);
  }

  @CommandAlias("pipispy")
  @CommandPermission("pipi.spy")
  public void executePipiSpy(Player player) {
    boolean yes = SPY.add(player.getName());
    if (!yes) {
      player.sendMessage("Вы отключили пипи спай");
      SPY.remove(player.getName());
    } else {
      player.sendMessage("Вы включили пипи спай");
    }
  }

}
