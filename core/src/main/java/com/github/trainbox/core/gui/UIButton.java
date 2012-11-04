package com.github.trainbox.core.gui;

import playn.core.Image;
import playn.core.Pointer;
import playn.core.Pointer.Event;

public abstract class UIButton implements Pointer.Listener {
	
	public Image buttonImage;
	public Image buttonPressedImage;
	public Image buttonDisabledImage;
	
	public boolean enabled = true;

	@Override
	public void onPointerStart(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerEnd(Event event) {
		// TODO Auto-generated method stub
		
	}
	
	public abstract void clicked();

	@Override
	public void onPointerDrag(Event event) {}

	
	
}
