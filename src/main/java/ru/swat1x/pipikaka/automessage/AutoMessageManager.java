package ru.swat1x.pipikaka.automessage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import ru.swat1x.pipikaka.common.Manager;

import java.util.HashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2(topic = "AutoMessages")
public class AutoMessageManager implements Manager {

  Plugin plugin;

  @NonFinal
  long delay;
  Map<Integer, AutoMessage> messages = new HashMap<>();

  @Override
  public void reload() {
    var config = plugin.getConfig();
    delay = config.getLong("messages-delay");
    var messagesPart = config.getConfigurationSection("messages");
    int i = 0;
    for (String key : messagesPart.getKeys(false)) {
      String path = key + ".";
      var message = String.join("\n", messagesPart.getStringList(path + "message"));
      message = ChatColor.translateAlternateColorCodes('&', message);
      String link = messagesPart.getString(path + "link");
      String hover = messagesPart.getString(path + "hover");
      messages.put(i++, new AutoMessage(
              message,
              hover,
              link
      ));
    }
  }

  private void sendMessage(AutoMessage message) {
    Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message.getText());
    if (message.getLink() != null) {
      component = component.clickEvent(ClickEvent.openUrl(message.getLink()));
    }
    if (message.getHover() != null) {
      component = component.hoverEvent(HoverEvent.showText(LegacyComponentSerializer.legacyAmpersand().deserialize(message.getText())));
    }
    Bukkit.broadcast(component);
  }

  public void initialize() {
    reload();
    runTask();
  }

  @NonFinal
  private int lastMessage = 0;

  @NonFinal
  private BukkitTask messageTask = null;

  private void runTask() {
    messageTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
      if (messages.size() <= lastMessage) {
        lastMessage = 0;
      }
      var message = messages.get(lastMessage);
      if (message == null) {
        log.error("Messages by index {} is null", lastMessage);
      } else {
        sendMessage(message);
      }
      lastMessage++;
    }, 20, delay);
  }

}
