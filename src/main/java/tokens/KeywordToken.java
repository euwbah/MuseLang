package tokens;

public class KeywordToken extends Token {

    public KeywordType keywordType;

    public KeywordToken(KeywordType keywordType) {
        this.keywordType = keywordType;
    }

    public enum KeywordType {
        BPM, SIG, LINKPORT, LISTPORTS, CHD
    }
}
