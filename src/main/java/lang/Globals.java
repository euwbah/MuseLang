package lang;

import java.util.ArrayList;

public class Globals {
    public ArrayList<MuseObject> objects;

    public Globals() {

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
