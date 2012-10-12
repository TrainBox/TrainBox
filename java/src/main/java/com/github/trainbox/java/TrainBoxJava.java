package com.github.trainbox.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.github.trainbox.core.TrainBox;

public class TrainBoxJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("com/github/trainbox/resources");
    PlayN.run(new TrainBox());
  }
}
