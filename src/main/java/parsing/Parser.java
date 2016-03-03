package parsing;

public class Parser {

    private String entireCode;
    private String evaluatedCode;
    private int caretStartPos;//If caretPos decl used, this refers to the caretPos
    private int caretEndPos;//Null if caret pos only and no selection.
    private int startExecPos;
    private int endExecPos;

    public Parser(String entireCode, int caretPos) {
        this.entireCode = entireCode;
        caretStartPos = caretPos;
    }

    public static String removeComments(String entireCode) {
        Context c = new Context(Context.Contexts.MAIN);

        String returnable = "";

        for(int i = 0; i < returnable.length(); i++) {
            String s = String.valueOf(returnable.charAt(i));
            String snext = i + 1 < returnable.length() ? String.valueOf(returnable.charAt(i + 1)) : "";
            String sprev = i - 1 >= 0 ? String.valueOf(returnable.charAt(i - 1)) : "";

            Balancer.applyContext(c, s, snext, sprev);

            if(c.context != Context.Contexts.COMMENT)
                returnable += s;
        }

        return returnable;
    }
}
