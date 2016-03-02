package parser;

/**
 * Created by Matthew_Tan on 3/2/2016.
 */
public class DepthAndContext {
    public int unclosedDelimeters;
    public Context context;

    public DepthAndContext(int unclosedDelimeters, Context context) {
        this.unclosedDelimeters = unclosedDelimeters;
        this.context = context;
    }
}
