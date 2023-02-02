package ru.swat1x.pipikaka.discord;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R2.block.CraftBlock;
import org.jetbrains.annotations.NotNull;
import ru.swat1x.pipikaka.PipiKakaPlugin;
import ru.swat1x.pipikaka.common.Manager;
import ru.swat1x.pipikaka.donategiver.Donate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class DiscordBotManager extends ListenerAdapter implements Manager {

  final PipiKakaPlugin plugin;

  final Duration updateDelta = Duration.ofMinutes(15);

  JDA botJda;
  World world;
  Location pos1;
  Location pos2;

  int mult = 4;

  long lastUpdate = System.currentTimeMillis();
  BufferedImage actualImage;

  public Color getColorOfBlock(Block block) {
    return new Color(((CraftBlock) block).getNMS().b().t().ak);
  }

  public BufferedImage rengerArt() {
    lastUpdate = System.currentTimeMillis();
    int minx, miny, minz, maxx, maxy, maxz;

    minx = Math.min(pos1.getBlockX(), pos2.getBlockX());
    miny = Math.min(pos1.getBlockY(), pos2.getBlockY());
    minz = Math.min(pos1.getBlockZ(), pos2.getBlockZ());

    maxx = Math.max(pos1.getBlockX(), pos2.getBlockX());
//    maxy = Math.max(pos1.getBlockY(), pos2.getBlockY());
    maxz = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

    int imageScale = 128 * mult;

    BufferedImage image = new BufferedImage(imageScale, imageScale, BufferedImage.TYPE_INT_RGB);
    Graphics g = image.getGraphics();
    int mapX = 0;
    int mapY = 0;
    g.setColor(new Color(124, 118, 118)); // Bg color
    g.fillRect(0, 0, imageScale, imageScale);
    for (int x = minx; x <= maxx; x++) {
      for (int z = minz; z <= maxz; z++) {
        Block block = world.getBlockAt(x, miny, z);
        var color = getColorOfBlock(block);
//        if (color.getRed() != 0 && color.getGreen() != 0 && color.getBlue() != 0) {
        g.setColor(getColorOfBlock(block));
        g.fillRect(mapX, mapY, mult, mult);
//        }
        mapY += mult;
      }
      mapY = 0;
      mapX += mult;
    }

    return image;
  }

  @SneakyThrows
  private InputStream inputStreamFromImage(BufferedImage image) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ImageIO.write(image, "png", os);
    return new ByteArrayInputStream(os.toByteArray());
  }

  private InputStream getImageOrUpdate() {
    if (actualImage == null || System.currentTimeMillis() - lastUpdate >= updateDelta.toMillis()) {
      actualImage = rengerArt();
    }
    return inputStreamFromImage(actualImage);
  }

  @Override
  public void initialize() {
    botJda = JDABuilder.createDefault(plugin.getSettingsManager().getDsToken()).addEventListeners(this).build();

    updateCommands();

    world = Bukkit.getWorld(plugin.getConfig().getString("art.world", "world"));
    pos1 = loadPoint(1);
    pos2 = loadPoint(2);
  }

  private Location loadPoint(int num) {
    var path = "art.pos" + num + ".";
    var x = plugin.getConfig().getDouble(path + "x", 0);
    var y = plugin.getConfig().getDouble(path + "y", 0);
    var z = plugin.getConfig().getDouble(path + "z", 0);
    return new Location(world, x, y, z);
  }

  public void handleDonate(String from, String to, Donate donate) {
    var self = from.equals(to);
    var channel = botJda.getTextChannelById(1067237064246034542L);
    if (channel != null) {
      channel.sendMessageFormat("Игрок %s купил %s услугу %s\n\n<@&1067237929258336366>", from, self ? "себе" : "для " + to, donate.getName()).queue();
    }
  }

  private void updateCommands() {
    botJda.updateCommands()
            .addCommands(
                    Commands.slash("list", "Онлайн игроки"),
                    Commands.slash("art", "Текущее состояние арта"),
                    Commands.slash("execute", "Выполнить команду на сервере")
                            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                            .addOption(OptionType.STRING, "команда", "Команда без слеша"))
            .queue();
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
      case "art":
        event.reply("Вот текущее состояние арта:")
                .addFiles(FileUpload
                        .fromData(
                                getImageOrUpdate(),
                                "Art-" + System.currentTimeMillis() + ".png"))
                .queue();
        break;
      case "execute":
        var command = event.getInteraction().getOption("команда").getAsString();
        executeCommand(event.getUser(), command);
        event.reply("Команда **/" + command + "** отправлена")
//                .setEphemeral(false)
                .queue();
        break;
      case "list":
        var message = Bukkit.getOnlinePlayers().stream().map(player -> {
          if (player.hasPermission("group.plus")) {
            return "**" + player.getName() + "**";
          } else return player.getName();
        }).collect(Collectors.joining(", "));
        message = "Игроки онлайн (" + Bukkit.getOnlinePlayers().size() + "): " + message;
        event.reply(message).queue();
        break;
    }
  }

  public void makeEnableActions() {

  }

  public void makeDisableActions() {
    botJda.getCallbackPool().shutdown();
  }

}
