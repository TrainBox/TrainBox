package com.github.trainbox.core.gui;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Layer;

public class UIModalPopup {

	protected static float WIDTH = graphics().width()*0.75f;
	protected static float HEIGHT = graphics().height() *0.75f;
	
	GroupLayer mRootLayer;

	GroupLayer modal;
	
	public UIModalPopup(GroupLayer root) {
		
		mRootLayer = root;
		
		log().debug("Modal created");

		modal = graphics().createGroupLayer();
		
		modal.setTranslation(
				(graphics().width() - WIDTH)/2,
				(graphics().height() - HEIGHT)/2);
				
		ImageLayer backgroundLayer = graphics().createImageLayer();
		
		CanvasImage background = graphics().createImage(WIDTH, HEIGHT);
		background.canvas().setFillColor(0x80000000);
		background.canvas().fillRoundRect(0, 0, WIDTH, HEIGHT, 10);
		backgroundLayer.setImage(background);
		
		modal.add(backgroundLayer);
		
		mRootLayer.add(modal);
	}

	public void setVisible(boolean show){
		modal.setVisible(show);
	}

	/**
	 * Adds a layer to the popup.
	 * @param layer
	 */
	public void addLayer(Layer layer) {
		modal.add(layer);
	}
	
	/**
	 * Removes a laZ
	 * @param layer
	 */
	public void removeLayey(Layer layer) {
		modal.remove(layer);
	}

	/**
	 * Will destroy all the layers held in the modal popup. Methods of this
	 * class may throw null pointer exceptions after this is called.
	 */
	public void destroy() {
		mRootLayer.remove(modal);
		modal = null;
	}
}
