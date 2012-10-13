package com.github.trainbox.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import playn.core.PlayN;
import pythagoras.f.Point;

import com.github.trainbox.core.model.Level;
import com.github.trainbox.core.model.Train;
import com.github.trainbox.core.uimodel.TrainTaker;
import com.github.trainbox.core.uimodel.UIComponent;
import com.github.trainbox.core.uimodel.UIHorizontalComponent;
import com.github.trainbox.core.uimodel.UITrain;
import com.github.trainbox.model.PlayNMocks.MockPlatform;

public class UIComponentTest extends TestCase {
	
	private static final int RUNS = 1000;
	
	private void addTrains(UIComponent track, Collection<Train> trains) {
		for (Train train : trains) {
			UITrain uitrain = new UITrain(train);
			uitrain.setPosition(new Point(-uitrain.getSize().width, 0));
			track.takeTrain(uitrain);
		}
	}
	
	private List<Train> run(UIComponent track) {
		MemoryTaker taker = new MemoryTaker();
		track.setTrainTaker(taker);
		for (int i = 0; i < RUNS; i++)
			track.update(Float.MAX_VALUE);
		return taker.trains;
	}
	
	private class MemoryTaker implements TrainTaker {
		public List<Train> trains = new ArrayList<Train>();
		@Override
		public void takeTrain(UITrain train) {
			trains.add(train.train());
		}
		@Override
		public float leftBlock() {
			return Float.MAX_VALUE;
		}
		@Override
		public void updateMaxLengthTrainExpected(int compNum, int len) {
			// TODO Auto-generated method stub
			
		}
	}
	
	// ----------- Tests ------------------------------------------------------
	
	@Test
	public void testSimple() {
		Level level = new Level(0, "",
				"1-2-3-4",
				"1-2-3-4",
				0, 0, 0, 0, 0);
		UIHorizontalComponent track = new UIHorizontalComponent(1);
		addTrains(track, level.input);
		assertEquals(run(track), level.goal);
	}
	
	@Override
	public void setUp() {
		PlayN.setPlatform(new MockPlatform());
	}
}































