package ru.swat1x.pipikaka.deathitems;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import ru.swat1x.pipikaka.common.Manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class DeathItemsManager implements Manager {

  Map<UUID, DeathSession> itemMap = new HashMap<>();

  @Override
  public void initialize() {

  }

  public void allow(Player executor, UUID uuid, String user) {

  }

}
