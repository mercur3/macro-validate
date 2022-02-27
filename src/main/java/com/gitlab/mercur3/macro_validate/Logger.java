package com.gitlab.mercur3.macro_validate;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

class Logger {
	private final Messager messager;

	public static Logger from(Messager m) {
		return new Logger(m);
	}

	public void log(String msg, Element el) {
		messager.printMessage(Diagnostic.Kind.OTHER, msg, el);
	}

	public void note(String msg, Element el) {
		messager.printMessage(Diagnostic.Kind.NOTE, msg, el);
	}

	public void warning(String msg, Element el) {
		messager.printMessage(Diagnostic.Kind.WARNING, msg, el);
	}

	public void error(String msg, Element el) {
		messager.printMessage(Diagnostic.Kind.ERROR, msg, el);
	}

	/// PRIVATE

	private Logger(Messager messager) {
		this.messager = messager;
	}
}