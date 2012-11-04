package com.github.trainbox.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.AssetWatcher;
import playn.core.Game;
import playn.core.Platform.Type;
import playn.core.PlayN;

import com.github.trainbox.core.model.Level;
import com.github.trainbox.core.scenes.DemoScene;
import com.github.trainbox.core.scenes.LevelScene;
import com.github.trainbox.core.scenes.LevelSelectScene;
import com.github.trainbox.core.scenes.LoadingScene;
import com.github.trainbox.core.scenes.NullScene;
import com.github.trainbox.core.scenes.Scene;
import com.github.trainbox.core.scenes.StartScene;
import com.github.trainbox.core.util.LevelTracker;

public class TrainBox implements Game{

	AssetWatcher watcher;
	
	public final static int WIDTH = 1280;
	public final static int HEIGHT = 720;
	Scene mScene = new NullScene();
	int currentLevel =0;
	
	@Override
	public void init() {
		
		setScene(new LoadingScene(this));
		if (PlayN.platformType() == Type.ANDROID){
			graphics().setSize(graphics().screenWidth(), graphics().screenHeight());
			// Keep the same aspect ratio.
			float sx = graphics().screenWidth() / (float) WIDTH;
			float sy = graphics().screenHeight() / (float) HEIGHT;

			// Fit to the available screen without stretching.
			graphics().rootLayer().setScale(Math.min(sx, sy));
		} else {
			graphics().setSize(1024, 640); // this changes the size of the main window
		}
		
		
		
		addResources();

		watcher.start();
	}

	private void addResources(){
		//Insert resources here.
		
		watcher = new AssetWatcher(new AssetWatcher.Listener() {
			public void done() {
				setScene(getStartScene());
			}

			public void error(Throwable e) {
			}
		});
		
		watcher.add(assets().getImage("images/pngs/aboutButton.png"));
		watcher.add(assets().getImage("images/pngs/aboutButtonPressed.png"));
		watcher.add(assets().getImage("images/pngs/aboutPage.png"));
		watcher.add(assets().getImage("images/pngs/backButton.png"));
		watcher.add(assets().getImage("images/pngs/boxComponentButtonImage.png"));
		watcher.add(assets().getImage("images/pngs/boxComponentButtonImagePressed.png"));
		watcher.add(assets().getImage("images/pngs/boxComponent.png"));
		watcher.add(assets().getImage("images/pngs/changeLevelButton.png"));
		watcher.add(assets().getImage("images/pngs/chooseLevelBlurb.png"));
		watcher.add(assets().getImage("images/pngs/clickNDropBar.png"));
		watcher.add(assets().getImage("images/pngs/concatComponentButtonImage.png"));
		watcher.add(assets().getImage("images/pngs/concatComponentButtonImagePressed.png"));
		watcher.add(assets().getImage("images/pngs/concatComponent.png"));
		watcher.add(assets().getImage("images/pngs/deleteButton.png"));
		watcher.add(assets().getImage("images/pngs/demoButton.png"));
		watcher.add(assets().getImage("images/pngs/demoButtonPressed.png"));
		watcher.add(assets().getImage("images/pngs/demoPage1.png"));
		watcher.add(assets().getImage("images/pngs/demoPage2.png"));
		watcher.add(assets().getImage("images/pngs/demoPage3.png"));
		watcher.add(assets().getImage("images/pngs/demoPage4.png"));
		watcher.add(assets().getImage("images/pngs/demoPage5.png"));
		watcher.add(assets().getImage("images/pngs/demoPage6.png"));
		watcher.add(assets().getImage("images/pngs/demoPage7.png"));
		watcher.add(assets().getImage("images/pngs/demoPage8.png"));
		watcher.add(assets().getImage("images/pngs/demoPage9.png"));
		watcher.add(assets().getImage("images/pngs/doneButton.png"));
		watcher.add(assets().getImage("images/pngs/dupComponentButtonImage.png"));
		watcher.add(assets().getImage("images/pngs/dupComponentButtonImagePressed.png"));
		watcher.add(assets().getImage("images/pngs/dupComponent.png"));
		watcher.add(assets().getImage("images/pngs/finishChecker.png"));
		watcher.add(assets().getImage("images/pngs/flipComponentButtonImage.png"));
		watcher.add(assets().getImage("images/pngs/flipComponentButtonImagePressed.png"));
		watcher.add(assets().getImage("images/pngs/flipComponent.png"));
		watcher.add(assets().getImage("images/pngs/goalBar.png"));
		watcher.add(assets().getImage("images/pngs/goButton.png"));
		watcher.add(assets().getImage("images/pngs/homeButton.png"));
		watcher.add(assets().getImage("images/pngs/inaccessibleLevelButton.png"));
		watcher.add(assets().getImage("images/pngs/levelButtonActive.png"));
		watcher.add(assets().getImage("images/pngs/levelButton.png"));
		watcher.add(assets().getImage("images/pngs/menuBackground.png"));
		watcher.add(assets().getImage("images/pngs/menuButton.png"));
		watcher.add(assets().getImage("images/pngs/nextButton.png"));
		watcher.add(assets().getImage("images/pngs/pauseButton.png"));
		watcher.add(assets().getImage("images/pngs/playButton.png"));
		watcher.add(assets().getImage("images/pngs/playButtonPressed.png"));
		watcher.add(assets().getImage("images/pngs/retryButton.png"));
		watcher.add(assets().getImage("images/pngs/splitComponentButtonImage.png"));
		watcher.add(assets().getImage("images/pngs/splitComponentButtonImagePressed.png"));
		watcher.add(assets().getImage("images/pngs/standardBackground.png"));
		watcher.add(assets().getImage("images/pngs/startPage.png"));
		watcher.add(assets().getImage("images/pngs/trains.png"));
		watcher.add(assets().getImage("images/pngs/watermelonTr.png"));
		watcher.add(assets().getImage("images/basicCarriage.png"));
		watcher.add(assets().getImage("images/emptyTrain.png"));
		watcher.add(assets().getImage("images/finishChecker.png"));
		watcher.add(assets().getImage("images/fullTrain.png"));
		watcher.add(assets().getImage("images/red_train.png"));
	}
	
	@Override
	public void update(float delta) {
		mScene.update(delta);
	}

	@Override
	public void paint(float alpha) {
		// TODO Auto-generated method stub
	}

	@Override
	public int updateRate() {
		return 0;
	}
	
	public Scene getDemoScene() {
		Scene demoScene;
		demoScene = new DemoScene(this);
		return demoScene;
	}
	
	public Scene getStartScene() {
		Scene startScene;
		startScene = new StartScene(this);
		return startScene;
	}

	public Scene getLevelSelectScene() {
		// We need to create a new scene here, to update our progress
		return new LevelSelectScene(this);
	}
	
	public void clearScene(){
		mScene.onDetach();
		mScene = new LoadingScene(this);
		mScene.onAttach();		
	}
	
	public void setScene(Scene scene) {
		mScene.onDetach();
		mScene = scene;
		scene.onAttach();
	}
	
	public Scene getScene() {
		return mScene;
	}
	
	public void setLevel(int index){
		LevelTracker.updateLevel(index);
		clearScene();
		setScene(new LevelScene(this,Level.levels.get(index)));
		currentLevel = index;
		PlayN.log().debug("Setting Level "+index);
	}
	public void levelComplete(){
		LevelTracker.setCurrentProgress(Math.max((currentLevel+1),LevelTracker.getCurrentProgress()));
	}

	

}
