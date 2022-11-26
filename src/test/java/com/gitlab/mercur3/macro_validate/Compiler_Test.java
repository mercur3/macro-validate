package com.gitlab.mercur3.macro_validate;

import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

import static com.google.testing.compile.CompilationSubject.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class Compiler_Test {
	private static final String ANNOTATION_TYPE_NOT_APPLICABLE_ERROR_MSG =
			"Annotation type not applicable to this kind of declaration";
	private static final String USAGE_OF_MIN_MAX =
			"Usage of Min.class in combination with Max.class.";

	private Compiler COMPILER;

	@BeforeEach
	void init_compiler() {
		COMPILER = Compiler
				.javac()
				.withProcessors(new com.gitlab.mercur3.macro_validate.Compiler());
	}

	@Test
	void it_compiles() throws IOException {
		var sourceName = "example.SimpleCorrectTestClass";
		var outName = sourceName + "Validator";
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				sourceName,
				SourceFiles.SIMPLE_CORRECT_TEST_CLASS
		));
		var result = assertThat(compilation);

		result.succeededWithoutWarnings();
		result.generatedSourceFile(outName);

		var output = compilation
				.generatedSourceFile(outName)
				.get()
				.getCharContent(false);
		Assertions.assertTrue(OutputFiles.SIMPLE_CORRECT_TEST_CLASS.contentEquals(output));
	}

	@Test
	void record_compiles() throws IOException {
		var sourceName = "example.CorrectRecord";
		var outputName = sourceName + "Validator";
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				sourceName,
				SourceFiles.CORRECT_RECORD
		));
		var result = assertThat(compilation);

		result.succeeded();
		result.hadWarningContaining(USAGE_OF_MIN_MAX);
		result.generatedSourceFile(outputName);

		var output = compilation
				.generatedSourceFile(outputName)
				.get()
				.getCharContent(false);
		Assertions.assertTrue(OutputFiles.CORRECT_RECORD.contentEquals(output));
	}

	@Test
	void it_does_not_compile() {
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				"example.SimpleIncorrectTestClass",
				SourceFiles.SIMPLE_INCORRECT_TEST_CLASS
		));
		var compilationResult = assertThat(compilation);

		compilationResult.failed();
		compilationResult.hadErrorContaining(ANNOTATION_TYPE_NOT_APPLICABLE_ERROR_MSG.toLowerCase());
	}

	@Test
	void min_does_not_work_on_boolean() {
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				"example.MinUsageOnBoolean",
				SourceFiles.MIN_USAGE_ON_BOOLEAN
		));
		var result = assertThat(compilation);

		result.failed();
		result.hadErrorContaining("not supported");
	}

	@Test
	void min_does_not_work_on_invalid_object() {
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				"example.MinUsageOnInvalidObject",
				SourceFiles.MIN_USAGE_ON_INVALID_OBJECT
		));
		var result = assertThat(compilation);

		result.failed();
		result.hadErrorContaining("not supported");
	}

	@Test
	void repeated_annotations_are_not_allowed() {
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				"example.RepeatedAnnotation",
				SourceFiles.REPEATED_ANNOTATION
		));
		var result = assertThat(compilation);

		result.failed();
		result.hadErrorContaining("Min is not a repeatable annotation type");
	}

	@Test
	void trivial_beans_are_always_valid() throws IOException {
		var sourceName = "example.TrivialValidClass";
		var outName = sourceName + "Validator";
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				sourceName,
				SourceFiles.TRIVIAL_VALID_CLASS
		));
		var result = assertThat(compilation);

		result.succeededWithoutWarnings();
		result.generatedSourceFile(outName);

		var output = compilation
				.generatedSourceFile(outName)
				.get()
				.getCharContent(false);
		Assertions.assertTrue(OutputFiles.TRIVIAL_VALID_CLASS.contentEquals(output));
	}
}