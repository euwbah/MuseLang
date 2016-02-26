package parser;

import tokens.Token;

import java.util.List;

public class Tokenizer {

    private String entireCode;
    private String evaluatedCode;
    private int startPos;
    private int endPos;//Null if caret pos only and no selection.
    private int startExecPos;
    private int endExecPos;

    public Tokenizer(String entireCode, int caretPos) {
        this.entireCode = entireCode;
        startPos = caretPos;
    }

    public List<Token> Tokenize() {
        //TODO:
        return null;
    }


}
