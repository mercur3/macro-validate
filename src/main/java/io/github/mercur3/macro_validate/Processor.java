package io.github.mercur3.macro_validate;

import io.github.mercur3.jrusty.result.Empty;
import io.github.mercur3.jrusty.result.Err;
import io.github.mercur3.jrusty.result.Ok;
import io.github.mercur3.jrusty.result.Result;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import java.util.Set;

interface Processor {
	Set<TypeKind> supportedTypes();

	Result<Empty, AnnotationProcessorError> process(Tree parseTree, Element el, MetaUtils utils);

	default Result<String, AnnotationProcessorError> getAccessor(Element el, MetaUtils utils) {
		String accessor;
		if (el.getModifiers().contains(Modifier.PUBLIC)) {
			accessor = ElementWithAccessor.publicField(el);
		}
		else if (el.getEnclosingElement().getKind() == ElementKind.RECORD) {
			accessor = ElementWithAccessor.recordField(el);
		}
		else {
			utils.logger().error("Annotation type not applicable to this kind of declaration. Given field is neither public nor a record component:", el);
			return new Err<>(AnnotationProcessorError.NOT_ACCESSIBLE);
		}

		return new Ok<>(accessor);
	}

	// TODO Constraint getConstraint(Element el)
}
