package lang;

import java.util.ArrayList;

/**
 * A Function declaration.
 */
public class Function extends LangEntity {
    public int argumentCount;
    public ArrayList<Call> calls;
    private Members members;

    public Function(Members initMembers, ArrayList<Call> calls) {
        this.members = initMembers;
        this.calls = calls;
    }


}
