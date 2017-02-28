package scintillate.amber.SpeechContext;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rin on 2/28/2017.
 */

public class Target extends Fragment{
    public final static int APPLICATION = 0;
//    public final static int VECTOR = 1;
    public final static int MUSIC = 2;
    public final static int VIDEO = 3;
    public final static int MEDIA = 4;
    public final static int DEVICE = 4;

    public ArrayList<ActionResponsePair> allowedActions;
    public int targetType;
    public String subAction = null;

    public Target(String word) {
        super(word);
    }

    public Target(String word, int pTargetType) {
        super(word);
        this.targetType = pTargetType;
        this.allowedActions = new ArrayList<ActionResponsePair>();
    }

    public Target setSubAction(String subAction){
        if (this.subAction == null){
            this.subAction = subAction;
        }
        return this;
    }

    public Target addAction(String action, Object response){
        this.allowedActions.add(new ActionResponsePair(action, response));
        return this;
    }

    public void respond(){

    }
    //TODO: implement find attachable action

    //TODO: implement some way to change
}

class ActionResponsePair {
    public String action;
    public Object response;
    public ActionResponsePair(String action, Object res){
        this.action = action;
        this.response = res;
    }
}
