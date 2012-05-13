package com.github.thomasahle.trainbox.trainbox.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {

	public final int levelNumber;
	public final String title;
	public final List<Train> input, goal;
	
	/**The ideal number of each type of component - solutions with in this should get maximum points*/
	public final int dupsBest,chainsBest,unchainsBest,flipsBest,splitsBest;

	/**Solutions with in this should get some points. Solutions out-side this should still be accepted
	 * WARNING: Some of these stats may be even tighter (i.e. less) than the "Best" stats.
	 * */
	public final int dupsGood,chainsGood,unchainsGood,flipsGood,splitsGood;

	
	/**@param levelNumber - number each level starting from zero, so that each level knows its own index*/
	public Level(
		int levelNumber,
		String title,
		String input,
		String goal,
		int dupsBest,
		int chainsBest,
		int unchainsBest,
		int flipsBest,
		int splitsBest,
		int dupsGood,
		int chainsGood,
		int unchainsGood,
		int flipsGood,
		int splitsGood

	){
		this.levelNumber=levelNumber;
		this.title=title;
		this.input=ComponentFactory.parseTrains(input);
		this.goal=ComponentFactory.parseTrains(goal);
		this.dupsBest=dupsBest;
		this.chainsBest=chainsBest;
		this.unchainsBest=unchainsBest;
		this.flipsBest=flipsBest;
		this.splitsBest=splitsBest;
		this.dupsGood=dupsGood;
		this.chainsGood=chainsGood;
		this.unchainsGood=unchainsGood;
		this.flipsGood=flipsGood;
		this.splitsGood=splitsGood;
	}
	
	public Level(
		int levelNumber,
		String title,
		String input,
		String goal,
		int dupsBest,
		int chainsBest,
		int unchainsBest,
		int flipsBest,
		int splitsBest
	){
		this.levelNumber=levelNumber;
		this.title=title;
		this.input=ComponentFactory.parseTrains(input);
		this.goal=ComponentFactory.parseTrains(goal);
		this.dupsBest=dupsBest;
		this.chainsBest=chainsBest;
		this.unchainsBest=unchainsBest;
		this.flipsBest=flipsBest;
		this.splitsBest=splitsBest;
		this.dupsGood=dupsBest;
		this.chainsGood=chainsBest;
		this.unchainsGood=unchainsBest;
		this.flipsGood=flipsBest;
		this.splitsGood=splitsBest;
	}

	
	
	private static final List<Level> mLevels = new ArrayList<Level>();
	public static final List<Level> levels = Collections.unmodifiableList(mLevels);
	static{
		/** Here we code all of the levels */
		
		mLevels.add(new Level(
			0, "Warm-up (dup)",
			"2 1", "2 2 1 1",
			1,0,0,0,0
		));
		mLevels.add(new Level(
			1, "Warm-up (chain)",
			"1 1 1 1", "1-1 1-1",
			0,1,0,0,0
		));
		mLevels.add(new Level(
			2, "Express Train Builder",
			"1 1", "1-1 1-1",
			2,1,0,0,0
		));
		mLevels.add(new Level(
			3, "Freight Train Builder",
			"1 1", "1-1-1-1 1-1-1-1",
			3,2,0,0,0
		));
		mLevels.add(new Level(
			4, "Twisted Train Builder",
			"2 1", "2-1-2-1 2-1-2-1",
			3,2,0,0,0
		));
		mLevels.add(new Level(
			5, "Warm-up (unchain)",
			"7 6-5 4 3-2-1", "7 6 5 4 3 2 1",
			0,0,1,0,0
		));
		mLevels.add(new Level(
			6, "Re-twist",
			"2 1", "2 1 2 1 2 1 2 1",
			3,1,1,0,0
		));
		mLevels.add(new Level(
			7, "Warm-up (track-split)",
			"2 1", "1 2 1",
			1,0,0,0,1
		));
		mLevels.add(new Level(
			8, "Odd Train Builder",
			"1", "1 1 1",
			2,0,0,0,1
		));
		mLevels.add(new Level(
			9, "Warm-up (flip)",
			"4 3 2 1", "3 4 1 2",
			0,0,0,1,0
		));
		mLevels.add(new Level(
			10, "Really Twisted Train Builder",
			"2 1", "1-2-1-2 1-2-1-2",
			3,2,0,1,0
		));
		mLevels.add(new Level(
			11, "Reverse Express",
			"4 3 2 1", "1 2 3 4",
			0,0,0,3,1,
			0,1,1,2,0
		));
		mLevels.add(new Level(
			12, "Reverse Small-Train",
			"3 2 1", "1 2 3",
			0,0,0,1,1
		));
		mLevels.add(new Level(
			13, "Small Reshuffle",
			"4-3 2-1", "4-2 3-1",
			0,2,1,0,1,
			0,1,2,0,1
		));
		mLevels.add(new Level(
			14, "Reverse Mega-Freight",
			"8 7 6 5 4 3 2 1", "1 2 3 4 5 6 7 8",
			0,2,1,3,0,
			0,0,0,7,3
		));
		mLevels.add(new Level(
			15, "Gigantic Reshuffle",
			"8-7-6-5 4-3-2-1", "8-4 7-3 6-2 5-1",
			0,1,2,0,1
		));
		mLevels.add(new Level(
			16, "Rotate Express",
			"4 3 2 1", "1 4 3 2",
			0,1,1,3,1
		));
		mLevels.add(new Level(
			17, "Titanic Reshuffle",
			"8-7 6-5 4-3 2-1", "8-6-4-2 7-5-3-1",
			0,1,4,0,3
		));
		mLevels.add(new Level(
			18, "Prime Train Builder (5)",
			"1", "1 1 1 1 1",
			3,1,1,0,1,
			4,0,0,0,2
		));
		mLevels.add(new Level(
			19, "Warm-up (dup)",
			"2 1", "2 2 1 1",
			3,2,0,1,0
		));
		mLevels.add(new Level(
			20, "Triangle Number Builder (7)",
			"1", "1 1 1 1 1 1 1",
			5,0,0,0,2
		));
		mLevels.add(new Level(
			21, "Rotate Small-Train",
			"3 2 1", "1 3 2",
			0,2,2,2,2
		));
		mLevels.add(new Level(
			22, "Rotate Mega-Freight",
			"8 7 6 5 4 3 2 1", "1 8 7 6 5 4 3 2",
			0,3,2,6,2
		));
		mLevels.add(new Level(
			23, "Lame Octopus",
			"7 6 5 4 3 2 1", "1 2 3 4 5 6 7",
			0,1,1,3,2
		));
		mLevels.add(new Level(
			24, "Triple Team",
			"3 2 1", "3-2-1",
			0,3,1,0,2
		));
		mLevels.add(new Level(
			25, "Permutations",
			"0 1", "0-0-0-0 1-0-0-0 0-1-0-0 0-0-1-0 0-0-0-1",
			4,5,1,0,1
		));
		mLevels.add(new Level(
			26, "Permutations",
			"0 0 0 1", "0-0-0-0 1-0-0-0 0-1-0-0 0-0-1-0 0-0-0-1",
			3,5,1,0,2
		));
		mLevels.add(new Level(
			27, "Mafia Gang",
			"5 4 3 2 1", "5-4-3-2-1",
			0,6,3,0,6
		));


	}
	
}
