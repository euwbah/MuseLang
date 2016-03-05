package parsing;

/**
 * This class is mainly used for initial code eval. To let the evaluator know what code exactly to evaluate...
 */
public class Context {
    public boolean stringDelimiterIsADoubleQuote;
    public Contexts context;

    //Flags. Yes. Bad programming practise. But there's no other way...

    /**
     * This works as a counter for how many chars are left before the comment ends in the function
     * applyContextBackwards. The counter will at the exact index of a newline and will
     * decrease to zero one index before the first (or last, backwards) occurance of the first /
     * of the two slashes // to denote a comment.
     */
    public int commentedCharsLeft;
    public boolean expectingEndOfString;
    public boolean expectingEndOfPattern;

    public Context(Contexts context) {
        stringDelimiterIsADoubleQuote = false;
        this.context = context;
    }

    public Context(Context context) {
        this.context = context.context;
        this.stringDelimiterIsADoubleQuote = context.stringDelimiterIsADoubleQuote;
        commentedCharsLeft = 0;
    }

    public enum Contexts {
        /**
         * The main syntax - lisp-like, MuseLang
         */
        MAIN,
        /**
         * The secondary syntax - PatternLang
         */
        PATTERN,
        /**
         * Standard string literals
         */
        STRING,
        /**
         * Comments. Marked with a double slash
         */
        COMMENT,
        /**
         * TODO only if necessary for initial code eval.
         * The main syntax when used as a method invoke within a pattern for the purpose
         * of returning a MuseLang.Number value as the parameter.
         */
        PATTERN_MAIN
    }
}
