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
				width - oxfordLogoImageLayer.width() - 10, height
						- oxfordLogoImageLayer.height() - 38);

		this.trainBox = trainBox;
		Canvas canvas = bgImage.canvas();
		canvas.setFillColor(0xffe9b96e);
		canvas.fillRect(0, 0, width, height);
		final Image backgroundImage = assets().getImage(
				"images/pngs/startPage.png");
		canvas.drawImage(backgroundImage, 0, 0);

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
		aboutBlurBackButtonImageLayer.addListener(new Pointer.Listener() {
			@Override
			public void onPointerStart(playn.core.Pointer.Event event) {
				aboutLayer.setVisible(false);
			}

			@Override
			public void onPointerEnd(playn.core.Pointer.Event event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPointerDrag(playn.core.Pointer.Event event) {
				// TODO Auto-generated method stub

			}
		});

		menuLayer = graphics().createGroupLayer();
		menuLayer.setTranslation(width / 5, height / 4 + 40);
		final Image menuBackgoundImage = assets().getImage(
				"images/pngs/menuBackground.png");
		final ImageLayer menuBackgoundImageLayer = graphics().createImageLayer(
				menuBackgoundImage);
		menuLayer.add(menuBackgoundImageLayer);

		final Image aboutButtonImage = assets().getImage(
				"images/pngs/aboutButton.png");
		final ImageLayer aboutButtonImageLayer = graphics().createImageLayer(
				aboutButtonImage);
		menuLayer.add(aboutButtonImageLayer);
		aboutButtonImageLayer.setTranslation(205, 240);
		aboutButtonImageLayer.addListener(new Pointer.Listener() {
			@Override
			public void onPointerStart(playn.core.Pointer.Event event) {
				aboutLayer.setVisible(true);
			}

			@Override
			public void onPointerEnd(playn.core.Pointer.Event event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPointerDrag(playn.core.Pointer.Event event) {
				// TODO Auto-generated method stub

			}
		});

		final Image demoButtonImage = assets().getImage(
				"images/pngs/demoButton.png");
		final ImageLayer demoButtonImageLayer = graphics().createImageLayer(
				demoButtonImage);
		menuLayer.add(demoButtonImageLayer);
		demoButtonImageLayer.setTranslation(110, 80);
		demoButtonImageLayer.addListener(new Pointer.Listener() {

//			Image demoButtonPressedImage = assets().getImage(
//					"images/pngs/demoButtonPressed.png");

			@Override
			public void onPointerStart(playn.core.Pointer.Event event) {
				// demoButtonImageLayer.setImage(demoButtonPressedImage);
				trainBox.setScene(trainBox.getDemoScene());
			}

			@Override
			public void onPointerEnd(playn.core.Pointer.Event event) {
				// demoButtonImageLayer.setImage(demoButtonImage);
			}

			@Override
			public void onPointerDrag(playn.core.Pointer.Event event) {
				// TODO Auto-generated method stub

			}

		});

		final Image playButtonImage = assets().getImage(
				"images/pngs/playButton.png");
		final ImageLayer playButtonImageLayer = graphics().createImageLayer(
				playButtonImage);
		menuLayer.add(playButtonImageLayer);
		playButtonImageLayer.setTranslation(280, 50);
		playButtonImageLayer.addListener(new Pointer.Listener() {

			Image playButtonPressedImage = assets().getImage(
					"images/pngs/playButtonPressed.png");

			@Override
			public void onPointerStart(playn.core.Pointer.Event event) {
				playButtonImageLayer.setImage(playButtonPressedImage);
				trainBox.setScene(trainBox.getLevelSelectScene());
			}

			@Override
			public void onPointerEnd(playn.core.Pointer.Event event) {
				playButtonImageLayer.setImage(playButtonPressedImage);
			}

			@Override
			public void onPointerDrag(playn.core.Pointer.Event event) {
			}

		});

		bgLayer = graphics().createImageLayer(bgImage);

	}

	@Override
	public void onAttach() {
		graphics().rootLayer().setScale(1, 1);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerEnd(playn.core.Pointer.Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerDrag(playn.core.Pointer.Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyDown(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyUp(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	// graphics().rootLayer().setScale(-1, 1);
	// graphics().rootLayer().setTranslation(graphics().width(), 0);

}
