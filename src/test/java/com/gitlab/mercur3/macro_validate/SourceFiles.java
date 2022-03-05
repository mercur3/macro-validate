package com.gitlab.mercur3.macro_validate;

public class SourceFiles {
	public static final String SIMPLE_CORRECT_TEST_CLASS = """
			package example;

			import com.gitlab.mercur3.macro_validate.constraints.Min;
			import com.gitlab.mercur3.macro_validate.constraints.Valid;

			@Valid
			class SimpleCorrectTestClass {
				@Min(value = 123, message = "Must be >= 123")
				public int num;
			}
			""";

	public static final String SIMPLE_INCORRECT_TEST_CLASS = """
			package example;

			import com.gitlab.mercur3.macro_validate.constraints.Min;
			import com.gitlab.mercur3.macro_validate.constraints.Valid;

			@Min(value = 123, message = "Must be >= 123")
			class SimpleIncorrectTestClass {
				public int num;
			}
			""";
}