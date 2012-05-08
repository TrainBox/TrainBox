package com.github.thomasahle.trainbox.trainbox.uimodel;

import playn.core.GroupLayer;
import playn.core.Layer;
import playn.core.Layer.HitTester;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Point;
import static playn.core.PlayN.graphics;

import com.github.thomasahle.trainbox.trainbox.model.Level;
import com.github.thomasahle.trainbox.trainbox.model.Train;

public class UILevel implements TrainsChangedListener, LevelFinishedListener, Listener, HitTester {
	
	private GroupLayer mLayer;
	private GroupLayer mTrainLayer;
	private UIStartComponent mStart;
	private UIGoalComponent mGoal;
	private UIComposite mTrack;
	private Level mLevel;
	private LevelFinishedListener mListener;
	
	public UILevel(Level level) {
		mLevel = level;
		mLayer = graphics().createGroupLayer();
		
		UIHorizontalComponent track = new UIHorizontalComponent(100); 
		mTrack = track;
		//mStart = new UIStartComponent(level.input);
		//track.add(mStart);
		//mGoal = new UIGoalComponent(level.goal);
		//track.add(mGoal);
		mTrack.paused(true);
		
		// Create a recursive track
		
		
		
		track.add(new UIIdentityComponent(100));
		track.add(new UISeparateComponent(100));
		
		UIHorizontalComponent top = new UIHorizontalComponent(60);
		UIHorizontalComponent bot = new UIHorizontalComponent(60);
		track.add(new UISplitMergeComponent(top, bot));
		
		UIHorizontalComponent top1 = new UIHorizontalComponent(40);
		UIHorizontalComponent bot1 = new UIHorizontalComponent(40);
		top.add(new UISplitMergeComponent(top1,bot1));
		top1.add(new UIJoinComponent(80));
		top1.add(new UIJoinComponent(80));
		bot1.add(new UIJoinComponent(80));
		bot1.add(new UIJoinComponent(80));
		
		UIHorizontalComponent top2 = new UIHorizontalComponent(40);
		UIHorizontalComponent bot2 = new UIHorizontalComponent(40);
		bot.add(new UISplitMergeComponent(top2,bot2));
		top2.add(new UIJoinComponent(80));
		top2.add(new UIJoinComponent(80));
		bot2.add(new UIJoinComponent(80));
		bot2.add(new UIJoinComponent(80));
		
		track.add(new UIIdentityComponent(200));
		track.add(new UIIdentityComponent(200));
		track.add(new UIIdentityComponent(200));

		//track.add(new UIDupComponent(100));
		//	UIHorizontalComponent nested = new UIHorizontalComponent(100);
		//	nested.add(new UIDupComponent(100));
		//track.add(nested);
		
		// This wont be needed when StartComponent is finished
		mTrainLayer = graphics().createGroupLayer();
		for (Train train : level.input) {
			UITrain uitrain = new UITrain(train);
			uitrain.setPosition(new Point(-uitrain.getSize().width, 0));
			mTrack.takeTrain(uitrain);
			mTrainLayer.add(uitrain.getLayer());
		}
			
		
		mLayer.add(mTrack.getBackLayer());
		mLayer.add(mTrainLayer);
		mLayer.add(mTrack.getFrontLayer());
		mLayer.setHitTester(this);
		
		mTrack.setTrainsChangedListener(this);
		mTrack.getBackLayer().addListener(this);
		//mGoal.setListener(this);
	}
	
	public void update(float delta) {
		mTrack.update(delta);
	}
	
	
	public Layer layer() {
		return mLayer;
	}
	@Override
	public void onTrainCreated(UITrain train) {
		mTrainLayer.add(train.getLayer());
	}
	@Override
	public void onTrainDestroyed(UITrain train) {
		mTrainLayer.remove(train.getLayer());
	}
	
	
	public boolean paused() {
		return mTrack.paused();
	}
	public void paused(boolean paused) {
		mTrack.paused(paused);
	}
	
	
	public void setListener(LevelFinishedListener listener) {
		mListener = listener;
	}
	@Override
	public void levelCleared() {
		mListener.levelCleared();
	}
	@Override
	public void levelFailed() {
		mListener.levelFailed();
	}

	@Override
	public void onPointerStart(Event event) {
		Point p = new Point(event.localX(), event.localY());
//		mTrack.insertChildAt(new UIJoinComponent(80), p);
		mTrack.insertChildAt(new UIDupComponent(80), p);
	}
	@Override public void onPointerEnd(Event event) {}
	@Override public void onPointerDrag(Event event) {}

	@Override
	public Layer hitTest(Layer layer, Point p) {
		float x = mTrack.getPosition().x;
		float y = mTrack.getPosition().y + mTrack.getSize().height*0.85f;
		float x1 = x + mTrack.getSize().width;
		float y1 = y + mTrack.getSize().height;
		if (x <= p.x && p.x < x1 && y <= p.y && p.y < y1)
			return layer;
		return mTrack.getBackLayer().hitTest(p);
	}
}
