package io.github.mercur3.macro_validate;

import io.github.mercur3.macro_validate.constraints.Valid;

import java.util.ArrayList;

/**
 * Bean validator.
 * <p>
 * If the bean is annotated with {@code @Valid}, no implementation is necessary since it will
 * provided at compile by the annotation processor. Each implementation will follow the pattern
 * described at {@link Valid}.
 * </p>
 *
 * @param <T> the bean type
 */
public interface Validator<T> {
	/**
	 * @return {@code true} if and only if every field annotated with an annotation from
	 * {@link io.github.mercur3.macro_validate.constraints} fulfills that constraint.
	 */
	boolean valid();

	/**
	 * @return a list of every constraint violation of this specific bean.
	 */
	ArrayList<String> errors();
}
