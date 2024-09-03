package axl.compiler.analysis.syntax.utils;

import axl.compiler.analysis.lexical.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Analyzer {

    private List<Pattern> patterns;

    @Data
    @AllArgsConstructor
    public static class Pattern {
        private boolean required;
        private TokenType identifier;
        private List<Pattern> subPatterns;
    }

    @Builder
    public Analyzer(List<Pattern> patterns) {
        this.patterns = patterns;
    }

    public static class AnalyzerBuilder {
        private final List<Pattern> patterns = new ArrayList<>();

        public AnalyzerBuilder rule(boolean required, TokenType identifier) {
            patterns.add(new Pattern(required, identifier, null));
            return this;
        }

        public AnalyzerBuilder rule(boolean required, AnalyzerBuilder builder) {
            patterns.add(new Pattern(required, null, builder.build().getPatterns()));
            return this;
        }

        public AnalyzerBuilder ruleListOr(boolean required, AnalyzerBuilder... builders) {
            List<Pattern> subPatterns = new ArrayList<>();
            for (AnalyzerBuilder builder : builders) {
                subPatterns.addAll(builder.build().getPatterns());
            }
            patterns.add(new Pattern(required, null, subPatterns));
            return this;
        }

        public AnalyzerBuilder ruleList(boolean requiredFirst, AnalyzerBuilder... builders) {
            List<Pattern> subPatterns = new ArrayList<>();
            for (AnalyzerBuilder builder : builders) {
                subPatterns.addAll(builder.build().getPatterns());
            }
            patterns.add(new Pattern(requiredFirst, null, subPatterns));
            return this;
        }
    }
}