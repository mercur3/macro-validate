package com.gitlab.mercur3.macro_validate;

import com.gitlab.mercur3.macro_validate.constraints.Max;
import com.gitlab.mercur3.macro_validate.constraints.Min;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Optional;

final class MaxProcessor extends PrimitiveProcessor {
	public static final Class<Max> MAX_CLASS = Max.class;

	MaxProcessor() {
		super();
	}

	@Override
	public Class<? extends Annotation> getKind() {
		return MAX_CLASS;
	}

	@Override
	public Optional<AnnotationProcessorError> isUsedCorrectlyOnPrimitive(Element el, MetaUtils utils) {
		var res = super.genericIsUsedCorrectly(el, utils);
		if (res.isPresent()) {
			return res;
		}

		var min = el.getAnnotation(Min.class);
		if (min != null) {
			var minVal = min.value();
			var maxVal = el.getAnnotation(MAX_CLASS).value();
			if (minVal > maxVal) {
				utils
						.logger()
						.warning(
								String.format(
										"Usage of Min.class in combination with Max.class. min: %d, max: %d",
										minVal,
										maxVal
								),
								el
						);
			}
		}
		return res;
	}

	@Override
	public void addToTree(Element el, Tree parseTree, String accessor) {
		var annotation = el.getAnnotation(MAX_CLASS);
		long val = annotation.value();
		String msg = annotation.message();

		parseTree.insert(
				new ElementWithAccessor(el, accessor),
				new Constraint(String.format("<= %d", val), msg)
		);
	}
}