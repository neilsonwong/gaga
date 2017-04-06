package scintillate.amber.SpeechContext;

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
    public String acceptedTarget = null;
    public Target target = null;
    public String actionWord;
    public boolean isGeneric = false;

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
        targetable = new ArrayList<Integer>();
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

    @Override
    public boolean isAcceptable(Target t){
        return this.targetable.contains(t.targetType);
    }

    @Override
    public boolean isAcceptable(Modifier m){
        return this.modifiable.contains(m.getModType());
    }

    public boolean setTarget(Target f){
        if (this.targetType > -1){
            if (this.target == null){
                this.target = f;
                acceptedTarget = f.getBase();
                return true;
            }
        }
        return false;
    }

    public String getAction(){
        return this.actionWord;
    }

    @Override
    public Command response(){
        //if there is no target or modifier set the command
        if (this.target == null && this.modifiers.isEmpty()){
            return new Command(this.actionWord);
        }

        Command c = new Command();
        String cAction = this.actionWord;
        boolean hijacked = false;

        c.setAction(cAction);

        int i;
        for(i = 0; i < this.modifiers.size(); ++i){
            hijacked |= this.modifiers.get(i).modify(c);
            this.actionWord = c.getAction();
        }

        if (this.target != null){
            hijacked |= this.target.modify(this, c);
        }

        for(i = 0; i < this.modifiers.size(); ++i){
            hijacked |= this.modifiers.get(i).modify(c);
            this.actionWord = c.getAction();
        }


        if (!hijacked ){
            //ensure all required targets are present
            if (this.targetType == REQUIRE_TARGET &&
                this.target == null){
                c.invalidate();
            }
        }

        return c;
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

        if (this.target != null){
            ret += " > " + this.target.toString();
        }

        return ret;
    }
}