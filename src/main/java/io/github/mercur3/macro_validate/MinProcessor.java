package io.github.mercur3.macro_validate;

import io.github.mercur3.macro_validate.constraints.Min;
import lombok.NoArgsConstructor;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

@NoArgsConstructor
final class MinProcessor extends PrimitiveProcessor {
	private static final Class<Min> MIN_CLASS = Min.class;

	@Override
	public Class<? extends Annotation> getKind() {
		return MIN_CLASS;
	}

	@Override
	public void addToTree(Element el, Tree parseTree, String accessor) {
		var annotation = el.getAnnotation(MIN_CLASS);
		long val = annotation.value();
		String msg = annotation.message();

		parseTree.insert(
				new ElementWithAccessor(el, accessor),
				new Constraint(String.format(">= %d", val), msg)
		);
	}
}
