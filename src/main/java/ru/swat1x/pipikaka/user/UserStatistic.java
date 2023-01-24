package ru.swat1x.pipikaka.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.swat1x.pipikaka.EffectType;

import java.util.Map;
import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserStatistic {

  String owner;
  Map<EffectType, EffectSession> sessions;

}
