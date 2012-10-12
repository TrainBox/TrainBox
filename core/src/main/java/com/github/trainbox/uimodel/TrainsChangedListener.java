package com.github.trainbox.uimodel;

public interface TrainsChangedListener {
	public void onTrainCreated(UITrain train);
	public void onTrainDestroyed(UITrain train);
}
