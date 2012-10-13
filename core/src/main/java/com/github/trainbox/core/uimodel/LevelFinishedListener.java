package com.github.trainbox.core.uimodel;

public interface LevelFinishedListener {
	public void levelComplete();
	public void levelFailed(String message);
	
	class Null implements LevelFinishedListener {
		@Override public void levelComplete() {}
		@Override public void levelFailed(String message) {}
	}
}
