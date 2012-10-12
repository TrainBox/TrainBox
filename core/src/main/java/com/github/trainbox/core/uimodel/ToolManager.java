package com.github.trainbox.core.uimodel;

import java.util.ArrayList;
import java.util.List;

import com.github.trainbox.core.scenes.ToolListener;
import com.github.trainbox.core.uimodel.UIComponentFactory.UIToken;

public class ToolManager {

	/** When overrideTool != null, it is used as the current tool. */
	UIToken overrideTool;
	UIToken currentTool;
	List<ToolListener> list;

	public ToolManager() {
		list = new ArrayList<ToolListener>();
	}

	public void add(ToolListener listener) {
		list.add(listener);
	}

	public void overrideTool(UIToken tool) {
		overrideTool = tool;
		notifySelect();
	}

	public void setTool(UIToken tool) {
		currentTool = tool;
		notifySelect();
	}

	public void stopOverriding() {
		overrideTool = null;
		notifySelect();
	}

	public void unselect() {
		currentTool = null;
		notifyUnselect();
	}

	public void notifySelect() {
		for (ToolListener lis : list) {
			lis.toolSelected(getCurrentTool());
		}
	}

	public void notifyUnselect() {
		for (ToolListener lis : list) {
			lis.toolsUnselected();
		}
	}

	public UIToken getCurrentTool() {
		if (overrideTool != null)
			return overrideTool;
		return currentTool;
	}

	public boolean isSelected() {
		return getCurrentTool() != null;
	}

}
