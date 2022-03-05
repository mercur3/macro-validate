package com.gitlab.mercur3.macro_validate;

public class SourceFiles {
	public static final String SIMPLE_CORRECT_TEST_CLASS = """
			package example;

			import com.gitlab.mercur3.macro_validate.constraints.Min;
			import com.gitlab.mercur3.macro_validate.constraints.Valid;

			@Valid
			class SimpleCorrectTestClass {
				@Min(value = 123, message = "Must be >= 123")
				public byte num1;

				@Min(value = 456, message = "Must be >= 456")
				public short num2;

				@Min(value = 456, message = "Must be >= 456")
				public int num3;

				@Min(value = 456, message = "Must be >= 456")
				public long num4;

				@Min(value = 456, message = "Must be >= 456")
				public Byte num5;

				@Min(value = 456, message = "Must be >= 456")
				public Short num6;

				@Min(value = 456, message = "Must be >= 456")
				public Integer num7;

				@Min(value = 456, message = "Must be >= 456")
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
			import com.gitlab.mercur3.macro_validate.constraints.Valid;

			record CorrectRecord(
				@Min(value = 123, message = "Must be >= 123")
				int num
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
}