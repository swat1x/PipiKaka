package ru.swat1x.pipikaka.deathitems;

import lombok.Value;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Value
public class DeathSession {

  UUID id;
  String owner;
  List<String> allowed = new LinkedList<>();
  List<String> warned = new LinkedList<>();

}
