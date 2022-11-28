package com.gitlab.mercur3.macro_validate;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Compiler_Test {
	private static final String ANNOTATION_TYPE_NOT_APPLICABLE_ERROR_MSG =
			"Annotation type not applicable to this kind of declaration";
	private static final String USAGE_OF_MIN_MAX =
			"Usage of Min.class in combination with Max.class.";
	private static final Path BASE_PATH = Path.of("src/test/java/com/gitlab/mercur3/macro_validate");

	private Compiler COMPILER;

	private Compilation compile(String sourceQualifiedName, String simpleClassName)
	throws
			IOException
	{
		var path = Path.of(BASE_PATH.toString(), "src/" + simpleClassName + ".java.txt");
		var content = Files.readString(path);

		return COMPILER.compile(JavaFileObjects.forSourceString(sourceQualifiedName, content));
	}

	private String outputFileContent(String simpleClassName) throws IOException {
		var path = Path.of(
				BASE_PATH.toString(),
				"out/" + simpleClassName + "Validator.java.txt"
		);
		return Files.readString(path);
	}

	@BeforeEach
	void init_compiler() {
		COMPILER = Compiler
				.javac()
				.withProcessors(new com.gitlab.mercur3.macro_validate.Compiler());
	}

	@Test
	void it_compiles() throws IOException {
		var className = "SimpleCorrectTestClass";
		var sourceQualifiedName = "example." + className;
		var outName = sourceQualifiedName + "Validator";

		var compilation = compile(sourceQualifiedName, className);
		var result = assertThat(compilation);

		result.succeededWithoutWarnings();
		result.generatedSourceFile(outName);

		var output = compilation
				.generatedSourceFile(outName)
				.get()
				.getCharContent(false);
		assertEquals(outputFileContent(className), output);
	}

	@Test
	void record_compiles() throws IOException {
		var className = "CorrectRecord";
		var sourceQualifiedName = "example." + className;
		var outputName = sourceQualifiedName + "Validator";

		var compilation = compile(sourceQualifiedName, className);
		var result = assertThat(compilation);

		result.succeeded();
		result.hadWarningContaining(USAGE_OF_MIN_MAX);
		result.generatedSourceFile(outputName);

		var output = compilation
				.generatedSourceFile(outputName)
				.get()
				.getCharContent(false);
		assertEquals(outputFileContent(className), output);
	}

	@Test
	void it_does_not_compile() throws IOException {
		var className = "SimpleIncorrectTestClass";
		var sourceQualifiedName = "example." + className;

		var compilation = compile(sourceQualifiedName, className);
		var result = assertThat(compilation);

		result.failed();
		result.hadErrorContaining(ANNOTATION_TYPE_NOT_APPLICABLE_ERROR_MSG.toLowerCase());
	}

	@Test
	void min_does_not_work_on_boolean() throws IOException {
		var className = "MinUsageOnBoolean";
		var sourceQualifiedName = "example." + className;

		var compilation = compile(sourceQualifiedName, className);
		var result = assertThat(compilation);

		result.failed();
		result.hadErrorContaining("not supported");
	}

	@Test
	void min_does_not_work_on_invalid_object() throws IOException {
		var className = "MinUsageOnInvalidObject";
		var sourceQualifiedName = "example." + className;

		var compilation = compile(sourceQualifiedName, className);
		var result = assertThat(compilation);

		result.failed();
		result.hadErrorContaining("not supported");
	}

	@Test
	void repeated_annotations_are_not_allowed() throws IOException {
		var className = "RepeatedAnnotation";
		var sourceQualifiedName = "example." + className;

		var compilation = compile(sourceQualifiedName, className);
		var result = assertThat(compilation);

		result.failed();
		result.hadErrorContaining("Min is not a repeatable annotation type");
	}

	@Test
	void trivial_beans_are_always_valid() throws IOException {
		var className = "TrivialValidClass";
		var sourceQualifiedName = "example." + className;
		var outputName = sourceQualifiedName + "Validator";

		var compilation = compile(sourceQualifiedName, className);
		var result = assertThat(compilation);

		result.generatedSourceFile(outputName);

		var output = compilation
				.generatedSourceFile(outputName)
				.get()
				.getCharContent(false);
		assertEquals(outputFileContent(className), output);
	}
}