package com.github.trainbox.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.github.trainbox.core.TrainBox;

public class TrainBoxActivity extends GameActivity {

  @Override
  public void main(){
    platform().assets().setPathPrefix("com/github/trainbox/resources");
    PlayN.run(new TrainBox());
  }
}
