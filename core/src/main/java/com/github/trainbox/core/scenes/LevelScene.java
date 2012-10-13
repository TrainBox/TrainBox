package com.github.trainbox.core.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.log;

import java.util.Arrays;

import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.TypedEvent;
import playn.core.Layer;
import playn.core.Pointer;
import playn.core.Pointer.Event;
import playn.core.TextFormat;
import playn.core.TextFormat.Alignment;
import pythagoras.f.Point;

import com.github.trainbox.core.TrainBox;
import com.github.trainbox.core.model.Level;
import com.github.trainbox.core.uimodel.ComponentHelper;
import com.github.trainbox.core.uimodel.LevelFinishedListener;
import com.github.trainbox.core.uimodel.ToolManager;
import com.github.trainbox.core.uimodel.UIComponentFactory;
import com.github.trainbox.core.uimodel.UILevel;
import com.github.trainbox.core.uimodel.UIComponentFactory.UIToken;

/**
 * This should contain everything we need for a level: - The track - Components
 * to add - The play button
 */
public class LevelScene implements Scene, Pointer.Listener, Keyboard.Listener {
	private static final float SPEED_INCREASE = 0.005f;
	private static final int MENU_HEIGHT = 200;
	private final static int HEIGHT = graphics().height();
	private final static int WIDTH = graphics().width();

	TrainBox trainBox;

	private Layer mBgLayer;
	private Layer mPlayButton;
	private Layer mResetButton;
	private UILevel mLevel;
	private UIPallet mPallet;
	private ToolManager toolMan;
	int currPauseGoButtonImageIndex = 0;

//	GroupLayer levelStatusLayer;
	GroupLayer levelControlLayer;
	ImageLayer pauseButtonImageLayer;
	GroupLayer levelPopupLayer;
	ImageLayer titleLayer;

	private float autoScrollSpeed = 0.01f;
	private float scrollTargetX = 0;
	private float scrollTargetY = 0;
	private boolean autoScroll = true;
	private boolean mEditable = true; // called k to avoid editing directly

	public LevelScene(TrainBox trainBox, Level level) {
		this.trainBox = trainBox;
		mLevel = new UILevel(level);

		// A background image. This should be really nice.
		CanvasImage bgImage = ComponentHelper.drawMoonCraters(WIDTH, HEIGHT,
				level.title.hashCode());
		mBgLayer = graphics().createImageLayer(bgImage);

		// initialise the level controller buttons
		initToolsAndDragging();
		initLevelController();
		initLevelStatus();
		initLevelText();
		initLevelPopup();
	}

	private void initLevelText() {
		CanvasImage textImage = graphics().createImage(graphics().width(), 400);
		Font font = graphics().createFont("Tahoma", Font.Style.BOLD, 35);
		if (font == null) font = graphics().createFont("Droid Sans", Font.Style.BOLD, 35);

		TextFormat format = new TextFormat().withFont(font);

		textImage.canvas().setStrokeColor(0xff8f5902).setFillColor(0xffca7829);
		textImage.canvas().fillText(
				graphics().layoutText(mLevel.getLevel().title, format), 0f, 0f);

//		textImage.canvas().strokeText(
//				graphics().layoutText(mLevel.getLevel().title, format), 0f, 0f);
		titleLayer = graphics().createImageLayer(textImage);
		titleLayer.setTranslation(50, 35);
	}

	@Override
	public void update(float delta) {
		mLevel.update(delta);
		updateScroll(delta);
	}

	public void resetLevel() {
		setPaused(true);
		setEditable(true);
		mLevel.reset();
		autoScroll = true;
	}

	@Override
	public void onAttach() {
		graphics().rootLayer().add(mBgLayer);
		graphics().rootLayer().add(titleLayer);
		graphics().rootLayer().add(mLevel.layer());
		graphics().rootLayer().add(levelControlLayer);
		graphics().rootLayer().add(pauseButtonImageLayer);
		graphics().rootLayer().add(levelPopupLayer);
		keyboard().setListener(this);
	}

	@Override
	public void onDetach() {
		// This helps us avoid a memory leak
		graphics().rootLayer().remove(mBgLayer);
		graphics().rootLayer().remove(titleLayer);
		graphics().rootLayer().remove(mLevel.layer());
		graphics().rootLayer().remove(levelControlLayer);
		graphics().rootLayer().remove(pauseButtonImageLayer);
		graphics().rootLayer().remove(levelPopupLayer);
		keyboard().setListener(null);
	}

