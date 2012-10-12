package com.github.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import playn.core.CanvasImage;
import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class UIStartComponent extends AbstractComponent {

	private final static int HEIGHT = 100;
	private int mWidth;

	private final List<UITrain> initialTrains; // not editied
	private Queue<UITrain> mTrains;
	private Layer mFrontLayer;
	private Layer mBackLayer;
	
	public UIStartComponent() {
		this(new ArrayList<UITrain>());
	}
	
	@Override
	public List<UITrain> getTrains() {
	    return Collections.unmodifiableList(new ArrayList<UITrain>(mTrains));
	}
	
	public UIStartComponent(List<UITrain> trains) {
		mWidth = calcWidth(trains);
		initialTrains = new LinkedList<UITrain>();
		for(UITrain uitrain:trains){
			initialTrains.add(new UITrain(uitrain));
		}
		mTrains = new LinkedList<UITrain>(trains);
		
		float right = mWidth;
		for (UITrain train : trains) {
			float left = right - train.getSize().width;
			train.vertCenterOn(this);
			train.setPosition(new Point(left, train.getPosition().y));
			right = left - UITrain.PADDING;
		}
		
		mFrontLayer = graphics().createImageLayer(graphics().createImage(1,1));
		
		int imageWidth = mWidth+(int)Math.ceil(2*ComponentHelper.RAIL_EXTRA);
		CanvasImage image = graphics().createImage(imageWidth, HEIGHT);
		ComponentHelper.drawTracks(image.canvas(), mWidth, 0xff565248, 0xff816647);
		mBackLayer = graphics().createImageLayer(image);
		xpadding(ComponentHelper.RAIL_EXTRA);
	}

	private static int calcWidth(List<UITrain> input) {
		int width = 0;
		for (UITrain train : input) {
			width += train.getSize().width;
			width += UITrain.PADDING;
		}
		return width;
	}

	@Override
	public Dimension getSize() {
		return new Dimension(mWidth, HEIGHT);
	}

	@Override
	public Layer getBackLayer() {
		return mBackLayer;
	}

	@Override
	public Layer getFrontLayer() {
		return mFrontLayer;
	}

	@Override
	public void update(float delta) {
		if (paused())
			return;
		moveTrains(mTrains, delta);
	}

	@Override
	public void takeTrain(UITrain train) {
		throw new UnsupportedOperationException("Start component doesn't take trains from anyone!");
	}

	@Override
	public float leftBlock() {
		return 0;
	}

	@Override
	public void reset() {
		mTrains.clear();
		//copy back the original trains
		for(UITrain uitrain:initialTrains){
			mTrains.add(new UITrain(uitrain));
		}
		//similar to constructor

		float right = mWidth;
		for (UITrain train : mTrains) {
			float left = right - train.getSize().width;
			train.vertCenterOn(this);
			train.setPosition(new Point(left, train.getPosition().y));
			right = left - UITrain.PADDING;
		}
		
	}
	@Override
	public boolean locked(){
		return true;
	}
	@Override
	public void updateMaxLengthTrainExpected(int compNum, int len){
		//don't care about the values passed
		int maxLen =0;
		for(UITrain t : initialTrains){
			maxLen = Math.max(maxLen, t.getCarriages().size());
		}
		len =maxLen;
		compNum =0;
		log().debug("Max length of train expected for component " + compNum + ":   " + len);
		
		mTrainTaker.updateMaxLengthTrainExpected(1, maxLen);
		
	}
}
