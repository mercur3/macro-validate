package com.gitlab.mercur3.macro_validate;

public class OutputFiles {
	public static String SIMPLE_CORRECT_TEST_CLASS = """
			package example;

			import com.gitlab.mercur3.macro_validate.Validator;
			import java.lang.Override;
			import java.lang.String;
			import java.util.ArrayList;

			class SimpleCorrectTestClassValidator implements Validator<SimpleCorrectTestClass> {
				private final SimpleCorrectTestClass ptr;

				private SimpleCorrectTestClassValidator(SimpleCorrectTestClass ptr) {
					this.ptr = ptr;
				}

				public static SimpleCorrectTestClassValidator of(SimpleCorrectTestClass ptr) {
					return new SimpleCorrectTestClassValidator(ptr);
				}

				@Override
				public boolean valid() {
					return this.ptr.num1 >= 1 && this.ptr.num2 >= 2 && this.ptr.num3 >= 3 && this.ptr.num4 >= 4 && this.ptr.num5 >= 5 && this.ptr.num6 >= 6 && this.ptr.num7 >= 7 && this.ptr.num8 >= 8;
				}

				@Override
				public ArrayList<String> errors() {
					var errors = new ArrayList<String>(8);
					if (!(ptr.num1 >= 1)) {
						errors.add("Must be >= 1");
					}
					if (!(ptr.num2 >= 2)) {
						errors.add("Must be >= 2");
					}
					if (!(ptr.num3 >= 3)) {
						errors.add("Must be >= 3");
					}
					if (!(ptr.num4 >= 4)) {
						errors.add("Must be >= 4");
					}
					if (!(ptr.num5 >= 5)) {
						errors.add("Must be >= 5");
					}
					if (!(ptr.num6 >= 6)) {
						errors.add("Must be >= 6");
					}
					if (!(ptr.num7 >= 7)) {
						errors.add("Must be >= 7");
					}
					if (!(ptr.num8 >= 8)) {
						errors.add("Must be >= 8");
					}
					return errors;
				}
			}
			""";

	public static String TRIVIAL_VALID_CLASS = """
			package example;

			import com.gitlab.mercur3.macro_validate.Validator;
			import java.lang.Override;
			import java.lang.String;
			import java.util.ArrayList;

			class TrivialValidClassValidator implements Validator<TrivialValidClass> {
				private final TrivialValidClass ptr;

				private TrivialValidClassValidator(TrivialValidClass ptr) {
					this.ptr = ptr;
				}

				public static TrivialValidClassValidator of(TrivialValidClass ptr) {
					return new TrivialValidClassValidator(ptr);
				}

				@Override
				public boolean valid() {
					return true;
				}

				@Override
				public ArrayList<String> errors() {
					var errors = new ArrayList<String>(0);
					return errors;
				}
			}
			""";

	public static String CORRECT_RECORD = """
			package example;

			import com.gitlab.mercur3.macro_validate.Validator;
			import java.lang.Override;
			import java.lang.String;
			import java.util.ArrayList;

			class CorrectRecordValidator implements Validator<CorrectRecord> {
				private final CorrectRecord ptr;

				private CorrectRecordValidator(CorrectRecord ptr) {
					this.ptr = ptr;
				}

				public static CorrectRecordValidator of(CorrectRecord ptr) {
					return new CorrectRecordValidator(ptr);
				}

				@Override
				public boolean valid() {
					return this.ptr.num1() >= 100 && this.ptr.num2() >= 100 && this.ptr.num2() <= 0;
				}

				@Override
				public ArrayList<String> errors() {
					var errors = new ArrayList<String>(2);
					if (!(ptr.num1() >= 100)) {
						errors.add("Must be >= 100");
					}
					if (!(ptr.num2() >= 100)) {
						errors.add("Must be >= 100");
					}
					if (!(ptr.num2() <= 0)) {
						errors.add("Must be <= 0");
					}
					return errors;
				}
			}
			""";
}
