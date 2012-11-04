package com.github.trainbox.core.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.Pointer;

import com.github.trainbox.core.TrainBox;

public class StartScene implements Scene, Keyboard.Listener, Pointer.Listener {

	boolean flipped = false; // we miss you, watermelon

	Image oxfordLogoImage = assets().getImage(
			"images/CompSci_logo_portraitR_RGB.jpg");
	ImageLayer oxfordLogoImageLayer = graphics().createImageLayer(
			oxfordLogoImage);

	int width = graphics().width();
	int height = graphics().height();
	CanvasImage bgImage = graphics().createImage(graphics().width(),
			graphics().height());
	ImageLayer bgLayer;
	GroupLayer menuLayer;
	ImageLayer startButton; // contained in menulayer
	ImageLayer exitButton; // contained in menulayer
	GroupLayer aboutLayer;
	TrainBox trainBox;

	public StartScene(final TrainBox trainBox) {
		oxfordLogoImageLayer.setTranslation(
				width - oxfordLogoImageLayer.width() - 20, height
						- oxfordLogoImageLayer.height() - 20);

		this.trainBox = trainBox;
		Canvas canvas = bgImage.canvas();
		canvas.setFillColor(0xffe9b96e);
		canvas.fillRect(0, 0, width, height);
		final Image backgroundImage = assets().getImage(
				"images/pngs/startPage.png");
		canvas.drawImage(backgroundImage, (width-backgroundImage.width())/2, 0);

		aboutLayer = graphics().createGroupLayer();
		aboutLayer.setTranslation(width / 20 + 40, height / 20);
		final Image aboutBlurbImage = assets().getImage(
				"images/pngs/aboutPage.png");
		final ImageLayer aboutBlurbImageLayer = graphics().createImageLayer(
				aboutBlurbImage);
		aboutLayer.add(aboutBlurbImageLayer);
		aboutLayer.setVisible(false);

		final Image aboutBlurBackButtonImage = assets().getImage(
				"images/pngs/backButton.png");
		final ImageLayer aboutBlurBackButtonImageLayer = graphics()
				.createImageLayer(aboutBlurBackButtonImage);
		aboutLayer.add(aboutBlurBackButtonImageLayer);
		aboutBlurBackButtonImageLayer.setTranslation(680, 520);
		aboutBlurBackButtonImageLayer.addListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(playn.core.Pointer.Event event) {
				aboutLayer.setVisible(false);
			}
		});

		//
		//	IT'S MENUING TIME.
		//
		
		// Menu goes in the middle.
		menuLayer = graphics().createGroupLayer();
		menuLayer.setTranslation(width / 2, (height + backgroundImage.height())  / 2);
		
		// Wibbly black thing.
		final Image menuBackgoundImage = assets().getImage(
				"images/pngs/menuBackground.png");
		final ImageLayer menuBackgoundImageLayer = graphics().createImageLayer(
				menuBackgoundImage);
		menuBackgoundImageLayer.setTranslation(-menuBackgoundImage.width()/2, -menuBackgoundImage.height()/2);
		menuLayer.add(menuBackgoundImageLayer);

		// About button.
		final Image aboutButtonImage = assets().getImage(
				"images/pngs/aboutButton.png");
		final ImageLayer aboutButtonImageLayer = graphics().createImageLayer(
				aboutButtonImage);
		menuLayer.add(aboutButtonImageLayer);
		aboutButtonImageLayer.setTranslation(-95, 22);
		aboutButtonImageLayer.addListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(playn.core.Pointer.Event event) {
				aboutLayer.setVisible(true);
			}
		});

		
		// Guide
		final Image demoButtonImage = assets().getImage(
				"images/pngs/demoButton.png");
		final ImageLayer demoButtonImageLayer = graphics().createImageLayer(
				demoButtonImage);
		menuLayer.add(demoButtonImageLayer);
		demoButtonImageLayer.setTranslation(-193, -137);
		demoButtonImageLayer.addListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(playn.core.Pointer.Event event) {
				trainBox.clearScene();
				trainBox.setScene(trainBox.getDemoScene());
			}
		});

		
		// LETS PLAY!
		final Image playButtonImage = assets().getImage(
				"images/pngs/playButton.png");
		final ImageLayer playButtonImageLayer = graphics().createImageLayer(
				playButtonImage);
		menuLayer.add(playButtonImageLayer);
		playButtonImageLayer.setTranslation(-23, -167);
		playButtonImageLayer.addListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(playn.core.Pointer.Event event) {
				trainBox.setScene(trainBox.getLevelSelectScene());
			}
		});

		bgLayer = graphics().createImageLayer(bgImage);
	}

	@Override
	public void onAttach() {
		graphics().rootLayer().add(bgLayer);
		graphics().rootLayer().add(menuLayer);
		graphics().rootLayer().add(aboutLayer);
		graphics().rootLayer().add(oxfordLogoImageLayer);
		pointer().setListener(this);
		keyboard().setListener(this);
	}

	@Override
	public void onDetach() {
		graphics().rootLayer().remove(bgLayer);
		graphics().rootLayer().remove(menuLayer);
		graphics().rootLayer().remove(aboutLayer);
		graphics().rootLayer().remove(oxfordLogoImageLayer);
		pointer().setListener(null);
		keyboard().setListener(null);
	}

	@Override
	public void onPointerStart(playn.core.Pointer.Event event) {
	}

	@Override
	public void onPointerEnd(playn.core.Pointer.Event event) {
	}

	@Override
	public void onPointerDrag(playn.core.Pointer.Event event) {
	}

	@Override
	public void onKeyDown(Event event) {
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
	}

	@Override
	public void onKeyUp(Event event) {
	}

	@Override
	public void update(float delta) {
	}
}
