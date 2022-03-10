package com.gitlab.mercur3.macro_validate;

import com.gitlab.mercur3.jrusty.result.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import java.lang.annotation.Annotation;
import java.util.Set;

abstract sealed class PrimitiveProcessor implements Processor
	permits MinProcessor, MaxProcessor
{
	private static final Set<TypeKind> SUPPORTED_TYPES = Set.of(
			TypeKind.BYTE,
			TypeKind.SHORT,
			TypeKind.INT,
			TypeKind.LONG
	);

	PrimitiveProcessor() {}

	public abstract Class<? extends Annotation> getKind();

	public abstract void addToTree(Element el, Tree parseTree, String accessor);

	@Override
	public Set<TypeKind> supportedTypes() {
		return SUPPORTED_TYPES;
	}

	@Override
	public Result<Empty, ErrorKind> process(Tree parseTree, Element el, MetaUtils utils) {
		var res = isUsedCorrectly(el, utils);
		if (res.isErr()) {
			return new Err<>(res.err().get());
		}

		addToTree(el, parseTree, res.unwrap());
		return new Ok<>(Empty.UNIT);
	}

	final Result<String, ErrorKind> genericIsUsedCorrectly(Element el, MetaUtils utils) {
		var typeMirror = el.asType();
		var logger = utils.logger();
		var supportedTypes = supportedTypes();
		var typeUtils = utils.typeUtils();

		var annotation = el.getAnnotation(getKind());
		if (annotation == null) {
			return new Err<>(ErrorKind.NOT_FOUND);
		}

		String accessor;
		if (el.getModifiers().contains(Modifier.PUBLIC)) {
			accessor = ElementWithAccessor.publicField(el);
		}
		else if (el.getEnclosingElement().getKind() == ElementKind.RECORD) {
			accessor = ElementWithAccessor.recordField(el);
		}
		else {
			logger.error("Annotation type not applicable to this kind of declaration. Given field is neither public nor a record component:", el);
			return new Err<>(ErrorKind.ILLEGAL_STATE);
		}

		var kind = typeMirror.getKind();
		if (!supportedTypes.contains(kind)) {
			// handle the case when it is a wrapper class e.g. =Integer=
			try {
				var unboxedType = typeUtils.unboxedType(typeMirror);
				if (!supportedTypes.contains(unboxedType.getKind())) {
					return new Err<>(ErrorKind.ILLEGAL_STATE);
				}
			}
			catch (IllegalArgumentException ex) {
				logger.error(String.format("Type %s is not supported", typeMirror), el);
				return new Err<>(ErrorKind.ILLEGAL_STATE);
			}
		}
		return new Ok<>(accessor);
	}

	Result<String, ErrorKind> isUsedCorrectly(Element el, MetaUtils utils) {
		return genericIsUsedCorrectly(el, utils);
	}
}
