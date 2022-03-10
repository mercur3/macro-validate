package com.gitlab.mercur3.macro_validate;

import com.gitlab.mercur3.macro_validate.constraints.Valid;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Set;

@AutoService(Processor.class)
class Compiler extends AbstractProcessor {
	private MetaUtils utils;

	public Compiler() {
		super();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Set.of(Valid.class.getCanonicalName());
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.RELEASE_17;
	}

	@Override
	public synchronized void init(ProcessingEnvironment env) {
		super.init(env);
		this.utils = new MetaUtils(
				env.getTypeUtils(),
				env.getElementUtils(),
				env.getFiler(),
				Logger.from(env.getMessager())
		);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		var logger = utils.logger();
		var filer = utils.filer();
		var elementUtils = utils.elementUtils();

		for (var el : env.getElementsAnnotatedWith(Valid.class)) {
			logger.log("Processing annotations for element:", el);
			var parseTree = ParseTree.from(el, utils);
			var parseResult = parseTree.generate();
			if (parseResult.isErr()) {
				return true;
			}

			var generateResult = SourceCodeGenerator.from(parseTree, filer, elementUtils)
					.generate();
			if (generateResult.isErr()) {
				return true;
			}
		}
		return false;
	}
}