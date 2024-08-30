package axl.compiler.analysis.syntax.utils;

import axl.compiler.analysis.syntax.ast.Node;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SubAnalyzer {

    Class<? extends Node> target();

    Class<? extends Node>[] allowed() default {Node.class};

}
