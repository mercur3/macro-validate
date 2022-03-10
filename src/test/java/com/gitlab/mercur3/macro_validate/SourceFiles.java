package com.gitlab.mercur3.macro_validate;

public class SourceFiles {
	public static final String SIMPLE_CORRECT_TEST_CLASS = """
			package example;

			import com.gitlab.mercur3.macro_validate.constraints.Min;
			import com.gitlab.mercur3.macro_validate.constraints.Valid;

			@Valid
			class SimpleCorrectTestClass {
				@Min(value = 1, message = "Must be >= 1")
				public byte num1;

				@Min(value = 2, message = "Must be >= 2")
				public short num2;

				@Min(value = 3, message = "Must be >= 3")
				public int num3;

				@Min(value = 4, message = "Must be >= 4")
				public long num4;

				@Min(value = 5, message = "Must be >= 5")
				public Byte num5;

				@Min(value = 6, message = "Must be >= 6")
				public Short num6;

				@Min(value = 7, message = "Must be >= 7")
				public Integer num7;

				@Min(value = 8, message = "Must be >= 8")
				public Long num8;
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

	public static final String CORRECT_RECORD = """
			package example;

			import com.gitlab.mercur3.macro_validate.constraints.Min;
			import com.gitlab.mercur3.macro_validate.constraints.Max;
			import com.gitlab.mercur3.macro_validate.constraints.Valid;

			@Valid
			record CorrectRecord(
				@Min(value = 100, message = "Must be >= 100")
				int num1,

				@Min(value = 100, message = "Must be >= 100")
				@Max(value = 0, message = "Must be <= 0")
				int num2
			) {}
			""";

	public static final String MIN_USAGE_ON_BOOLEAN = """
			package example;

			import com.gitlab.mercur3.macro_validate.constraints.Min;
			import com.gitlab.mercur3.macro_validate.constraints.Valid;

			@Valid
			class MinUsageOnBoolean {
				@Min(value = 123, message = "Must be >= 123")
				public boolean flag;
			}
			""";

	public static final String MIN_USAGE_ON_INVALID_OBJECT = """
			package example;

			import com.gitlab.mercur3.macro_validate.constraints.Min;
			import com.gitlab.mercur3.macro_validate.constraints.Valid;

			@Valid
			class MinUsageOnInvalidObject  {
				@Min(value = 123, message = "Must be >= 123")
				public String str;
			}
			""";

	public static final String REPEATED_ANNOTATION = """
			package example;

			import com.gitlab.mercur3.macro_validate.constraints.Min;
			import com.gitlab.mercur3.macro_validate.constraints.Valid;

			@Valid
			class RepeatedAnnotation {
				@Min(value = 1, message = "Must be >= 1")
				@Min(value = 2, message = "Must be >= 2")
				public byte num1;
			}
			""";

	public static final String TRIVIAL_VALID_CLASS = """
			package example;

			import com.gitlab.mercur3.macro_validate.constraints.Min;
			import com.gitlab.mercur3.macro_validate.constraints.Valid;

			@Valid
			class TrivialValidClass {
				public int num;
				public String str;
			}
			""";
}