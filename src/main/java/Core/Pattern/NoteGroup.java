package Core.Pattern;

public class NoteGroup extends NoteElement{

    public Type groupType;

    public NoteGroup() {

    }

    /**
     * The type of grouping applied.
     */
    public enum Type {
        /**
         * Denoted by (Parenthesis). Params applied to the group will be applied to each NoteElement in this group.
         */
        EACH,
        /**
         * Denoted by [Brackets]. Params applied to the group will be applied as a whole as if this group was a single Unit.
         */
        WHOLE,
        /**
         * Denoted by {Braces}. Params applied to the group will be applied the same way as in Brackets.
         * Each element in this group will start and end at the same time.
         */
        SYNC
    }
}
