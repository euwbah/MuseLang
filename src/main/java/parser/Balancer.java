package parser;

public class Balancer {
    public static int unclosedParenthesisIn(String code, Context context) {
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

        return unclosedParenthesis;
    }

    public static int unclosedBracketsIn(String code, Context context) {
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

        return unclosedBrackets;
    }

    private static void applyContext(Context context, String s, String snext, String sprev) {
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
