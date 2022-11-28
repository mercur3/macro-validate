package com.gitlab.mercur3.macro_validate;

import com.gitlab.mercur3.jrusty.result.*;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.Set;

abstract sealed class PrimitiveProcessor implements Processor
	permits MinProcessor, MaxProcessor
{
	private static final Set<TypeKind> PRIMITIVE_TYPES = Set.of(
			TypeKind.BYTE,
			TypeKind.SHORT,
			TypeKind.INT,
			TypeKind.LONG
	);

	PrimitiveProcessor() {}

	public abstract Class<? extends Annotation> getKind();

	public abstract void addToTree(Element el, Tree parseTree, String accessor);

	public Result<Empty, AnnotationProcessorError> process(
			Tree parseTree,
			Element el,
			MetaUtils utils
	) {
		var res = isUsedCorrectlyOnPrimitive(el, utils);

		if (res.isEmpty()) {
			var accessor = getAccessor(el, utils);

			if (accessor.isOk()) {
				addToTree(el, parseTree, accessor.unwrap());
				return new Ok<>(Empty.UNIT);
			}
			return new Err<>(accessor.unwrapErr());
		}
		return new Err<>(res.get());
	}

	@Override
	public Set<TypeKind> supportedTypes() {
		return PRIMITIVE_TYPES;
	}

	final Optional<AnnotationProcessorError> genericIsUsedCorrectly(
			Element el,
			MetaUtils utils
	) {
		var typeMirror = el.asType();
		var logger = utils.logger();
		var supportedTypes = supportedTypes();
		var typeUtils = utils.typeUtils();

		var annotation = el.getAnnotation(getKind());
		if (annotation == null) {
			return Optional.of(AnnotationProcessorError.NOT_FOUND);
		}

		var kind = typeMirror.getKind();
		if (!supportedTypes.contains(kind)) {
			// handle the case when it is a wrapper class e.g. =Integer=
			try {
				var unboxedType = typeUtils.unboxedType(typeMirror);
				if (!supportedTypes.contains(unboxedType.getKind())) {
					return Optional.of(AnnotationProcessorError.NOT_PRIMITIVE);
				}
			}
			catch (IllegalArgumentException ex) {
				logger.error(String.format("Type %s is not supported", typeMirror), el);
				return Optional.of(AnnotationProcessorError.NOT_ACCESSIBLE);
			}
		}
		return Optional.empty();
	}

	Optional<AnnotationProcessorError> isUsedCorrectlyOnPrimitive(Element el, MetaUtils utils) {
		return genericIsUsedCorrectly(el, utils);
	}
}
