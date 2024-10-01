package axl.compiler.analysis.syntax.utils;

import axl.compiler.analysis.syntax.state.State;

public interface AnalyzerEntry {

    void accept(State state);
}
