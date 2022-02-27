package com.gitlab.mercur3.macro_validate.constraints;

import java.lang.annotation.*;

/**
 * Annotation used to check if a given value is <code>&ge;</code> {@link Min#value()}.
 * <h2>Supported types:</h2>
 * <ul>
 *     <li><b>Primitives:</b> byte, short, int, long.</li>
 *     <li><b>Primitive wrapper classes:</b> {@link Byte}, {@link Short}, {@link Integer},
 *     {@link Long}.</li>
 * </ul>
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface Min {
	/**
	 * The value we will be checking against. Defaults to {@link Long#MIN_VALUE}.
	 */
	long value() default Long.MIN_VALUE;

	/**
	 * A custom error message in case the field is not valid.
	 */
	String message();
}