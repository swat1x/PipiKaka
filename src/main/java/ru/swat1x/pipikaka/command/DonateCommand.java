package ru.swat1x.pipikaka.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;

@CommandAlias("donate")
public class DonateCommand extends BaseCommand {

  @Default
  public void execute(Player player) {
    player.sendMessage(Component.text("§eДонат сайт §7(нажмите)")
            .clickEvent(ClickEvent.openUrl("https://shop.Penis-Helicopter.ru")));
  }

}
