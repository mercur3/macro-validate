package com.gitlab.mercur3.macro_validate;

import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;

class Compiler_Test {
	private static final String simpleTestClass = """
			package example;
			
			import com.gitlab.mercur3.macro_validate.constraints.Min;
			import com.gitlab.mercur3.macro_validate.constraints.Valid;
			
			@Valid
			class SimpleTestClass {
				@Min(value = 123, message = "Must be >= 123")
				public int num;
			}
			""";

	@Test
	void it_compiles() {
		var compilation = Compiler.javac()
				.withProcessors(new com.gitlab.mercur3.macro_validate.Compiler())
				.compile(JavaFileObjects.forSourceString(
						"example.SimpleTestClass",
						simpleTestClass
				));
		assertThat(compilation).succeededWithoutWarnings();
	}
}