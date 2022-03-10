package com.gitlab.mercur3.macro_validate;

import javax.annotation.processing.Filer;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public record MetaUtils(Types typeUtils, Elements elementUtils, Filer filer, Logger logger) {}
