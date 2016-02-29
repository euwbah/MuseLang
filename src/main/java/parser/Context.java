package parser;

public class Context {
    public boolean stringDelimiterIsADoubleQuote;
    public Contexts context;

    public Context(Contexts context) {
        stringDelimiterIsADoubleQuote = false;
        this.context = context;
    }

    public enum Contexts {
        MAIN, PATTERN, STRING, COMMENT
    }
}
