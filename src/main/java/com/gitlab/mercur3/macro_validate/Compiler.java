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
	private Types typeUtils;
	private Elements elementUtils;
	private Filer filer;
	private Logger logger;

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
		this.typeUtils = env.getTypeUtils();
		this.elementUtils = env.getElementUtils();
		this.filer = env.getFiler();
		this.logger = Logger.from(env.getMessager());
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		for (var el : env.getElementsAnnotatedWith(Valid.class)) {
			logger.log("Got element", el);
			var parseTree = ParseTree.from(el, logger, typeUtils);
			var result = parseTree.generate();
			if (result.isErr()) {
				return true;
			}
		}

		/// FIXME should return =false=
		return true;
	}
}