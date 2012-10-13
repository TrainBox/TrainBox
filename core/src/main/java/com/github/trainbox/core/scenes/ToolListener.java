package com.github.trainbox.core.scenes;

import com.github.trainbox.core.uimodel.UIComponentFactory.UIToken;

public interface ToolListener {
	
	public void toolSelected(UIToken currentTool);
	/***
	 * Called when no tool is selected.
	 */
	public void toolsUnselected();

}