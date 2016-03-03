package parsing;

/**
 * Only intended for temporal use as string indexing will easily be outdated by inserting a char.
 */
public class IndexedCode {
    public String code;
    public int startIndex, endIndex;

    public IndexedCode(String code, int startIndex, int endIndex) {
        this.code = code;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
}
