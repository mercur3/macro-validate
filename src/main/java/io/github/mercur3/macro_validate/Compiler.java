package io.github.mercur3.macro_validate;

import io.github.mercur3.macro_validate.constraints.Valid;
import com.google.auto.service.AutoService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AutoService(Compiler.class)
class Compiler extends AbstractProcessor {
	private MetaUtils utils;

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return Set.of(Valid.class.getCanonicalName());
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.RELEASE_25;
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

		for (var el : env.getElementsAnnotatedWith(Valid.class)) {
			logger.log("Processing annotations for element:", el);

			var parseTree = ParseTree.from(el, utils);
			var parseResult = parseTree.generate();
			if (parseResult.isErr()) {
				logger.error("Compilation failed on element:", el);
				return true;
			}

			var generateResult = SourceCodeGenerator
					.from(parseTree, utils)
					.generate();
			if (generateResult.isErr()) {
				logger.error("Compilation failed on element:", el);
				return true;
			}
			else {
				logger.log("Compilation succeed:", el);
			}
		}
		return false;
	}
}
