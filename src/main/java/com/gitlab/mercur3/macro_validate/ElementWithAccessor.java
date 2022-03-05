package com.gitlab.mercur3.macro_validate;

import javax.lang.model.element.Element;

class ElementWithAccessor {
	public final Element element;
	public final String accessor;

	private ElementWithAccessor(Element element, String accessor) {
		this.element = element;
		this.accessor = accessor;
	}

	public static ElementWithAccessor publicField(Element el) {
		return new ElementWithAccessor(el, String.format("this.%s", el.getSimpleName()));
	}

	@Override
	public int hashCode() {
		return element.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ElementWithAccessor o) {
			return element.equals(o.element);
		}
		return false;
	}
}