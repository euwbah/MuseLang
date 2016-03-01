package parser;

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
        MAIN, PATTERN, STRING, COMMENT
    }
}
