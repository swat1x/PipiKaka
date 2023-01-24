package ru.swat1x.pipikaka.deathitems;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("deathitems|death|di")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DeathItemsCommand extends BaseCommand {

  DeathItemsManager manager;

  @Default
  public void executeDefault(Player player) {
    player.sendMessage("§cАбоба?");
  }

  @Subcommand("allow")
  @CommandCompletion("<айди> @players")
  public void onAllow(Player player, String idString, @Single String playerName) {
    try {
      UUID id = UUID.fromString(idString);
      manager.allow(player, id, playerName);
    } catch (Exception e) {
      player.sendMessage("§cЧто-то не так");
    }
  }

  @Subcommand("list")
  @CommandPermission("pipi.di.list")
  public void onList(Player player) {

  }

}
