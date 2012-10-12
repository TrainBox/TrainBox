package com.github.trainbox.core.model;

public class BoxComponent implements Component {
	
	private final Component prev;
	
	public BoxComponent(Component prev) {
		this.prev = prev;
	}
	
	private Train buf = Train.EMPTY;
	
	@Override
	public Train pull() {
		Train t = prev.pull();
		if (t.length() == 0)
			return t;
		if (buf.length() == 0) {
			buf = t;
			return pull();
		}
		else {
			t = buf.addLast(t);
			buf = Train.EMPTY;
			return t;
		}
	}
}
