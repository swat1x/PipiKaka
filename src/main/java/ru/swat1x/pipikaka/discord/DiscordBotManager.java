package ru.swat1x.pipikaka.discord;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ru.swat1x.pipikaka.PipiKakaPlugin;
import ru.swat1x.pipikaka.common.Manager;
import ru.swat1x.pipikaka.donategiver.Donate;

import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DiscordBotManager extends ListenerAdapter implements Manager {

  PipiKakaPlugin plugin;

  @NonFinal
  JDA botJda;

  @Override
  public void initialize() {
    botJda = JDABuilder.createDefault(plugin.getSettingsManager().getDsToken())
            .addEventListeners(this)
            .build();

    updateCommands();
  }

  public void handleDonate(String from, String to, Donate donate) {
    var self = from.equals(to);
    var channel = botJda.getTextChannelById(1067237064246034542L);
    if (channel != null) {
      channel.sendMessageFormat("Игрок %s купил %s услугу %s\n\n<@&1067237929258336366>",
              from, self ? "себе" : "для " + to, donate.getName()).queue();
    }
  }

  private void updateCommands() {
    botJda.updateCommands().addCommands(
            Commands.slash("list", "Онлайн игроки"),
            Commands.slash("execute", "Выполнить команду на сервере")
                    .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                    .addOption(OptionType.STRING, "команда", "Команда без слеша")
    ).queue();
  }

  private void executeCommand(User executor, String command) {
    Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    var message = "§eПользователь Discord §f" + executor.getAsTag() + "§e выполнил команду §f/" + command;
    Bukkit.getConsoleSender().sendMessage(message);
    Bukkit.getOnlinePlayers().forEach(player -> {
      if (player.hasPermission("pipi.discord.notify")) {
        player.sendMessage(message);
      }
    });
  }

  @Override
  public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
    switch (event.getName()) {
      case "execute":
        var command = event.getInteraction().getOption("команда").getAsString();
        executeCommand(event.getUser(), command);
        event.reply("Команда **/" + command + "** отправлена")
//                .setEphemeral(false)
                .queue();
      case "list":
        var message = Bukkit.getOnlinePlayers().stream()
                .map(player -> {
                  if (player.hasPermission("group.plus")) {
                    return "**" + player.getName() + "**";
                  } else return player.getName();
                }).collect(Collectors.joining(", "));
        message = "Игроки онлайн (" + Bukkit.getOnlinePlayers().size() + "): " + message;
        event.reply(message).queue();
    }
  }

  public void makeEnableActions() {

  }

  public void makeDisableActions() {

  }

}
