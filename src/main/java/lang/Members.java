package lang;

import java.util.ArrayList;

public class Members {
    public ArrayList<LangEntity> objects;

    public Members() {
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
