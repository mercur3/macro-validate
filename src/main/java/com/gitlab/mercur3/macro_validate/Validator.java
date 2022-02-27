package com.gitlab.mercur3.macro_validate;

import java.util.ArrayList;

/**
 * Bean validator.
 * <p>
 * If the bean is annotated with TODO, no implementation is necessary since it will provided
 * at compile by the annotation processor. Each implementation will follow these pattern:
 * </p>
 * <pre>
 *     public final class BeanNameValidator implements Validator<BeanName> {
 *         private final BeanName bean;
 *
 *         private BeanNameValidator(BeanName bean) {
 *             this.bean = bean;
 *         }
 *
 *         public static BeanNameValidator of(BeanName bean) {
 *             return new BeanNameValidator(bean);
 *         }
 *
 *         // Implementation of Validator methods
 *
 *     }
 * </pre>
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