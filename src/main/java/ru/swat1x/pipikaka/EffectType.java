package ru.swat1x.pipikaka;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum EffectType {

  PIPI(
          () -> new ItemStack(Material.YELLOW_CONCRETE, 64),
          "§eВы наступили в мочу %s",
          "§eВ вашу мочу наступил %s"
  ),
  KAKA(
          () -> new ItemStack(Material.BROWN_CONCRETE, 64),
          "§eВы наступили в говно %s",
          "§eВ ваше говно наступил %s"
  );

  Supplier<ItemStack> itemSupplier;
  String stepMessage;
  String stepToOwnerMessage;

}
