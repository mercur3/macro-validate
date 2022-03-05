package com.gitlab.mercur3.macro_validate;

import com.gitlab.mercur3.jrusty.result.Empty;
import com.gitlab.mercur3.jrusty.result.Err;
import com.gitlab.mercur3.jrusty.result.Ok;
import com.gitlab.mercur3.jrusty.result.Result;
import com.gitlab.mercur3.macro_validate.constraints.Min;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Types;
import java.util.Set;

class ParseTree {
	private final Element element;
	private final Logger logger;
	private final Types typeUtils;
	private final Tree tree;

	public static ParseTree from(Element el, Logger logger, Types typeUtils) {
		return new ParseTree(el, logger, typeUtils);
	}

	public Result<Empty, Empty> generate() {
		var classMembers = element.getEnclosedElements()
				.stream()
				.filter(el -> el.getKind() == ElementKind.FIELD)
				.toList();
		for (var el : classMembers) {
			var resMin = processMin(el);
			if (resMin.isErr()) {
				return resMin;
			}
		}

		return new Ok<>(Empty.UNIT);
	}

	/// PRIVATE

	private ParseTree(Element element, Logger logger, Types typeUtils) {
		this.element = element;
		this.logger = logger;
		this.typeUtils = typeUtils;
		this.tree = new Tree();
	}

	private Result<Empty, Empty> processMin(Element el) {
		// metadata
		var supportedElements = Set.of(TypeKind.BYTE, TypeKind.SHORT, TypeKind.INT, TypeKind.LONG);
		var typeMirror = el.asType();

		var min = el.getAnnotation(Min.class);
		if (min == null) {
			return new Ok<>(Empty.UNIT);
		}

		var modifiers = el.getModifiers();
		if (!modifiers.contains(Modifier.PUBLIC)) {
			logger.error("Only public class members are currently supported", el);
			return new Err<>(Empty.UNIT);
		}

		var kind = typeMirror.getKind();
		if (!supportedElements.contains(kind)) {
			// handle the case when it is a wrapper class i.e. =Integer=
			try {
				var unboxedType = typeUtils.unboxedType(typeMirror);
				if (!supportedElements.contains(unboxedType.getKind())) {
					return new Err<>(Empty.UNIT);
				}
			}
			catch (IllegalArgumentException ex) {
				logger.error(String.format("Type %s is not supported", typeMirror), el);
				return new Err<>(Empty.UNIT);
			}
		}

		// prepare source code generation
		long val = min.value();
		String msg = min.message();
		tree.insert(
				ElementWithAccessor.publicField(el),
				new Constraint(String.format(" < %d", val), msg)
		);
		return new Ok<>(Empty.UNIT);
	}
}