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
    public final static int TRACKING = 5;

    public ArrayList<ActionResponsePair> allowedActions;
    public int targetType;
    public String subAction = null;
    public boolean isGeneric = false;

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

    public Target addAction(String action, String application){
        this.allowedActions.add(new ActionResponsePair(action, application));
        return this;
    }

    public Target addAction(String action, String application, Object response){
        this.allowedActions.add(new ActionResponsePair(action, application, response));
        return this;
    }

    public boolean modify(Action boundTo, Command c){
        //deal with modifiers first
        boolean hijacked = modifierTakeover(boundTo);

        int i;
        ActionResponsePair tmp;

        for (i = 0; i < this.allowedActions.size(); ++i){
            tmp = this.allowedActions.get(i);
            if (tmp.action.equals(boundTo.getAction())){
                c.setTarget(tmp.response.toString());
                if (tmp.application != null){
                    c.setApp(tmp.application);
                }
            }
        }
        return hijacked;
    }

    private boolean modifierTakeover(Action boundTo){
        boolean takenOver = false;
        if (!this.modifiers.isEmpty()){
            int len = this.modifiers.size();
            int i;
            for (i = 0; i < len; ++i){
                this.modifiers.get(i).getMod().equals("cmd");
                //this will change the original action
                //attach the takeover

                //boundRes should be the same as the one bound to target
                boundTo.addModifier(this.modifiers.get(i));
                takenOver = true;
            }
        }
        return takenOver;
    }

    @Override
    public boolean isAcceptable(Modifier m){
        return true;
    }

    @Override
    public boolean findRelated(Fragment head){
        Fragment cursor = this.prev;
        Fragment cursor2 = this.next;
        while (cursor != null || cursor2 != null){
            if (cursor != null){
                if (cursor.isAcceptable(this)){
                    ((Action)cursor).setTarget(this);
                    return true;
                }
                cursor = cursor.prev;
            }
            else if (cursor2 != null){
                if (cursor2.isAcceptable(this)){
                    ((Action)cursor).setTarget(this);
                    return true;
                }
                cursor2 = cursor2.next;
            }

        }
        return false;
    }

    @Override
    public String toString(){
        String ret = this.getBase();

        if (this.modifiers.size() > 0){
            ret += " ( ";
            int i;
            for (i = 0; i < modifiers.size(); ++i){
                ret += this.modifiers.get(i).getMod() + " , " ;
            }
            ret += " )";
        }

        return ret;
    }
}