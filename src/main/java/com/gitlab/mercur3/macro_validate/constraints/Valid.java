package com.gitlab.mercur3.macro_validate.constraints;

import java.lang.annotation.*;

/**
 * A marker annotation for the annotation processor to detect which class to construct a
 * {@link com.gitlab.mercur3.macro_validate.Validator} for.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface Valid {}