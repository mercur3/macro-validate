package com.gitlab.mercur3.macro_validate;

import com.gitlab.mercur3.jrusty.result.Empty;
import com.gitlab.mercur3.jrusty.result.Err;
import com.gitlab.mercur3.jrusty.result.Ok;
import com.gitlab.mercur3.jrusty.result.Result;
import com.squareup.javapoet.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

class SourceCodeGenerator {
	private final ParseTree parseTree;
	private final Filer filer;
	private final Elements elementUtil;

	public static SourceCodeGenerator from(ParseTree tree, Filer writer, Elements utils) {
		return new SourceCodeGenerator(tree, writer, utils);
	}

	public Result<Empty, Empty> generate() {
		var el = parseTree.element;
		var ptr = "ptr";
		var elType = el.asType();
		var thisClassName = ClassName.get(generatePackage(el), generateName(el));
		var targetClassName = ClassName.get(elType);

		var constructor = MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PRIVATE)
				.addParameter(targetClassName, ptr)
				.addStatement("this.$N = $N", ptr, ptr)
				.build();

		var initializer = MethodSpec.methodBuilder("of")
				.returns(thisClassName)
				.addModifiers(Modifier.PUBLIC, Modifier.STATIC)
				.addParameter(targetClassName, ptr)
				.addStatement("return new $T($N)", thisClassName, ptr)
				.build();

		var overrideValid = overrideValid();
		var overrideErrors = overrideErrors();

		var classBuilder = TypeSpec.classBuilder(thisClassName)
				.addSuperinterface(ParameterizedTypeName.get(
						ClassName.get(Validator.class),
						targetClassName
				))
				.addMethod(constructor)
				.addMethod(initializer)
				.addMethod(overrideValid)
				.addMethod(overrideErrors)
				.addField(targetClassName, ptr, Modifier.PRIVATE, Modifier.FINAL);


		var file = JavaFile.builder(generatePackage(el), classBuilder.build())
				.indent("\t")
				.build();
		try {
			file.writeTo(filer);
		}
		catch (IOException e) {
			e.printStackTrace();
			return new Err<>(Empty.UNIT);
		}
		return new Ok<>(Empty.UNIT);
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

	private MethodSpec overrideValid() {
		var overrideValid = MethodSpec.methodBuilder("valid")
				.addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC)
				.returns(boolean.class);

		StringBuilder acc = new StringBuilder("return ");
		var entries = parseTree.tree.nodes.entrySet();
		if (entries.size() == 0) {
			acc.append("true");
		}
		else {
			var iter = entries.iterator();
			var first = iter.next();
			display(acc, first);
			iter.forEachRemaining(el -> {
				acc.append(" && ");
				display(acc, el);
			});
		}
		overrideValid.addStatement(acc.toString());
		return overrideValid.build();
	}

	private void display(
			StringBuilder acc,
			Map.Entry<ElementWithAccessor, ArrayList<Constraint>> field
	) {
		acc.append("this.ptr");
		acc.append(field.getKey().accessor);
		acc.append(' ');
		var constraints = field.getValue();
		acc.append(constraints.get(0).statement());
		for (int i = 1; i < constraints.size(); ++i) {
			acc.append("this.ptr");
			acc.append(field.getKey().accessor);
			acc.append(" && ");
			acc.append(constraints.get(i).statement());
		}
	}

	private MethodSpec overrideErrors() {
		var entries = parseTree.tree.nodes.entrySet();
		var builder = MethodSpec.methodBuilder("errors")
				.addAnnotation(Override.class)
				.returns(ParameterizedTypeName.get(ArrayList.class, String.class))
				.addModifiers(Modifier.PUBLIC)
				.addStatement(String.format(
						"var errors = new ArrayList<String>(%d)",
						entries.size())
				);
		for (var el : entries) {
			var accessor = el.getKey().accessor;
			for (var constraint : el.getValue()) {
				builder = builder.beginControlFlow(
						"if (!(ptr$N $N))",
								accessor,
								constraint.statement()
						)
						.addStatement("errors.add($S)", constraint.errMsg())
						.endControlFlow();
			}
		}
		return builder.addStatement("return errors")
				.build();
	}
}