package scintillate.amber.SpeechContext;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by rin on 2/27/2017.
 */

public class Action extends Fragment {
    public final static int NO_TARGETS = -1;
    public final static int ACCEPT_TARGET = 0;
    public final static int REQUIRE_TARGET = 1;

    public int targetType;
    public ArrayList<Integer> targetable;
    public ArrayList<Integer> modifiers;
    public int acceptedTarget;
    public Fragment target = null;
    public String actionWord;

    public Action(String word) {
        this(word, word, -1);
    }

    //shorthand
    public Action(String word, int pTargetType) {
        this(word, word, pTargetType);
    }

    public Action(String word, String pActionWord, int pTargetType) {
        super(word);
        targetType = pTargetType;
        actionWord = pActionWord;
    }

    public Action addTargetable(int ... targets){
        int target;
        for (int i = 0; i < targets.length; i++) {
            target = targets[i];
            if (!targetable.contains(target)){
                targetable.add(target);
            }
        }
        return this;
    }

    public Action addModifier(int modifier){
        modifiers.add(modifier);
        return this;
    }

    public boolean setTarget(Fragment f){
        if (this.targetType > -1){
            if (this.target == null){
                this.target = f;
                return true;
            }
        }
        return false;
    }

    @Override
    public Command response(){
        return new Command();
    }
}
