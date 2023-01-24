package ru.swat1x.pipikaka.settings;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.swat1x.pipikaka.PipiKakaPlugin;
import ru.swat1x.pipikaka.common.Manager;

import java.io.File;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class SettingsManager implements Manager {

  private static final String SPAWN_PROTECT_KEY = "spawn-protect";
  private static final String SPAWN_SIZE_KEY = "spawn-size";

  final PipiKakaPlugin plugin;

  FileConfiguration dataFile;

  private File getFile() {
    return new File("settings.yml");
  }

  @SneakyThrows
  private void save() {
    dataFile.save(getFile());
  }

  @Override
  public void initialize() {
    dataFile = YamlConfiguration.loadConfiguration(getFile());
  }

  public boolean isSpawnProtectEnable() {
    return dataFile.getBoolean(SPAWN_PROTECT_KEY, false);
  }

  public String getDsToken() {
    return plugin.getConfig().getString("ds-token");
  }

  public int getSpawnSize() {
    return dataFile.getInt(SPAWN_SIZE_KEY, 100);
  }

  public void setSpawnProtect(boolean state) {
    dataFile.set(SPAWN_PROTECT_KEY, state);
  }


}
