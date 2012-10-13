package com.github.trainbox.core.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;

public class UIModalMessage {

	Boolean mVisible;
	GroupLayer mRootLayer;
	private String mTitle;
	private String mMessage;
	private UIButton[] mButton;
	GroupLayer dialogue;
	
	public UIModalMessage(GroupLayer rootLayer, String title, String message, UIButton... button) {
		mRootLayer = rootLayer;
		mTitle = title;
		mMessage = message;
		mButton = button;
		
		dialogue = graphics().createGroupLayer();
		dialogue.setTranslation(graphics().height() / 20 + 40, graphics().width() / 20);
		
		Image levelCompletedBlurb = assets().getImage(
				"images/pngs/levelCompleteBlurb.png");
		
		ImageLayer blurbLayer = graphics().createImageLayer(levelCompletedBlurb);
		
		dialogue.add(blurbLayer);

		
	}

	public void setVisible(Boolean show){
		if (show && !mVisible){
			show();
		} else if (!show && mVisible) {
			hide();
		}
	}
	
	private void show(){
		mVisible = true;
		mRootLayer.add(dialogue);
		dialogue.setVisible(true);
	}
	
	private void hide(){
		mVisible = false;
		mRootLayer.remove(dialogue);
	}
}
