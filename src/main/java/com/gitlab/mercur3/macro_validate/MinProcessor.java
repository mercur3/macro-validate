package com.gitlab.mercur3.macro_validate;

import com.gitlab.mercur3.jrusty.result.Empty;
import com.gitlab.mercur3.jrusty.result.Ok;
import com.gitlab.mercur3.macro_validate.constraints.Min;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

final class MinProcessor extends PrimitiveProcessor {
	MinProcessor() {
		super();
	}

	@Override
	public Class<? extends Annotation> getKind() {
		return Min.class;
	}

	@Override
	public void addToTree(Element el, Tree parseTree, String accessor) {
		var annotation = el.getAnnotation(Min.class);
		long val = annotation.value();
		String msg = annotation.message();

		parseTree.insert(
				new ElementWithAccessor(el, accessor),
				new Constraint(String.format(">= %d", val), msg)
		);
	}
}
