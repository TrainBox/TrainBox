package com.github.trainbox.core.uimodel;

import pythagoras.f.Dimension;

public interface SizeChangedListener {
	public void onSizeChanged(UIComponent source, Dimension oldSize);
}
