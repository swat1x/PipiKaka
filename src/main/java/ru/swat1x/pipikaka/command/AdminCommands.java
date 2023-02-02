package ru.swat1x.pipikaka.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import ru.swat1x.pipikaka.PipiKakaPlugin;
import ru.swat1x.pipikaka.donategiver.Donate;
import ru.swat1x.pipikaka.util.TextUtil;

@CommandAlias("admin")
@CommandPermission("pipi.admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AdminCommands extends BaseCommand {

  PipiKakaPlugin plugin;

  @Subcommand("giveDonate")
  @CommandCompletion("@donates @players")
  public void giveDonate(CommandSender sender, Donate donate, @Split(" ") String[] target) {
    sender.sendMessage(String.join(" ", target));
    sender.sendMessage("§eНачинаю процесс выдачи " + donate.getName() + "§e игроку §a" + target[0]);
    plugin.getDonateGiveManager().handle(donate, target[0], target.length > 1 ? target[1] : target[0]);
  }

  @Subcommand("reloadDonate")
  public void reloadDonate(CommandSender sender) {
    plugin.getDonateGiveManager().reload();
    sender.sendMessage("§aВы перезагрузили донаты");
  }

  @Subcommand("reloadmessages")
  public void reloadMessages(CommandSender sender) {
    plugin.getAutoMessageManager().reload();
    sender.sendMessage("§aУспешно!");
  }

  @Subcommand("howLongPlayed")
  @CommandCompletion("@players")
  public void setSpawnProtect(CommandSender sender, String target) {
    OfflinePlayer player = Bukkit.getOfflinePlayer(target);
    if (!player.hasPlayedBefore() && !player.isOnline()) {
      sender.sendMessage("§cИгрок не заходил");
    } else {
      int time = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
      time *= 50;
      sender.sendMessage("Игрок наиграл " + TextUtil.getTimeLabel(0, time));
    }
  }

  @Subcommand("setSpawnProtect")
  public void setSpawnProtect(CommandSender sender, boolean state) {
    plugin.getSettingsManager().setSpawnProtect(state);
    sender.sendMessage("§aЗащита спавна установлена §e" + state);
  }

  @Subcommand("enableSpawnProtect")
  public void enableSpawnProtect(CommandSender sender) {
    plugin.getSettingsManager().setSpawnProtect(true);
    sender.sendMessage("§eВы включили защиту спавна");
  }

  @Subcommand("disableSpawnProtect")
  public void disableSpawnProtect(CommandSender sender) {
    plugin.getSettingsManager().setSpawnProtect(false);
    sender.sendMessage("§eВы отключили защиту спавна");
  }

  @Subcommand("setSpawnProtectSize")
  public void setSpawnProtect(CommandSender sender, long size) {
    plugin.getAutoMessageManager().reload();
    sender.sendMessage("§aРазмер спавна установлен на §e" + size);
  }

}
