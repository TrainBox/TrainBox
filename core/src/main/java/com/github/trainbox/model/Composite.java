package com.github.trainbox.model;

import java.util.List;

public interface Composite extends Component {
	public List<Component> getChildren();
}
