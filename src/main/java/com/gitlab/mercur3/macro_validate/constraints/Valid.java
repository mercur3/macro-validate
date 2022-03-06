package com.gitlab.mercur3.macro_validate.constraints;

import java.lang.annotation.*;

/**
 * <p>
 * A marker annotation for the annotation processor to detect which class to construct a
 * {@link com.gitlab.mercur3.macro_validate.Validator} for. The annotation processor will produce
 * the following pattern:
 * </p>
 * <pre>
 *     public final class BeanNameValidator implements Validator&lt;BeanName&gt; {
 *         private final BeanName ptr;
 *
 *         private BeanNameValidator(BeanName ptr) {
 *             this.ptr = ptr;
 *         }
 *
 *         public static BeanNameValidator of(BeanName ptr) {
 *             return new BeanNameValidator(ptr);
 *         }
 *
 *         // Implementation of Validator methods
 *
 *     }
 * </pre>
 *
 * @see com.gitlab.mercur3.macro_validate.Validator
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Valid {}