package ru.swat1x.pipikaka.user;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.bukkit.entity.Item;
import ru.swat1x.pipikaka.EffectType;

import java.util.Set;

@Value
public class EffectSession {

  String owner;
  @NonFinal
  @Setter
  boolean warned = false;
  EffectType type;
  Set<String> steppedPlayers;
  Set<Item> itemSet;

}
