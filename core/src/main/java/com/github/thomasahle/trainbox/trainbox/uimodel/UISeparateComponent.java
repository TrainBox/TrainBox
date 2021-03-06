package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class UISeparateComponent extends AbstractComponent {

	private static final float DOWN_LEVEL = 0.15f;

	private static final float UP_LEVEL = 0.95f;

	private Layer mBackLayer, mFrontLayer;
	private int mWidth, mHeight;
	private LinkedList<UITrain> mLeftSide = new LinkedList<UITrain>();
	private LinkedList<UITrain> mRightSide = new LinkedList<UITrain>();
	
	private final static int KNIFE_WIDTH = 10;
	private final static float KNIFE_CYCLE = 1000f;
	private Layer mKnifeLayer;
	private float mKnifeT = 0;
	
	public UISeparateComponent() {
		Image concatComponentImage = assets().getImage("images/pngs/concatComponent.png");
		mBackLayer = graphics().createImageLayer(concatComponentImage);
		
		mWidth = concatComponentImage.width();
		mHeight = concatComponentImage.height();
		
		CanvasImage knifeImage = graphics().createImage(KNIFE_WIDTH, mHeight);
		playn.core.Path knifePath = knifeImage.canvas().createPath();
		knifePath.moveTo(KNIFE_WIDTH/2, 0);
		knifePath.lineTo(KNIFE_WIDTH, mHeight);
		knifePath.lineTo(0, mHeight);
		knifePath.lineTo(KNIFE_WIDTH/2, 0);
		knifeImage.canvas().setFillColor(0xff000000).fillPath(knifePath);
		mKnifeLayer = graphics().createImageLayer(knifeImage);
		
		mFrontLayer = mKnifeLayer;
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
	public List<UITrain> getTrains() {
		List<UITrain> trains = new ArrayList<UITrain>(mLeftSide);
		trains.addAll(mRightSide);
		return trains;
	}

	@Override
	public Dimension getSize() {
		return new Dimension(mWidth, mHeight);
	}
	
	@Override public void setPosition(Point position) {
		super.setPosition(position);
		updateKnife();
	}
	
	/**
	 * @param percentage 0.05 if the gate must be at least 5% up.
	 * @return whether the gate is now that much up.
	 */
	private boolean isUp(float percentage) {
		return percentage <= 1-gateY(mKnifeT)/mHeight;
	}
	
	@Override
	public void update(float delta) {
		if (paused())
			return;
		mKnifeT += delta;
		
		float knifeX = getSize().width/2 + getDeepPosition().x;
		float compLeft = getDeepPosition().x;
		float compRight = compLeft + getSize().width;
		
		// Check if the front left train is ready to be cut
		if (mLeftSide.size() >= 1) {
			UITrain train = mLeftSide.getFirst();
			float trainLeft = train.getPosition().x;
			float carLeft = trainLeft + train.getCarriages().get(0).getPosition().x;
			
			if (train.getCarriages().size() > 1 && carLeft-knifeX > -1) {
				// Wait for the knife to go up.
				// XXX: This would be nicer if the train remembered what time it
				// started to wait, so we don't risk livelocks due to evil delta values.
				if (isUp(UP_LEVEL)) {
					UITrain head = train.cutOffHead();
					fireTrainCreated(head);
					mLeftSide.addFirst(head);
				}
			}
		}
		
		// First make sure all the trains that belong in mRightSide are there
		for (Iterator<UITrain> it = mLeftSide.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			float trainLeft = train.getPosition().x;
			float trainRight = trainLeft + train.getSize().width;
			
			// If the train has passed (only small trains can pass)
			if (trainLeft >= knifeX - 1) {
				assert train.getCarriages().size() == 1;
				it.remove();
				
				if (trainRight > compRight) {
					getTrainTaker().takeTrain(train);
					if (trainLeft < compRight) {
						mRightSide.add(train);
					}
				}
			}
		}
		
		// The right side is easy to move
		float rightBorder = moveTrains(mRightSide, delta);
		int actualLefties = 0;
		
		// The left side requires more thought
		for (UITrain train : mLeftSide) {
			float trainLeft = train.getPosition().x;
			float trainRight = trainLeft + train.getSize().width;
			float carLeft = trainLeft + train.getCarriages().get(0).getPosition().x;
			
			// We don't enforce the PADDING between two trains seperated by the knife.
			// If we did that the left part of a train would jump back, when seperated from its head.
			if (trainRight <= knifeX + 0.1f)
				actualLefties += 1;
			if (actualLefties == 1)
				rightBorder = Math.max(rightBorder, knifeX);
			
			float newRight = Math.min(rightBorder, trainRight + train.speed*delta);
			
			// If we are already on the knife, hold it down
			if (carLeft + 1f < knifeX && knifeX <= trainRight - 1f) {
				mKnifeT = 0;
			}
			// If we are on the left side of the knife, and its up, we have to wait
			if (trainRight - 0.1f < knifeX && isUp(DOWN_LEVEL)) {
				newRight = Math.min(newRight, knifeX);
			}
			// If we are a big train, we can't go too far even if its down
			if (train.getCarriages().size() > 1) {
				newRight = Math.min(newRight, knifeX + UICarriage.WIDTH);
			}
			// Note: if the train is small, we may let it go all the way to rightBorder.
			// The next update should take care of moving it to rightSide and then takeTrain if necessary.
			float newLeft = newRight - train.getSize().width;
			//newLeft = Math.max(trainLeft, newLeft);
			//assert newLeft >= trainLeft;
			train.setPosition(new Point(newLeft, train.getPosition().y));
			rightBorder = Math.min(rightBorder, newLeft-UITrain.PADDING);
		}
		
		// Animate the knife
		updateKnife();
	}

	private void updateKnife() {
		float x = getSize().width/2-KNIFE_WIDTH/2.f;
		float y = gateY(mKnifeT);
		mKnifeLayer.setTranslation(x + getPosition().x, y + getPosition().y);
		mKnifeLayer.setScale(1, (mHeight-y)/mHeight+1e-9f);
	}

	private float gateY(float t) {
		return (float)(mHeight*(Math.cos(t/KNIFE_CYCLE*(2*Math.PI))+1)/2);
	}
	
	@Override
	public void takeTrain(UITrain train) {
		assert train.getPosition().x + train.getSize().width <= leftBlock();
		mLeftSide.add(train);
	}
	
	@Override
	public float leftBlock() {
		// Never go further than the knife
		float res = getSize().width/2 + getDeepPosition().x - 0.1f;
		// Unless it is down.
		// Really we should handle single trains differently, as they may move
		// even further, but we have no way to do that.
		if (!isUp(DOWN_LEVEL))
			res += UICarriage.WIDTH;
		// Don't overlap trains we currently manage
		if (!mLeftSide.isEmpty())
			res = Math.min(res, mLeftSide.getLast().getPosition().x - UITrain.PADDING);
		return res;
	}

	@Override
	public void reset() {
		mLeftSide.clear();
		mRightSide.clear();
	}
	
	@Override
	public void updateMaxLengthTrainExpected(int compNum, int len){
		this.maxExpectedLength = len;
		mTrainTaker.updateMaxLengthTrainExpected(compNum+1, 1);
	}
}
