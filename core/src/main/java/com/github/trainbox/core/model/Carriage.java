package com.github.trainbox.core.model;

public class Carriage implements Train {
	
	private int mCargo;
	private Train mNext;
	
	/**
	 * Create a new train linked to an existing train.
	 * @param cargo The number on this train.
	 * @param next The next train.
	 */
	public Carriage(int cargo, Train next) {
		mCargo = cargo;
		mNext = next;
	}
	
	/**
	 * A singleton train.
	 */
	public Carriage(int cargo) {
		this(cargo, EMPTY);
	}
	
	@Override
	public int cargo() {
		return mCargo;
	}

	@Override
	public Train tail() {
		return mNext;
	}
	
	@Override
	public Train head() {
		return new Carriage(cargo());
	}
	
	@Override
	public int length() {
		return tail().length() + 1;
	}
	
	@Override
	public Train addLast(Train behind) {
		return new Carriage (cargo(), tail().addLast(behind));
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Train) {
			if (this == other) return true;
			Train t = (Train)other;
			return length() == t.length()
					&& cargo() == t.cargo()
					&& tail().equals(t.tail());
		}
		return false;
	}
	
	@Override
	public String toString() {
		String str = "("+cargo()+">";
		if (length() > 1)
			return tail().toString()+"-"+str;
		return str;
	}
}
