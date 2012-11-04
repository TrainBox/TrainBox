package com.github.trainbox.core.gui;

import static playn.core.PlayN.graphics;

import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.TextFormat;
import playn.core.TextFormat.Alignment;
import playn.core.TextLayout;

public class UIModalDialogue extends UIModalPopup {
	
	public UIModalDialogue(GroupLayer root, String title, String message, UIButton... buttons) {
		
		super(root);
		addText(title, message);
		addButtons(buttons);
		
	}

	//TODO: Work out what buttons are. Add them to the modal dialogue;
	private void addButtons(UIButton[] buttons) {
		
		
				
	}

	private void addText(String title, String message) {

		CanvasImage textImage = graphics().createImage(
				graphics().screenWidth(), 400);
		Font font = graphics().createFont("Sans", Font.Style.BOLD, 30);
		TextFormat format = new TextFormat().withFont(font).withAlignment(
				Alignment.CENTER);
		textImage.canvas().setStrokeColor(0xff000000).setFillColor(0xffffffff);
		TextLayout layoutText = graphics().layoutText(title, format);
		textImage.canvas()
				.fillText(layoutText, (WIDTH - layoutText.width())/2, (HEIGHT - layoutText.height())/2);
//				.strokeText(layoutText, 0, 0);

		ImageLayer textLayer = graphics().createImageLayer();
		textLayer.setImage(textImage);
		
		modal.add(textLayer);
	}

}
