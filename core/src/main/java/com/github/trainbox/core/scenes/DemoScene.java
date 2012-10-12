package com.github.trainbox.core.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.pointer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import pythagoras.f.Point;

import com.github.trainbox.core.TrainBox;
import com.github.trainbox.core.model.Level;
import com.github.trainbox.core.uimodel.UIFlipComponent;
import com.github.trainbox.core.uimodel.UIHorizontalComponent;
import com.github.trainbox.core.uimodel.UIJoinComponent;
import com.github.trainbox.core.uimodel.UILevel;
import com.github.trainbox.core.uimodel.UISeparateComponent;
import com.github.trainbox.core.uimodel.UISplitMergeComponent;

public class DemoScene implements Scene {
    int width = graphics().width();
	int height = graphics().height();
	CanvasImage bgImage = graphics().createImage(graphics().width(),graphics().height());
    ImageLayer bgLayer;
    GroupLayer demoLayer;
    TrainBox trainBox;
    List<GroupLayer> demoPages;
    Map<Integer, UILevel> levels;
    
    public static final int LEVELS = 9;
    
    int mPage;

    ImageLayer nextButtonImageLayer;
    ImageLayer backButtonImageLayer;
    ImageLayer doneButtonImageLayer;
    
	public DemoScene(final TrainBox trainBox) {
		this.trainBox = trainBox;
        bgLayer = graphics().createImageLayer(bgImage);
		Canvas canvas = bgImage.canvas();
        final Image backgroundImage = assets().getImage("images/pngs/startPage.png");
		canvas.drawImage(backgroundImage, 0, 0);
		
		// Create the demoLayer that contains the demo pages
        demoLayer = graphics().createGroupLayer();
        demoLayer.setTranslation(width/20+40, height/20);
        final String[] images = {
        	"images/pngs/demoPage1.png",
        	"images/pngs/demoPage2.png",
        	"images/pngs/demoPage3.png",
        	"images/pngs/demoPage4.png",
        	"images/pngs/demoPage5.png",
        	"images/pngs/demoPage6.png",
        	"images/pngs/demoPage7.png",
        	"images/pngs/demoPage8.png",
        	"images/pngs/demoPage9.png"
        };
        
        assert(images.length == LEVELS);
        
        demoPages = new ArrayList<GroupLayer>();
        for (String path : images) {
        	Image image = assets().getImage(path);
        	ImageLayer imageLayer = graphics().createImageLayer(image);
        	GroupLayer groupLayer = graphics().createGroupLayer();
        	groupLayer.add(imageLayer);
        	groupLayer.setVisible(false);
        	demoPages.add(groupLayer);
        	demoLayer.add(groupLayer);
        }
        
        assert(demoPages.size() == LEVELS);
        
        initDemoLevels();
        
        PlayN.log().debug("images.length = " + images.length + ", "
        		+ "demoPages.length = " + demoPages.size() + ", "
        		+ "levels.length = " + levels.size());
        
        // TODO could possibly be a bit cleaner
        for (int i = 0; i < demoPages.size(); i++) {
        	UILevel level = levels.get(i);
        	if (level != null) demoPages.get(i).add(level.layer());
        }
        
        demoLayer.setVisible(true);
        mPage = 0;
        demoPages.get(mPage).setVisible(true);
        
        final Image backButtonImage = assets().getImage("images/pngs/backButton.png");
        backButtonImageLayer = graphics().createImageLayer(backButtonImage);
        demoLayer.add(backButtonImageLayer);
		backButtonImageLayer.setTranslation(20, 520);
		backButtonImageLayer.setVisible(false);
		backButtonImageLayer.addListener(new Pointer.Adapter() {

			@Override
			public void onPointerStart(Pointer.Event event) {
				previousPage();
			}

		});
        
        final Image nextButtonImage = assets().getImage("images/pngs/nextButton.png");
        nextButtonImageLayer = graphics().createImageLayer(nextButtonImage);
        demoLayer.add(nextButtonImageLayer);
		nextButtonImageLayer.setTranslation(680, 520);
		nextButtonImageLayer.addListener(new Pointer.Adapter() {

			@Override
			public void onPointerStart(Pointer.Event event) {
				nextPage();
			}
		});

		final Image doneButtonImage = assets().getImage("images/pngs/doneButton.png");
        doneButtonImageLayer = graphics().createImageLayer(doneButtonImage);
        demoLayer.add(doneButtonImageLayer);
		doneButtonImageLayer.setTranslation(680, 520);
		doneButtonImageLayer.setVisible(false);
		doneButtonImageLayer.addListener(new Pointer.Adapter() {

			@Override
			public void onPointerStart(Pointer.Event event) {
				leaveDemo();
			}
		});

	}
	
