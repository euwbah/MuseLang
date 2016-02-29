package lang;

import java.util.ArrayList;

public class Globals {
    public ArrayList<LangEntity> objects;

    public Globals() {
        objects = new ArrayList<>();
    }

    public ArrayList<Function> getAllFunctions() {
        ArrayList<Function> returnable = new ArrayList<>();
        objects.forEach(museObject -> {
            if (museObject instanceof Function)
                returnable.add((Function) museObject);
        });
        return returnable;
    }
}
