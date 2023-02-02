package ru.swat1x.pipikaka.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Optional;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class BedCommand extends BaseCommand {

  @CommandAlias("bed|кровать|home")
  public void onBed(Player player, @Optional String target) {
    OfflinePlayer targetPlayer = target != null && player.hasPermission("pipi.bed.others") ? Bukkit.getOfflinePlayer(target) : player;
    if(targetPlayer == null){
      player.sendMessage("§c");
      return;
    }
    var bed = player.getBedSpawnLocation();
    if (bed == null) {
      player.sendMessage("§cУ Вас нет активной кровати!");
    } else {
      player.teleport(bed);
      player.sendMessage("§aВы телепортированы на кровать!");
    }
  }

}
