package parsing;

public class Balancer {
    public static DepthAndContext unclosedParenthesisIn(String code, Context context) {
        int unclosedParenthesis = 0;

        for(int c = 0; c < code.length(); c++) {
            String s = String.valueOf(code.charAt(c));
            String snext = c < code.length() - 1 ? String.valueOf(code.charAt(c + 1)) : "";
            String sprev = c > 0 ? String.valueOf(code.charAt(c - 1)) : "";

            if(context.context == Context.Contexts.MAIN) {
                if (s.equals("("))
                    unclosedParenthesis++;
                else if (s.equals(")"))
                    unclosedParenthesis--;
            }
            applyContext(context, s, snext, sprev);
        }

        return new DepthAndContext(unclosedParenthesis, context);
    }

    public static DepthAndContext unclosedBracketsIn(String code, Context context) {
        int unclosedBrackets = 0;

        for(int c = 0; c < code.length(); c++) {
            String s = String.valueOf(code.charAt(c));
            String snext = c < code.length() - 1 ? String.valueOf(code.charAt(c + 1)) : "";
            String sprev = c > 0 ? String.valueOf(code.charAt(c - 1)) : "";

            if (context.context == Context.Contexts.MAIN) {
                if (s.equals("["))
                    unclosedBrackets ++;
                else if (s.equals("]"))
                    unclosedBrackets --;
            }

            applyContext(context, s, snext, sprev);
        }

        return new DepthAndContext(unclosedBrackets, context);
    }

    /**
     * TODO
     * Returns an indexed substring of where the current depth of caretPos terminates
     * @param code The entire code in the text box
     * @param caretPos The index of the caret
     * @return Returns an IndexedCode instance of the zero-depth code. If improperly balanced parenthesis, returns
     *         the lowest depth code syntactically possible.
     */
    public static IndexedCode getZeroDepthCode(String code, int caretPos) {
        String codeUntilCaret = code.substring(0, caretPos);
        DepthAndContext uncParen = unclosedParenthesisIn(codeUntilCaret, new Context(Context.Contexts.MAIN));
        int unclosedParenthesisAtCaret = uncParen.unclosedDelimeters;
        Context contextAtCaret = uncParen.context;

        Context currContext = new Context(contextAtCaret);

        for(int i = caretPos; i >= 0; i++) {

        }

    }

    /**
     * If applying context backwards, remember to remove comments first!
     * @param context
     * @param s
     * @param snext
     * @param sprev
     */
    public static void applyContext(Context context, String s, String snext, String sprev) {
        if (s.equals("\"")) {
            if (context.context == Context.Contexts.MAIN) {
                context.context = Context.Contexts.STRING;
                context.stringDelimiterIsADoubleQuote = true;
            } else if (context.context == Context.Contexts.STRING && context.stringDelimiterIsADoubleQuote && !sprev.equals("\\"))
                context.context = Context.Contexts.MAIN;
        } else if (s.equals("\'")) {
            if (context.context == Context.Contexts.MAIN) {
                context.context = Context.Contexts.STRING;
                context.stringDelimiterIsADoubleQuote = false;
            } else if (context.context == Context.Contexts.STRING && !context.stringDelimiterIsADoubleQuote && !sprev.equals("\""))
                context.context = Context.Contexts.MAIN;
        } else if (s.equals("/") && snext.equals("/")) {
            if (context.context == Context.Contexts.MAIN)
                context.context = Context.Contexts.COMMENT;
        } else if (s.equals("\n")) {
            if(context.context == Context.Contexts.COMMENT)
                context.context = Context.Contexts.MAIN;
        }
        else if (s.equals("\\")) {
            if(context.context == Context.Contexts.MAIN)
                context.context = Context.Contexts.PATTERN;
            else if(context.context == Context.Contexts.PATTERN)
                context.context = Context.Contexts.MAIN;
        }
    }
}
