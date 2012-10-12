package com.github.trainbox.uimodel;

public interface LevelFinishedListener {
	public void levelCleared();
	public void levelFailed(String message);
	
	class Null implements LevelFinishedListener {
		@Override public void levelCleared() {}
		@Override public void levelFailed(String message) {}
	}
}
