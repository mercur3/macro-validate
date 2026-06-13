package io.github.mercur3.macro_validate;

import javax.lang.model.element.Element;

record ElementWithAccessor(Element element, String accessor) {
	public static String publicField(Element el) {
		return String.format(".%s", el.getSimpleName());
	}

	public static String recordField(Element el) {
		return String.format(".%s()", el.getSimpleName());
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
