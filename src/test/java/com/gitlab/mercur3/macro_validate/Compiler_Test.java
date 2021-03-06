package com.gitlab.mercur3.macro_validate;

import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

import static com.gitlab.mercur3.macro_validate.SourceFiles.*;
import static com.google.testing.compile.CompilationSubject.assertThat;

class Compiler_Test {
	private static final String ANNOTATION_TYPE_NOT_APPLICABLE_ERROR_MSG =
			"Annotation type not applicable to this kind of declaration";
	private static final String USAGE_OF_MIN_MAX =
			"Usage of Min.class in combination with Max.class.";

	private Compiler COMPILER;

	@BeforeEach
	void init_compiler() {
		COMPILER = Compiler.javac()
				.withProcessors(new com.gitlab.mercur3.macro_validate.Compiler());
	}

	@Test
	void it_compiles() {
		var sourceName = "example.SimpleCorrectTestClass";
		var outName = sourceName + "Validator";
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				sourceName,
				SIMPLE_CORRECT_TEST_CLASS
		));
		var result = assertThat(compilation);

		result.succeededWithoutWarnings();
		result.generatedSourceFile(outName);
		try {
			var output = compilation.generatedSourceFile(outName)
					.get()
					.getCharContent(false);
			Assertions.assertTrue(OutputFiles.SIMPLE_CORRECT_TEST_CLASS.contentEquals(output));
		}
		catch (IOException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}

	@Test
	void record_compiles() {
		var sourceName = "example.CorrectRecord";
		var outputName = sourceName + "Validator";
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				sourceName,
				CORRECT_RECORD
		));
		var result = assertThat(compilation);

		result.succeeded();
		result.hadWarningContaining(USAGE_OF_MIN_MAX);
		result.generatedSourceFile(outputName);

		try {
			var output = compilation.generatedSourceFile(outputName)
					.get()
					.getCharContent(false);
			Assertions.assertTrue(OutputFiles.CORRECT_RECORD.contentEquals(output));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void it_does_not_compile() {
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				"example.SimpleIncorrectTestClass",
				SIMPLE_INCORRECT_TEST_CLASS
		));
		var compilationResult = assertThat(compilation);

		compilationResult.failed();
		compilationResult.hadErrorContaining(ANNOTATION_TYPE_NOT_APPLICABLE_ERROR_MSG.toLowerCase());
	}

	@Test
	void min_does_not_work_on_boolean() {
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				"example.MinUsageOnBoolean",
				MIN_USAGE_ON_BOOLEAN
		));
		var result = assertThat(compilation);

		result.failed();
		result.hadErrorContaining("not supported");
	}

	@Test
	void min_does_not_work_on_invalid_object() {
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				"example.MinUsageOnInvalidObject",
				MIN_USAGE_ON_INVALID_OBJECT
		));
		var result = assertThat(compilation);

		result.failed();
		result.hadErrorContaining("not supported");
	}

	@Test
	void repeated_annotations_are_not_allowed() {
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				"example.RepeatedAnnotation",
				REPEATED_ANNOTATION
		));
		var result = assertThat(compilation);

		result.failed();
		result.hadErrorContaining("Min is not a repeatable annotation type");
	}

	@Test
	void trivial_beans_are_always_valid() {
		var sourceName = "example.TrivialValidClass";
		var outName = sourceName + "Validator";
		var compilation = COMPILER.compile(JavaFileObjects.forSourceString(
				sourceName,
				TRIVIAL_VALID_CLASS
		));
		var result = assertThat(compilation);

		result.succeededWithoutWarnings();
		result.generatedSourceFile(outName);
		try {
			var output = compilation.generatedSourceFile(outName)
					.get()
					.getCharContent(false);
			Assertions.assertTrue(OutputFiles.TRIVIAL_VALID_CLASS.contentEquals(output));
		}
		catch (IOException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}
}