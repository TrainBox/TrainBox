package com.github.trainbox.core.model;

public interface Component {
	/**
	 * Returns the first train that comes out of the component.
	 * Or Train.EMPTY if there is currently nothing to give.
	 * @return
	 */
	public Train pull();
}
