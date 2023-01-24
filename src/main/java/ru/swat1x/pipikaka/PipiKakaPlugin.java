package ru.swat1x.pipikaka;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.Locales;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.swat1x.pipikaka.automessage.AutoMessageManager;
import ru.swat1x.pipikaka.command.*;
import ru.swat1x.pipikaka.common.Manager;
import ru.swat1x.pipikaka.discord.DiscordBotManager;
import ru.swat1x.pipikaka.donategiver.DonateGiveManager;
import ru.swat1x.pipikaka.listener.ChatListener;
import ru.swat1x.pipikaka.listener.DeathMessageListener;
import ru.swat1x.pipikaka.listener.StepListener;
import ru.swat1x.pipikaka.settings.SettingsManager;
import ru.swat1x.pipikaka.spawnprotect.SpawnProtectListener;
import ru.swat1x.pipikaka.spawnprotect.SpawnProtectManager;
import ru.swat1x.pipikaka.user.UserManager;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class PipiKakaPlugin extends JavaPlugin {

  BukkitCommandManager commandManager;

  SettingsManager settingsManager;
  UserManager userManager;
  Manager autoMessageManager;
  DonateGiveManager donateGiveManager;
  SpawnProtectManager spawnProtectManager;
  DiscordBotManager discordBotManager;

  @Override
  public void onEnable() {
    saveDefaultConfig();

    settingsManager = new SettingsManager(this);
    settingsManager.initialize();

    userManager = new UserManager(this);
    userManager.initialize();

    autoMessageManager = new AutoMessageManager(this);
    autoMessageManager.initialize();

    commandManager = new BukkitCommandManager(this);
    commandManager.getLocales().setDefaultLocale(Locales.RUSSIAN);

    donateGiveManager = new DonateGiveManager(this);
    donateGiveManager.initialize();

    spawnProtectManager = new SpawnProtectManager();
    spawnProtectManager.initialize();

    discordBotManager = new DiscordBotManager(this);
    discordBotManager.initialize();
    discordBotManager.makeEnableActions();

    // Commands
    commandManager.registerCommand(new EffectsCommand(this, userManager));
    commandManager.registerCommand(new BedCommand());
    commandManager.registerCommand(new AdminCommands(this));
    commandManager.registerCommand(new OtherCommands());
    commandManager.registerCommand(new DonateCommand());

    // Listeners
    PluginManager pm = Bukkit.getPluginManager();
    pm.registerEvents(new ChatListener(this), this);
    pm.registerEvents(new StepListener(userManager), this);
    pm.registerEvents(new DeathMessageListener(this), this);
    pm.registerEvents(new SpawnProtectListener(spawnProtectManager), this);
  }

  @Override
  public void onDisable() {
    commandManager.unregisterCommands();
    discordBotManager.makeDisableActions();
  }
}
