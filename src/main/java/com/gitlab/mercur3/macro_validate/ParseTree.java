package com.gitlab.mercur3.macro_validate;

import com.gitlab.mercur3.jrusty.result.Empty;
import com.gitlab.mercur3.jrusty.result.Ok;
import com.gitlab.mercur3.jrusty.result.Result;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

class ParseTree {
	public final Element element;
	public final Tree tree;
	public final MetaUtils utils;
	private static final Processor processors[] = {new MinProcessor(), new MaxProcessor()};

	public static ParseTree from(Element el, MetaUtils utils) {
		return new ParseTree(el, utils);
	}

	public Result<Empty, AnnotationProcessorError> generate() {
		var classMembers = element.getEnclosedElements()
				.stream()
				.filter(el -> el.getKind() == ElementKind.FIELD)
				.toList();
		for (var el : classMembers) {
			for (var processor : processors) {
				var res = processor.process(tree, el, utils);
				if (res.isErr() && res.err().get() != AnnotationProcessorError.NOT_FOUND) {
					return res;
				}
			}
		}

		return new Ok<>(Empty.UNIT);
	}

	/// PRIVATE

	private ParseTree(Element element, MetaUtils utils) {
		this.element = element;
		this.utils = utils;
		this.tree = new Tree();
	}
}