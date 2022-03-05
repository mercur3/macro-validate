package com.gitlab.mercur3.macro_validate;

import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

class SourceCodeGenerator {
	private final ParseTree parseTree;
	private final Filer filer;
	private final Elements elementUtil;

	public static SourceCodeGenerator from(ParseTree tree, Filer writer, Elements utils) {
		return new SourceCodeGenerator(tree, writer, utils);
	}

	public void generate() {
		var el = parseTree.element;
		var ptr = "ptr";
		var elType = el.asType();
		var classTypeName = ClassName.get(elType);

		var constructor = MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PRIVATE)
				.addParameter(classTypeName, ptr)
				.addStatement("this.$N = $N", ptr, ptr)
				.build();

		var initializer = MethodSpec.methodBuilder("of")
				.returns(classTypeName)
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.addParameter(classTypeName, ptr)
				.addStatement("return this.$N", ptr)
				.build();

		var classSpec = TypeSpec.classBuilder(generateName(el))
				.addSuperinterface(ParameterizedTypeName.get(
						ClassName.get(Validator.class),
						classTypeName
				))
				.addMethod(constructor)
				.addMethod(initializer)
				.build();

		var file = JavaFile.builder(generatePackage(el), classSpec)
				.indent("\t")
				.build();
		try {
			file.writeTo(System.out);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}


	/// PRIVATE


	private SourceCodeGenerator(ParseTree parseTree, Filer filer, Elements elementUtil) {
		this.parseTree = parseTree;
		this.filer = filer;
		this.elementUtil = elementUtil;
	}

	private String generateName(Element el) {
		return el.getSimpleName().toString() + "Validator";
	}

	private String generatePackage(Element el) {
		return elementUtil.getPackageOf(el).getQualifiedName().toString();
	}
}
