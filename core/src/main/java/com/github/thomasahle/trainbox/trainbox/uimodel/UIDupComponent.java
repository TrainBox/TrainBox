package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import java.util.Queue;

import playn.core.Image;
import playn.core.Layer;
import pythagoras.f.Dimension;

import com.github.thomasahle.trainbox.trainbox.util.CanvasHelper;

public class UIDupComponent extends BlackBoxComponent{

	private Layer mBackLayer, mFrontLayer;
	

	
	public UIDupComponent() {
		mBackLayer = CanvasHelper.newEmptyLayer();
		
		Image dupComponentImage = assets().getImage("images/pngs/dupComponent.png");
		mFrontLayer = graphics().createImageLayer(dupComponentImage);
		
		mWidth = dupComponentImage.width();
		mHeight = dupComponentImage.height();
	}

	

	@Override
	public Dimension getSize() {
		return new Dimension(mWidth, mHeight);
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
	public void onTrainEntered(UITrain train, Queue<UITrain> currentTrains) {
		currentTrains.add(train);
		train.getLayer().setVisible(false);
		UITrain clone = new UITrain(train);
		currentTrains.add(clone);
		fireTrainCreated(clone);
		clone.getLayer().setVisible(false);
	}

}
