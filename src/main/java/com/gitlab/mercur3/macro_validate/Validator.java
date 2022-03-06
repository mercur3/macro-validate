package com.gitlab.mercur3.macro_validate;

import java.util.ArrayList;

/**
 * Bean validator.
 * <p>
 * If the bean is annotated with {@code @Valid}, no implementation is necessary since it will
 * provided at compile by the annotation processor. Each implementation will follow the pattern
 * described at {@link com.gitlab.mercur3.macro_validate.constraints.Valid}.
 * </p>
 *
 * @param <T> the bean type
 */
public interface Validator<T> {
	/**
	 * @return {@code true} if and only if every field annotated with an annotation from
	 * {@link com.gitlab.mercur3.macro_validate.constraints} fulfills that constraint.
	 */
	boolean valid();

	/**
	 * @return a list of every constraint violation of this specific bean.
	 */
	ArrayList<String> errors();
}