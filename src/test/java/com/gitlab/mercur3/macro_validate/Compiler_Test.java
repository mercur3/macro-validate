package com.gitlab.mercur3.macro_validate;

import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;

class Compiler_Test {
	private static final Compiler COMPILER = Compiler.javac()
			.withProcessors(new com.gitlab.mercur3.macro_validate.Compiler());

	private static final String ANNOTATION_TYPE_NOT_APPLICABLE_ERROR_MSG =
			"annotation type not applicable to this kind of declaration";

	private static final String SIMPLE_CORRECT_TEST_CLASS = """
			package example;

			import com.gitlab.mercur3.macro_validate.constraints.Min;
			import com.gitlab.mercur3.macro_validate.constraints.Valid;

			@Valid
			class SimpleCorrectTestClass {
				@Min(value = 123, message = "Must be >= 123")
				public int num;
			}
			""";
	private static final String SIMPLE_INCORRECT_TEST_CLASS = """
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
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				"example.SimpleCorrectTestClass",
				SIMPLE_CORRECT_TEST_CLASS
		));
		assertThat(compilation).succeededWithoutWarnings();
	}

	@Test
	void it_does_not_compile() {
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				"example.SimpleIncorrectTestClass",
				SIMPLE_INCORRECT_TEST_CLASS
		));
		var compilationResult = assertThat(compilation);

		compilationResult.failed();
		compilationResult.hadErrorContaining(ANNOTATION_TYPE_NOT_APPLICABLE_ERROR_MSG);
	}
}