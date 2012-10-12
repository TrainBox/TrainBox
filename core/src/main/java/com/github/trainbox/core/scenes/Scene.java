package com.github.trainbox.core.scenes;

/**
 * Like in a play, we need to go between different scenes.
 */
public interface Scene {
	public void update(float delta);
	public void onAttach();
	public void onDetach();
}
