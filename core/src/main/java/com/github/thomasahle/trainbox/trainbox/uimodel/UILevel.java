package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayList;
import java.util.List;

import playn.core.GroupLayer;
import playn.core.Layer;
import playn.core.Layer.HitTester;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

import com.github.thomasahle.trainbox.trainbox.model.Level;
import com.github.thomasahle.trainbox.trainbox.model.Train;

public class UILevel implements TrainsChangedListener, LevelFinishedListener, HitTester {
	
	private GroupLayer mLayer;
	private GroupLayer mTrainLayer;
	private UIStartComponent mStart;
	private UIGoalComponent mGoal;
	private UIComposite mTrack;
	private Level mLevel;	
	private LevelFinishedListener mListener;
	
	private Point farthestPointReachedByTrains = new Point(0,0);
	
	public UILevel(Level level) {
		mLevel = level;
		mLayer = graphics().createGroupLayer();
		
		mTrainLayer = graphics().createGroupLayer();
		List<UITrain> trains = new ArrayList<UITrain>();
		for (Train train : level.input) {
			UITrain uitrain = new UITrain(train);
			trains.add(uitrain);
			mTrainLayer.add(uitrain.getLayer());
		}
		
		UIHorizontalComponent track = new UIHorizontalComponent(100); 
		mStart = new UIStartComponent(trains);
		mGoal = new UIGoalComponent(mLevel.goal);
		mGoal.setListener(this);
		
		track.add(mStart);
		track.add(mGoal);
		
		// Small hack to make start and goal the actual ends
		((UIIdentityComponent)track.getChildren().get(0)).setWidth(1);
		((UIIdentityComponent)track.getChildren().get(track.getChildren().size()-1)).setWidth(1);
		
		mTrack = track;
		mTrack.paused(true);
		
		mLayer.add(mTrack.getBackLayer());
		mLayer.add(mTrainLayer);
		mLayer.add(mTrack.getFrontLayer());
		mLayer.setHitTester(this);
		
		mTrack.setTrainsChangedListener(this);
	}
	
	public void update(float delta) {
		mTrack.update(delta);
	}
	
	public void reset(){
	//reset position of all trains
		//recursively remove trains from all components
		mTrack.reset();
		//clear the trainLayer
		mTrainLayer.clear();
		//recreated the original trains in the start component as part of StartComponent.reset(), so we add these to the trainLayer
		for(UITrain uitrain: mStart.getTrains()){
			mTrainLayer.add(uitrain.getLayer());
		}
		//keep components where they are (don't modify track)
		
		//reset other stuff
			//farthestPoint
			this.farthestPointReachedByTrains = new Point(0,0);
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
	
	/**
	 * 
	 * @return The farthest position a train has been to the right.
	 * This is because we don't want the auto-scroll to jump back when a train is taken from the track by a component.
	 */
	public Point getFarthestTrainPosition(){
		for (UITrain train : mTrack.getTrains())
			for (UICarriage car : train.getCarriages())
				if (car.getPosition().x+train.getPosition().x > farthestPointReachedByTrains.x)
					farthestPointReachedByTrains = car.getPosition().add(train.getPosition().x, train.getPosition().y);
		return farthestPointReachedByTrains;
	}
	
	public void paused(boolean paused) {
		mTrack.paused(paused);
	}
	
	public void setListener(LevelFinishedListener listener) {
		mListener = listener;
	}
	@Override
	public void levelCleared() {
		mTrack.paused(true);
		// check all trains have arrived in the goalComponent
		List<UITrain> trainsInGoalComp = mGoal.getTrains();
		List<UITrain> allTrains = mTrack.getTrains();

		allTrains.removeAll(trainsInGoalComp);

		if (!allTrains.isEmpty()) { //  not all the trains are in the goalComp
			log().debug("EXTRA TRAINS");
			this.levelFailed("Hidden trains in your track!");
		} else {
			log().debug("LEVEL CLEARED !!!");
			if (mListener != null)
				mListener.levelCleared();
		}
	}
	@Override
	public void levelFailed(String message) {
		mTrack.paused(true);
		log().debug("LEVEL FAILED !!!");
		if (mListener != null)	
			mListener.levelFailed(message);
	}

	@Override
	public Layer hitTest(Layer layer, Point p) {
		return layer;
	}
	
	public void setTrainSpeed(float s){
		for(UITrain t: mTrack.getTrains()){
			t.setSpeed(s);
		}
	}
	public void increaseTrainSpeed(float ds){
		for(UITrain t: mTrack.getTrains()){
			t.setSpeed(t.getSpeed()+ds);
		}
	}

	public void decreaseTrainSpeed(float ds) {
		for(UITrain t: mTrack.getTrains()){
			if(t.getSpeed()-ds >= 0)
			t.setSpeed(t.getSpeed()-ds);
		}
	}
	
	public Dimension getSize(){
		return mTrack.getSize();
	}

	public Level getLevel() {
		return mLevel;
	}
	
	public boolean insertChildAt(UIComponent child, Point position) {
		return mTrack.insertChildAt(child, position);
	}
	public Boolean deleteChildAt(Point position){
		return mTrack.deleteChildAt(position) != null;
	}
	
	public void startMaxExpectedTrainPropogation(){
		mTrack.getChildren().get(0).updateMaxLengthTrainExpected(0, 0);
	}
	
	public int countUserComponents() {
		return countChildrenRecursively(mTrack);
	}
	private int countChildrenRecursively(UIComponent comp) {
		if (comp instanceof UIComposite) {
			int res = 0;
			for (UIComponent child : ((UIComposite)comp).getChildren())
				res += countChildrenRecursively(child);
			if (comp instanceof UISplitMergeComponent)
				res += 1;
			return res;
		}
		if (comp instanceof UIIdentityComponent || comp instanceof UIStartComponent || comp instanceof UIGoalComponent)
			return 0;
		return 1;
	}
}
