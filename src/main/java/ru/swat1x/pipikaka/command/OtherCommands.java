package ru.swat1x.pipikaka.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Random;

public class OtherCommands extends BaseCommand {

  private static int skipNum = 0;
  private static final Random RANDOM = new Random();
  private static final String[] MESSAGES = new String[]{
          "Доброе утро страна!",
          "Слышь, работать",
          "Солнце в зените. Жаль не в цска"
  };


  @CommandAlias("skipnight|sn")
  public void onSkinNight(Player player) {
    World world = player.getWorld();
    if (player.hasPermission("skipnight.skip")) {
      if (world.getTime() < 13000L) {
        player.sendMessage("§cЕщё ночь не наступила");
      } else {
        world.setTime(1000L);
        String message = MESSAGES[RANDOM.nextInt(MESSAGES.length)];
        boolean adv = false;
        if (skipNum++ == 2) {
          adv = true;
          skipNum = 0;
        }
        Component bc;
        if (!adv) {
          bc = Component.text("§e" + message + " §7§o(" + player.getName() + " пропустил ночь)");
        } else {
          bc = Component.text(
                  "\n §e" + message + "\n\n §7§oИгрок " + player.getName() + " пропустил ночь" +
                          "\n И ты сможешь так же, если купишь ранг Plus\n всего за 70₽ на сайте *тык на сообщение*\n "
          );
        }
        bc = bc.hoverEvent(HoverEvent.showText(Component.text("§fНажмите чтобы перейти на\nсайт §bshop.Penis-Helicopter.ru")))
                .clickEvent(ClickEvent.openUrl("https://shop.Penis-Helicopter.ru")
                );
        Bukkit.broadcast(bc);
      }
    } else {
      player.sendMessage("§cКоманда доступна только от ранга Plus");
    }
  }

}
