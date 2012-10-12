package com.github.trainbox.core.scenes;

import playn.core.Pointer;
import playn.core.Pointer.Event;

public abstract class Button implements Pointer.Listener {
	
	private boolean mEnabled = true;
	
	public Button() {
		
	}

	@Override
	public void onPointerDrag(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerEnd(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerStart(Event event) {
		// TODO Auto-generated method stub
		
	}

	public boolean isEnabled() {
		return mEnabled;
	}

	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
	}
	
	public void onButtonPressed() {
		
	}

}
