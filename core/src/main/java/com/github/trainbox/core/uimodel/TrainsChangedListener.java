package com.github.trainbox.core.uimodel;

public interface TrainsChangedListener {
	public void onTrainCreated(UITrain train);
	public void onTrainDestroyed(UITrain train);
}
