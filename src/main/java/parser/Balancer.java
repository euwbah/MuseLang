package parser;

/**
 * Created by Matthew_Tan on 2/27/2016.
 */
public class Balancer {
    public static int unclosedParenthesisIn(String code) {
        Contexts context = Contexts.MAIN;
        int unclosedParenthesis = 0;
        boolean stringDelimiterIsADoubleQuote = false;

        for(int c = 0; c < code.length(); c++) {
            String s = String.valueOf(code.charAt(c));
            String snext = c < code.length() - 1 ? String.valueOf(code.charAt(c + 1)) : "";
            String nprev = c > 0 ? String.valueOf(code.charAt(c - 1)) : "";

            if (s.equals("(")) {
                if (context == Contexts.MAIN)
                    unclosedParenthesis++;
            } else if (s.equals(")")) {
                if (context == Contexts.MAIN)
                    unclosedParenthesis--;
            } else if (s.equals("\"")) {
                if (context == Contexts.MAIN) {
                    context = Contexts.STRING;
                    stringDelimiterIsADoubleQuote = true;
                } else if (context == Contexts.STRING && stringDelimiterIsADoubleQuote &! nprev.equals("\\"))
                    context = Contexts.MAIN;
            } else if (s.equals("\'")) {
                if (context == Contexts.MAIN) {
                    context = Contexts.STRING;
                    stringDelimiterIsADoubleQuote = false;
                } else if (context == Contexts.STRING && !stringDelimiterIsADoubleQuote &! nprev.equals('\\"'))
                    context = Contexts.MAIN;
            } else if (s.equals("/") && snext.equals("/")) {
                if (context == Contexts.MAIN)
                    context = Contexts.COMMENT;
            } else if (s.equals("\n")) {
                if(context == Contexts.COMMENT)
                    context = Contexts.MAIN;
            }
            else if (s.equals("\\")) {
                if(context == Contexts.MAIN) {
                    context == Contexts.PATTERN; 
                }
                   
            }
        }
    }
}
