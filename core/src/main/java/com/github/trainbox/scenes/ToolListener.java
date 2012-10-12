package com.github.trainbox.scenes;

import com.github.trainbox.uimodel.UIComponentFactory.UIToken;

public interface ToolListener {
	
	public void toolSelected(UIToken currentTool);
	/***
	 * Called when no tool is selected.
	 */
	public void toolsUnselected();

}
