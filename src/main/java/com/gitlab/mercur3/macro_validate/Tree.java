package com.gitlab.mercur3.macro_validate;

import java.util.ArrayList;
import java.util.LinkedHashMap;

class Tree {
	public final LinkedHashMap<ElementWithAccessor, ArrayList<Constraint>> nodes;

	public Tree() {
		this.nodes = new LinkedHashMap<>();
	}

	public void insert(ElementWithAccessor el, Constraint c) {
		nodes.putIfAbsent(el, new ArrayList<>());
		nodes.get(el).add(c);
	}
}
