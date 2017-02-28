package scintillate.amber.SpeechContext;

import java.util.ArrayList;

/**
 * Created by rin on 2/28/2017.
 */

public class Modifier extends Fragment{
    public final static int VECTOR = 0;
    public final static int ORDER = 1;

    private int modType;
    private ArrayList<ActionResponsePair> allowedTargets;

    public Modifier(String word, int type) {
        super(word);
        this.modType = type;
    }

    public Modifier addTarget(String action, Object response) {
        this.allowedTargets.add(new ActionResponsePair(action, response));
        return this;
    }
}
