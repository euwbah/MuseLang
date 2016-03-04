package parsing;

public class Context {
    public boolean stringDelimiterIsADoubleQuote;
    public Contexts context;

    public Context(Contexts context) {
        stringDelimiterIsADoubleQuote = false;
        this.context = context;
    }

    public Context(Context context) {
        this.context = context.context;
        this.stringDelimiterIsADoubleQuote = context.stringDelimiterIsADoubleQuote;
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
         * The main syntax when used as a method invoke within a pattern for the purpose
         * of returning a MuseLang.Number value as the parameter.
         */
        PATTERN_MAIN
    }
}
