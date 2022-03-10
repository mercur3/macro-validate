package com.gitlab.mercur3.macro_validate;

import com.gitlab.mercur3.jrusty.result.Empty;
import com.gitlab.mercur3.jrusty.result.ErrorKind;
import com.gitlab.mercur3.jrusty.result.Result;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import java.util.Set;

interface Processor {
	Set<TypeKind> supportedTypes();

	Result<Empty, ErrorKind> process(Tree parseTree, Element el, MetaUtils utils);
}