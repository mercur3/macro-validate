package com.gitlab.mercur3.macro_validate;

import com.gitlab.mercur3.jrusty.result.Empty;
import com.gitlab.mercur3.jrusty.result.Err;
import com.gitlab.mercur3.jrusty.result.Ok;
import com.gitlab.mercur3.jrusty.result.Result;
import com.gitlab.mercur3.macro_validate.constraints.Min;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

final class MinProcessor extends PrimitiveProcessor {
	private static final Class<Min> MIN_CLASS = Min.class;

	MinProcessor() {
		super();
	}

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
