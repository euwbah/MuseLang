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

        Context currContext = new Context(contextAtCaret);//ditto
        int currDepth = depthAtCaret;

        int startIndex, endIndex;

        //This one moves backwards...
        for(int i = index; i >= 0; i++) {
            String s = String.valueOf(code.charAt(i));

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
            applyContextBackwards(currContext, code, i);
        }


        //This one moves forwards
        for(int i = index; i < )
    }

    /**
     * FORWARD USE ONLY!
     * @param context The Context instance to store context data.
     * @param s String.valueOf(string.charAt(n))
     * @param snext String.valueOf(string.charAt(n + 1))
     * @param sprev String.valueOf(string.charAt(n - 1))
     */
    public static void applyContext(Context context, String s, String snext, String sprev) {

        //The flags first:
        if(context.expectingEndOfString) {
            context.expectingEndOfString = false;
            context.context = Context.Contexts.MAIN;
        }
        if(context.expectingEndOfPattern) {
            context.expectingEndOfPattern = false;
            context.context = Context.Contexts.MAIN;
        }

        //Now the real thing
        if (s.equals("\"")) {
            if (context.context == Context.Contexts.MAIN) {
                context.context = Context.Contexts.STRING;
                context.stringDelimiterIsADoubleQuote = true;
            } else if (context.context == Context.Contexts.STRING && context.stringDelimiterIsADoubleQuote && !sprev.equals("\\"))
                context.expectingEndOfString = true;
        } else if (s.equals("\'")) {
            if (context.context == Context.Contexts.MAIN) {
                context.context = Context.Contexts.STRING;
                context.stringDelimiterIsADoubleQuote = false;
            } else if (context.context == Context.Contexts.STRING && !context.stringDelimiterIsADoubleQuote && !sprev.equals("\""))
                context.expectingEndOfString = true;
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
                context.expectingEndOfPattern = true;
        }
    }

    /**
     * This only works backwards!!!!
     * @param context the context instance
     * @param entireCode the whole code present
     * @param caretPos the caret position where s = String.valueOf(entireCode.charAt(caretPos));
     */
    public static void applyContextBackwards(Context context, String entireCode, int caretPos) {
        String s = String.valueOf(entireCode.charAt(caretPos));
        //So far this is not used
        //String snext = caretPos + 1 < entireCode.length() ? String.valueOf(entireCode.charAt(caretPos + 1)) : "";
        String sprev = caretPos - 1 >= 0 ? String.valueOf(entireCode.charAt(caretPos - 1)) : "";

        //Check for all the flags and what not...
        if(context.commentedCharsLeft > 0) {
            context.commentedCharsLeft--;
            if(context.commentedCharsLeft == 0)
                context.context = Context.Contexts.MAIN;//The comment is over
        }
        if(context.expectingEndOfString){
            context.context = Context.Contexts.MAIN;
            context.expectingEndOfString = false;
        }
        if(context.expectingEndOfPattern) {
            context.context = Context.Contexts.MAIN;
            context.expectingEndOfPattern = false;
        }

        //After all the flags are cleared, do this
        if(context.commentedCharsLeft == 0) {
            if (s.equals("\"")) {
                if (context.context == Context.Contexts.MAIN) {
                    context.context = Context.Contexts.STRING;
                    context.stringDelimiterIsADoubleQuote = true;
                } else if (context.context == Context.Contexts.STRING && context.stringDelimiterIsADoubleQuote && !sprev.equals("\\"))
                    context.expectingEndOfString = true;//Change the next char to a MAIN, cuz this one's still part of the string
            } else if (s.equals("\'")) {
                if (context.context == Context.Contexts.MAIN) {
                    context.context = Context.Contexts.STRING;
                    context.stringDelimiterIsADoubleQuote = false;
                } else if (context.context == Context.Contexts.STRING && !context.stringDelimiterIsADoubleQuote && !sprev.equals("\""))
                    context.expectingEndOfString = true;//Change the next char to a MAIN, cuz this one's still part of the string
            } else if (s.equals("\n")) {

                int earliestOccuranceOfSingleLineCommentDelimiterAsDistanceFromThisNewLine = -1;//-1 for no comments

                //Loop until the next \n is found. In the process, determine location of comment if any
                for(int i = caretPos; i >= 0; i--) {
                    String curr = String.valueOf(entireCode.charAt(i));
                    String prev = i - 1 >= 0 ? String.valueOf(entireCode.charAt(i - 1)) : "";

                    if(curr.equals("\n"))
                        break;//Line has been scanned through

                    if(curr.equals("/") && prev.equals("/"))
                        earliestOccuranceOfSingleLineCommentDelimiterAsDistanceFromThisNewLine = caretPos - i;
                }

                //Set the comment context flag
                //If no comments, -1 + 1 will be 0 and will be treated as no comments.
                context.commentedCharsLeft = earliestOccuranceOfSingleLineCommentDelimiterAsDistanceFromThisNewLine + 1;
                if(earliestOccuranceOfSingleLineCommentDelimiterAsDistanceFromThisNewLine > 0) {
                    context.context = Context.Contexts.COMMENT;
                }
            } else if (s.equals("\\")) {
                if (context.context == Context.Contexts.MAIN)
                    context.context = Context.Contexts.PATTERN;
                else if (context.context == Context.Contexts.PATTERN)
                    context.expectingEndOfPattern = true;//Change the next char to a MAIN cuz this one's still part of the Pattern
            }
        }
    }
}
