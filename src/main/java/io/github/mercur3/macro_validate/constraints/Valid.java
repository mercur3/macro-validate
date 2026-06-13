package io.github.mercur3.macro_validate.constraints;

import io.github.mercur3.macro_validate.Validator;

import java.lang.annotation.*;

/**
 * <p>
 * A marker annotation for the annotation processor to detect which class to construct a
 * {@link Validator} for. The annotation processor will produce
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
 * @see Validator
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Valid {}
