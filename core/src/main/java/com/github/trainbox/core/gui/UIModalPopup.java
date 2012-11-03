package com.github.trainbox.core.gui;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;

public class UIModalPopup {

	protected static float WIDTH = graphics().width()*0.75f;
	protected static float HEIGHT = graphics().height() *0.75f;
	
	
	boolean mVisible = false;
	GroupLayer mRootLayer = graphics().rootLayer();

	GroupLayer modal;
	
	public UIModalPopup(Layer layer) {
		log().debug("Modal created");

		modal = graphics().createGroupLayer();
		
		modal.setTranslation(
				(graphics().width() - WIDTH)/2,
				(graphics().height() - HEIGHT)/2);
		
		modal.add(layer);
		
		ImageLayer backgroundLayer = graphics().createImageLayer();
		
		CanvasImage background = graphics().createImage(WIDTH, HEIGHT);
		background.canvas().setFillColor(0x80000000);
		background.canvas().fillRoundRect(0, 0, WIDTH, HEIGHT, 10);
		backgroundLayer.setImage(background);
		
		modal.add(backgroundLayer);
	}

	public void setVisible(boolean show){
		log().debug("Set visible to " + show);
		if (show && !mVisible){
			show();
		} else if (!show && mVisible) {
			hide();
		}
	}
	
	private void show(){
		log().debug("Show");
		mVisible = true;
		mRootLayer.add(modal);
		modal.setVisible(true);
	}
	
	private void hide(){
		log().debug("Hide");
		mVisible = false;
		mRootLayer.remove(modal);
	}

	public void addLayer(Layer nextButtonLeveLStatusImageLayer) {
		modal.add(nextButtonLeveLStatusImageLayer);
	}
}
