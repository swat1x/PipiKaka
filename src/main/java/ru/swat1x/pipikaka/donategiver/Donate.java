package ru.swat1x.pipikaka.donategiver;

import lombok.Value;

@Value
public class Donate {

  String id;
  String name;
  String command; // {user}

}