	private void nextPage() {
		setPage(getPage() + 1);
	}
	
	private void previousPage() {
		setPage(getPage() - 1);
	}
	
	private void leaveDemo() {
		setPage(0);
		trainBox.setScene(trainBox.getStartScene());
	}
	
	private void setPage(int page) {
		demoPages.get(mPage).setVisible(false);
		if (levels.get(mPage) != null)
			levels.get(mPage).paused(true);
		
		mPage = page;
		
		demoPages.get(mPage).setVisible(true);
		if (levels.get(mPage) != null) {
			levels.get(mPage).reset();
			levels.get(mPage).paused(false);
		}
		
		backButtonImageLayer.setVisible(mPage != 0);
		nextButtonImageLayer.setVisible(mPage != demoPages.size() - 1);
		doneButtonImageLayer.setVisible(mPage == demoPages.size() - 1);
		
	}
	
	private int getPage() {
		return mPage;
	}

	private void initDemoLevels() {
		levels = new HashMap<Integer, UILevel>(LEVELS);
		UILevel level5 = new UILevel(new Level(
				0, "",
				"2 1", "2-1",
				0,0,0,0,0
			));
		
		level5.paused(true);
		//mLevelPage6.insertChildAt(new UIJoinComponent(), new Point(0,0));
		level5.insertChildAt(new UIJoinComponent(), new Point(210,50));
		level5.layer().setTranslation(80, 420);
		levels.put(5, level5);
		
		UILevel level6 = new UILevel(new Level(
				0, "",
				"2-1", "2 1",
				0,0,0,0,0
			));
		level6.paused(true);
		level6.insertChildAt(new UISeparateComponent(), new Point(210,50));
		level6.layer().setTranslation(80, 420);
		levels.put(6, level6);
		
		UILevel level7 = new UILevel(new Level(
				0, "",
				"2 1", "1 2",
				0,0,0,0,0
			));
		level7.paused(true);
		level7.insertChildAt(new UIFlipComponent(), new Point(210,50));
		level7.layer().setTranslation(80, 420);
		levels.put(7, level7);
		
		UILevel level8 = new UILevel(new Level(
				0, "",
				"2 1", "2 1",
				0,0,0,0,0
			));
		level8.paused(true);
		level8.insertChildAt(new UISplitMergeComponent(new UIHorizontalComponent(100), new UIHorizontalComponent(100)), new Point(210,50));
		level8.layer().setTranslation(10, 350);
		levels.put(8, level8);
	}

	@Override
	public void onAttach() {
		graphics().rootLayer().add(bgLayer);
	    graphics().rootLayer().add(demoLayer);
		//graphics().rootLayer().add();
	    pointer().setListener(null);
	}

	@Override
	public void onDetach() {
		graphics().rootLayer().remove(bgLayer);
	    graphics().rootLayer().remove(demoLayer);
		//graphics().rootLayer().remove(mLevel.layer());
	    pointer().setListener(null);
	}
	
	@Override
	public void update(float delta) {
		for (UILevel level : levels.values())
			if (level != null) level.update(delta);
	}
}
