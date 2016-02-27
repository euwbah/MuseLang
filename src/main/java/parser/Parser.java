package parser;

import java.util.List;

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
}
