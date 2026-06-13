package io.github.mercur3.macro_validate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class Tree {
	public final LinkedHashMap<ElementWithAccessor, ArrayList<Constraint>> nodes = new LinkedHashMap<>();

	public void insert(ElementWithAccessor el, Constraint c) {
		nodes.putIfAbsent(el, new ArrayList<>());
		nodes.get(el).add(c);
	}
}
