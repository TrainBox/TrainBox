package com.github.trainbox.core.model;

import java.util.List;

public interface Composite extends Component {
	public List<Component> getChildren();
}
