package com.gitlab.mercur3.macro_validate;

import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;

class Compiler_Test {
	private static final Compiler compiler = Compiler.javac()
			.withProcessors(new com.gitlab.mercur3.macro_validate.Compiler());
	private static final String simpleCorrectTestClass = """
			package example;

			import com.gitlab.mercur3.macro_validate.constraints.Min;
			import com.gitlab.mercur3.macro_validate.constraints.Valid;

			@Valid
			class SimpleCorrectTestClass {
				@Min(value = 123, message = "Must be >= 123")
				public int num;
			}
			""";
	private static final String simpleIncorrectTestClass = """
			package example;

			import com.gitlab.mercur3.macro_validate.constraints.Min;
			import com.gitlab.mercur3.macro_validate.constraints.Valid;

			@Min(value = 123, message = "Must be >= 123")
			class SimpleIncorrectTestClass {
				public int num;
			}
			""";

	@Test
	void it_compiles() {
		var compilation = compiler.compile(JavaFileObjects.forSourceString(
				"example.SimpleCorrectTestClass",
				simpleCorrectTestClass
		));
		assertThat(compilation).succeededWithoutWarnings();
	}

	@Test
	void it_does_not_compile() {
		var compilation = compiler.compile(JavaFileObjects.forSourceString(
				"example.SimpleIncorrectTestClass",
				simpleIncorrectTestClass
		));

		assertThat(compilation).failed();
		assertThat(compilation).hadErrorContaining(
				"annotation type not applicable to this kind of declaration");
	}
}