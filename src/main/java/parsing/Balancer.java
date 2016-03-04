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
     * Returns an instance of IndexedCode, containing the code at, and of the same depth as, the caret.
     * @param code The entire code in the text box
     * @param index The index to get the code from.
     * @return Returns an IndexedCode instance of the zero-depth code. If improperly balanced parenthesis, returns
     *         the lowest depth code syntactically possible.
     */
    public static IndexedCode getCodeWithinParenthesisAtIndex(String code, int index) {
        String codeUntilCaret = code.substring(0, index);
        DepthAndContext uncParen = unclosedParenthesisIn(codeUntilCaret, new Context(Context.Contexts.MAIN));
        int depthAtCaret = uncParen.unclosedDelimeters;//by depth, I mean unclosed delimiters
        Context contextAtCaret = uncParen.context;

        String codeWithoutComments = Parser.removeComments(code);
        Context currContext = new Context(contextAtCaret);//ditto
        int currDepth = depthAtCaret;

        int startIndex, endIndex;

        //This one moves backwards...
        for(int i = index; i >= 0; i++) {
            String s = String.valueOf(codeUntilCaret.charAt(i));
            String snext = i + 1 < codeUntilCaret.length() ? String.valueOf(codeUntilCaret.charAt(i + 1)) : "";
            String sprev = i - 1 >= 0 ? String.valueOf(codeUntilCaret.charAt(i - 1)) : "";

            if(currContext.context == Context.Contexts.MAIN) {
                if(s.equals("(")) {
                    currDepth--;
                } else if(s.equals(")")) {
                    currDepth++;
                }
            }
            if(currDepth < depthAtCaret) {
                startIndex = i + 1;
                break;
            }
            applyContext(currContext, s, snext, sprev);
        }


        //This one moves forwards
        for(int i = index; i < )
    }

    /**
     * If applying context backwards, remember to remove comments first!
     * @param context The Context instance to store context data.
     * @param s String.valueOf(string.charAt(n))
     * @param snext String.valueOf(string.charAt(n + 1))
     * @param sprev String.valueOf(string.charAt(n - 1))
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
