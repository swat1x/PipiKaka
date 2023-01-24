package ru.swat1x.pipikaka.donategiver;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.Locales;
import co.aikar.locales.MessageKey;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import ru.swat1x.pipikaka.PipiKakaPlugin;
import ru.swat1x.pipikaka.common.Manager;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DonateGiveManager implements Manager {

  Map<String, Donate> donateMap = new HashMap<>();
  PipiKakaPlugin plugin;
  MessageKey wrongDonateId = MessageKey.of("wrongDonateId");

  public void handle(Donate donate, String to, String from) {
    boolean self = to.equals(from);
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), donate.getCommand().replace("{user}", to));
    Bukkit.broadcast((self ?
            Component.text("§7-----------\n" +
                    "§eИгрок §f" + to + "§e купил §e" + donate.getName() + "\n" +
                    "§eна нашем сайте. Спасибо ему большое!\n" +
                    "§eТы можешь сделать так же на §fshop.Penis-Helicopter.ru\n" +
                    "§7-----------") : Component.text("§7-----------\n" +
            "§f" + from + "§e подарил §e" + donate.getName() + "§e игроку §f" + to + "\n" +
            "§eна нашем сайте. Спасибо ему большое!\n" +
            "§eТы можешь сделать так же на §fshop.Penis-Helicopter.ru\n" +
            "§7-----------"))
            .clickEvent(ClickEvent.openUrl("https://shop.Penis-Helicopter.ru"))
            .hoverEvent(HoverEvent.showText(Component.text("§aНажмите чтобы перейти на сайт")))
    );
    Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.6f, 2));
    plugin.getDiscordBotManager().handleDonate(from, to, donate);
  }

  @Override
  public void initialize() {
    reload();
    var commandManager = plugin.getCommandManager();
    commandManager.getLocales().addMessage(Locales.RUSSIAN, wrongDonateId, "§cДонат с таким id не найден");
    commandManager.getCommandCompletions().registerAsyncCompletion("@donates", ctx -> donateMap.keySet());
    commandManager.getCommandContexts().registerContext(Donate.class, ctx -> {
      var name = ctx.popFirstArg();
      var donate = donateMap.get(name);
      if (donate == null) throw new InvalidCommandArgument(wrongDonateId);
      return donate;
    });
  }

  @Override
  public void reload() {
    reloadMap();
  }

  private void reloadMap() {
    plugin.reloadConfig();
    donateMap.clear();
    var donSec = plugin.getConfig().getConfigurationSection("donate");
    if (donSec != null) {
      for (String id : donSec.getKeys(false)) {
        var cs = plugin.getConfig().getConfigurationSection("donate." + id);
        if (cs != null) {
          var name = ChatColor.translateAlternateColorCodes('&', cs.getString("name", "&cИмя не установлено"));
          var command = cs.getString("command");
          donateMap.put(id, new Donate(id, name, command));
        }
      }
    }
  }
}