	private void setPaused(boolean paused) {
		mLevel.paused(paused);
		if (!paused) {
			autoScroll = true;
			setEditable(false);
			currPauseGoButtonImageIndex = 1;
		} else {
			currPauseGoButtonImageIndex = 0;
		}
		pauseButtonImageLayer.setVisible(!paused);
	}

	private boolean isPaused() {
		return mLevel.paused();
	}

	// /////////////////////////////////////////////////////////////////////////
	// UI Initialization
	// /////////////////////////////////////////////////////////////////////////

	private void initLevelPopup() {
		levelPopupLayer = graphics().createGroupLayer();
		levelPopupLayer.setVisible(false);
		levelPopupLayer.setTranslation(WIDTH / 5, HEIGHT / 5);

		// set background
		final Image backgroundImage = assets().getImage(
				"images/pngs/menuBackground.png");
		ImageLayer bgImageLayer = graphics().createImageLayer(backgroundImage);
		levelPopupLayer.add(bgImageLayer);

		// button to enter a new level
		Image changeLevelButtonImage = assets().getImage(
				"images/pngs/changeLevelButton.png");
		ImageLayer changeLevelButtonImageLayer = graphics().createImageLayer(
				changeLevelButtonImage);
		levelPopupLayer.add(changeLevelButtonImageLayer);
		changeLevelButtonImageLayer.setTranslation(350, 100);

		changeLevelButtonImageLayer.addListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(Event event) {
				setPaused(!mLevel.paused());
				trainBox.setScene(trainBox.getLevelSelectScene());
			}
		});

		Image levelPopulBackButtonImage = assets().getImage(
				"images/pngs/backButton.png");
		ImageLayer levelPopulBackButtonImageLayer = graphics()
				.createImageLayer(levelPopulBackButtonImage);
		levelPopupLayer.add(levelPopulBackButtonImageLayer);
		levelPopulBackButtonImageLayer.setTranslation(370, 230);
		levelPopulBackButtonImageLayer.addListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(Event event) {
				levelPopupLayer.setVisible(false);
			}
		});

		Image levelPopulHomeButtonImage = assets().getImage(
				"images/pngs/homeButton.png");
		ImageLayer levelPopulHomeButtonImageLayer = graphics()
				.createImageLayer(levelPopulHomeButtonImage);
		levelPopupLayer.add(levelPopulHomeButtonImageLayer);
		levelPopulHomeButtonImageLayer.setTranslation(150, 250);
		levelPopulHomeButtonImageLayer.addListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(Event event) {
				trainBox.setScene(trainBox.getStartScene());
			}
		});

		final Image demoButtonImage = assets().getImage(
				"images/pngs/demoButton.png");
		final ImageLayer demoButtonImageLayer = graphics().createImageLayer(
				demoButtonImage);
		levelPopupLayer.add(demoButtonImageLayer);
		demoButtonImageLayer.setTranslation(120, 60);
		demoButtonImageLayer.addListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(Event event) {
				trainBox.setScene(trainBox.getDemoScene());
			}
		});
	}

	private void initLevelController() {
		Image resetButtonImage = assets().getImage(
				"images/pngs/resetButton.png");
		mResetButton = graphics().createImageLayer(resetButtonImage);

		mResetButton.addListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(Event event) {
				resetLevel();
				;

				// trainBox(mLevel.getLevel().levelNumber);
			}
		});

		mResetButton.setTranslation(graphics().width() - 390, graphics()
				.height() - 125);

		Image goButtonImage = assets().getImage("images/pngs/goButton.png");
		mPlayButton = graphics().createImageLayer(goButtonImage);

		levelControlLayer = graphics().createGroupLayer();

		// Connect the play button to the track
		mPlayButton.addListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(Event event) {
				setPaused(!mLevel.paused());
			}
		});

		mPlayButton.setTranslation(graphics().width() - 130, graphics()
				.height() - 125);

		Image pauseButtonImage = assets().getImage(
				"images/pngs/pauseButton.png");
		pauseButtonImageLayer = graphics().createImageLayer(pauseButtonImage);
		pauseButtonImageLayer.setTranslation(graphics().width() - 130,
				graphics().height() - 125);
		pauseButtonImageLayer.setVisible(false);

		Image menuButtonImage = assets().getImage("images/pngs/menuButton.png");
		ImageLayer menuButtonImageImageLayer = graphics().createImageLayer(
				menuButtonImage);
		menuButtonImageImageLayer.setTranslation(graphics().width() - 260,
				graphics().height() - 125);
		menuButtonImageImageLayer.addListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(Event event) {
				setPaused(true);
				levelPopupLayer.setVisible(true);
			}
		});

		// add the component pallet.
		mPallet = new UIPallet();

		UIComponentButton dupBut = new UIComponentButton(toolMan, UIToken.DUP);
		UIComponentButton boxBut = new UIComponentButton(toolMan, UIToken.BOX);
		UIComponentButton flipBut = new UIComponentButton(toolMan, UIToken.FLIP);
		UIComponentButton catBut = new UIComponentButton(toolMan, UIToken.CAT);
		UIComponentButton mergBut = new UIComponentButton(toolMan, UIToken.MERG);
		UIComponentButton delBut = new UIComponentButton(toolMan, UIToken.DEL);

		for (UIComponentButton but : Arrays.asList(dupBut, boxBut, flipBut,
				catBut, mergBut, delBut)) {
			mPallet.add(but);
		}
		// We want to hide unused components for the first levels, as it makes
		// the
		// game easier to play. However it also makes it easier to win, so for
		// the
		// harder levels we always show everything.
		if (mLevel.getLevel().levelNumber < 12) {
			dupBut.enabled(mLevel.getLevel().dupsBest > 0);
			boxBut.enabled(mLevel.getLevel().chainsBest > 0);
			flipBut.enabled(mLevel.getLevel().flipsBest > 0);
			catBut.enabled(mLevel.getLevel().unchainsBest > 0);
			mergBut.enabled(mLevel.getLevel().splitsBest > 0);
			delBut.enabled(mLevel.getLevel().levelNumber != 0);
		}

		mPallet.getLayer().setTranslation(20, graphics().height() - 100 - 30);

		levelControlLayer.add(mPallet.getLayer());
		levelControlLayer.add(mPlayButton);
		levelControlLayer.add(menuButtonImageImageLayer);
		levelControlLayer.add(mResetButton);
	}

	private void initLevelStatus() {

//		// initialise the next button image layer
//		Image nextButtonImage = assets().getImage("images/pngs/nextButton.png");
//		final Layer nextButtonLeveLStatusImageLayer = graphics()
//				.createImageLayer(nextButtonImage);
////		levelStatusLayer.add(nextButtonLeveLStatusImageLayer);
//		nextButtonLeveLStatusImageLayer.setTranslation(680, 520);
//		nextButtonLeveLStatusImageLayer.addListener(new Pointer.Adapter() {
//			@Override
//			public void onPointerStart(Event event) {
//				levelFailedBlurbImageLayer.setVisible(false);
//				levelCompletedBlurbImageLayer.setVisible(false);
//				levelStatusLayer.setVisible(false);
//				levelStatusText.setVisible(false);
//				trainBox.setLevel(mLevel.getLevel().levelNumber + 1);
//			}
//		});

		// initialise the retry button image layer
		// TODO create an image called retryButton and replace the text below
		// VVVV
//		Image retryButtonImage = assets().getImage(
//				"images/pngs/retryButton.png");
//		final Layer retryButtonLeveLStatusImageLayer = graphics()
//				.createImageLayer(retryButtonImage);
//		retryButtonLeveLStatusImageLayer.setScale(0.95f, 0.95f); // to fix the
//																	// fact that
//																	// the retry
//																	// button is
//																	// slightly
//																	// larger
//																	// than the
//																	// next
//																	// one...
//		levelStatusLayer.add(retryButtonLeveLStatusImageLayer);
//		retryButtonLeveLStatusImageLayer.setTranslation(550, 520);
//		retryButtonLeveLStatusImageLayer.addListener(new Pointer.Adapter() {
//			@Override
//			public void onPointerStart(Event event) {
//				levelFailedBlurbImageLayer.setVisible(false);
//				levelCompletedBlurbImageLayer.setVisible(false);
//				levelStatusText.setVisible(false);
//				levelStatusLayer.setVisible(false);
//				resetLevel();
//				// trainBox.setLevel(mLevel.getLevel().levelNumber);
//			}
//		});

		mLevel.setListener(new LevelFinishedListener() {
			@Override
			public void levelComplete() {
				
				int used = mLevel.countUserComponents();
				int needed = mLevel.getLevel().dupsBest
						+ mLevel.getLevel().chainsBest
						+ mLevel.getLevel().flipsBest
						+ mLevel.getLevel().unchainsBest
						+ mLevel.getLevel().splitsBest;
				String comment = used == needed ? "You found the perfect solution!"
						: "Your solution used " + (used - needed)
								+ " more\ncomponents than needed";
				
				UIModalMessage levelCompleteDialogue = new UIModalMessage(graphics().rootLayer(), "Level Complete!", comment);
				
				levelCompleteDialogue.setVisible(true);
				log().debug("Level Complete!");

//				levelFailedBlurbImageLayer.setVisible(false);
//				levelCompletedBlurbImageLayer.setVisible(true);
//				nextButtonLeveLStatusImageLayer.setVisible(true);
//				retryButtonLeveLStatusImageLayer.setVisible(true);
//				levelStatusText.setVisible(true);
//
//				levelStatusText.setImage(createCommentImage(comment));
//				levelStatusText.setTranslation(100, 425);
				trainBox.levelComplete();
			}

			@Override
			public void levelFailed(String message) {
//				levelStatusLayer.setVisible(true);
//				levelCompletedBlurbImageLayer.setVisible(false);
//				levelFailedBlurbImageLayer.setVisible(true);
//				nextButtonLeveLStatusImageLayer.setVisible(false);
//				retryButtonLeveLStatusImageLayer.setVisible(true);
//				levelStatusText.setVisible(true);
//
//				levelStatusText.setImage(createCommentImage(message));
//				levelStatusText.setTranslation(50, 500);
			}
		});
	}

	private Image createCommentImage(String comment) {
		CanvasImage textImage = graphics().createImage(
				graphics().screenWidth(), 400);
		Font font = graphics().createFont("Sans", Font.Style.BOLD, 30);
		TextFormat format = new TextFormat().withFont(font).withAlignment(
				Alignment.CENTER);
		textImage.canvas().setStrokeColor(0xff000000).setFillColor(0xfffdd99b);
		textImage.canvas()
				.fillText(graphics().layoutText(comment, format), 0, 0);
//				.strokeText(graphics().layoutText(comment, format), 0, 0);
		return textImage;
	}

	private void initToolsAndDragging() {
		toolMan = new ToolManager();
		// If the click has propagated to the root layer, we have unchecked our
		// tool
		mBgLayer.addListener(new Pointer.Adapter() {
			@Override
			public void onPointerStart(Event event) {
				log().debug("Fell through");
				toolMan.unselect();
			}
		});
		// A listener on the level
		mLevel.layer().addListener((Pointer.Listener) this);
		setLevelTranslation(graphics().width() / 2.f - mLevel.getSize().width
				/ 2.f,
				(graphics().height() - MENU_HEIGHT) / 2.f
						- mLevel.getSize().height / 2.f, true);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Input/Output
	// /////////////////////////////////////////////////////////////////////////

	private boolean mIsDragging = false;
	private float mDragStartXPos;
	private float mDragStartYPos;

	@Override
	public void onPointerStart(Event event) {
		boolean didInsertSomething = false;
		boolean didDeleteSomething = false;

		if (isEditable() && toolMan.isSelected()) {

			UIToken tool = toolMan.getCurrentTool();
			Point p = new Point(event.localX(), event.localY());

			if (tool == UIToken.DEL) {
				didDeleteSomething = mLevel.deleteChildAt(p);
			} else {
				didInsertSomething = mLevel
						.insertChildAt(UIComponentFactory.fromTok(toolMan
								.getCurrentTool()), p);
			}

		}

		// if (toolMan.isSelected() && isEditable()) {
		// Point p = new Point(event.localX(), event.localY());
		// didInsertSomething = mLevel.insertChildAt(
		// UIComponentFactory.fromTok(toolMan.getCurrentTool()), p);
		// } else {
		// if (isEditable() && deleting) {
		// Point p = new Point(event.localX(), event.localY());
		// didDeleteSomething = mLevel.deleteChildAt(p);
		// }
		// }
		if (didDeleteSomething) {
			log().debug("DELETED A COMPONENT");
		}
		if (didInsertSomething) {

			mLevel.startMaxExpectedTrainPropogation();

			mIsDragging = false;
			toolMan.unselect();
			float oldx = event.x() - event.localX();
			float oldy = event.y() - event.localY();
			float newx = graphics().width() / 2 - event.localX();
			float newy = graphics().height() / 2 - event.localY();
			if (Math.abs(newx - oldx) > graphics().width() * 0.382f
					|| Math.abs(newy - oldy) > graphics().height() * 0.382f)
				setLevelTranslation(newx, newy, false);
			else
				setLevelTranslation(oldx, oldy, false);
		} else {
			toolMan.unselect();
			mIsDragging = true;
			mDragStartXPos = event.localX();
			mDragStartYPos = event.localY();
		}
	}

	@Override
	public void onPointerDrag(Event event) {
		if (mIsDragging) {
			autoScroll = false;
			float x = event.x() - mDragStartXPos;
			float y = event.y() - mDragStartYPos;
			setLevelTranslation(x, y, true);
		}
	}

	@Override
	public void onPointerEnd(Event event) {
		mIsDragging = false;
	}

	private void setLevelTranslation(float x, float y, boolean force) {

		if (force)
			mLevel.layer().setTranslation(x, y);

		float y0 = MENU_HEIGHT * 0.618f;
		float y1 = graphics().height() - MENU_HEIGHT;
		float x0 = 0;
		float x1 = graphics().width();

		if (mLevel.getSize().width < x1 - x0) {
			x = Math.max(x, x0);
			x = Math.min(x, x1 - mLevel.getSize().width);
		} else {
			x = Math.max(x, x1 - mLevel.getSize().width);
			x = Math.min(x, x0);
		}
		if (mLevel.getSize().height < y1 - y0) {
			y = y0 + (y1 - y0) / 2 - mLevel.getSize().height / 2;
		} else {
			y = Math.max(y, y1 - mLevel.getSize().height);
			y = Math.min(y, y0);
		}

		scrollTargetX = x;
		scrollTargetY = y;
	}

	private void updateScroll(float delta) {
		if (mIsDragging)
			return;

		if (!isPaused() && autoScroll) {
			Point p = mLevel.getFarthestTrainPosition();
			float x = graphics().width() * 0.618f - p.x;
			setLevelTranslation(x, p.y, false);
		}

		float tx = mLevel.layer().transform().tx();
		float ty = mLevel.layer().transform().ty();
		float distx = scrollTargetX - tx;
		float disty = scrollTargetY - ty;

		float x = tx + autoScrollSpeed * delta * distx;
		float y = ty + autoScrollSpeed * delta * disty;
		if (Math.abs(x - tx) + 1 >= Math.abs(distx))
			x = scrollTargetX;
		if (Math.abs(y - ty) + 1 >= Math.abs(disty))
			y = scrollTargetY;

		mLevel.layer().setTranslation(x, y);
	}

	@Override
	public void onKeyDown(playn.core.Keyboard.Event event) {
		if (event.key() == Key.UP) {
			mLevel.increaseTrainSpeed(SPEED_INCREASE);
			log().debug("INCREASING SPEED");
		}
		if (event.key() == Key.DOWN) {
			mLevel.decreaseTrainSpeed(SPEED_INCREASE);
			log().debug("DECREASING SPEED");

		}
		if (event.key() == Key.ENTER || event.key() == Key.SPACE
				|| event.key() == Key.P) {
			setPaused(!isPaused());
		}

		if (event.key() == Key.ESCAPE) {
			toolMan.unselect();
		}
		if (event.key() == Key.CONTROL) {
			toolMan.overrideTool(UIToken.DEL);
		}
		if (event.key() == Key.ESCAPE) {// toggle pallet visibility
			mPallet.setVisible(!mPallet.isVisible());
		}
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
	}

	@Override
	public void onKeyUp(playn.core.Keyboard.Event event) {
		if (event.key() == Key.CONTROL) {
			toolMan.stopOverriding();
		}
	}

	private void setEditable(boolean b) {
		mEditable = b;
		if (!b) {
			mPallet.setAlpha(0.1f);
		} else {
			mPallet.setAlpha(1);
		}
	}

	private boolean isEditable() {
		return mEditable;
	}
}