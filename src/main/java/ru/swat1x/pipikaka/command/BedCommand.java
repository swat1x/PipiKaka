package ru.swat1x.pipikaka.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import org.bukkit.entity.Player;

public class BedCommand extends BaseCommand {

  @CommandAlias("bed|кровать|home")
  public void onBed(Player player) {
    var bed = player.getBedSpawnLocation();
    if (bed == null) {
      player.sendMessage("§cУ Вас нет активной кровати!");
    } else {
      player.teleport(bed);
      player.sendMessage("§aВы телепортированы на кровать!");
    }
  }

}
