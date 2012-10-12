package com.github.trainbox.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.github.trainbox.core.TrainBox;

public class TrainBoxHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("trainbox/");
    PlayN.run(new TrainBox());
  }
}
