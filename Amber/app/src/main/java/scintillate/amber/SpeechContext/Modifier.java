package scintillate.amber.SpeechContext;

import java.util.ArrayList;

/**
 * Created by rin on 2/28/2017.
 */

public class Modifier extends Fragment{
    public final static int VECTOR = 0;
    public final static int ORDER = 1;

    private int modType;
    private ArrayList<ActionResponsePair> allowedTargets  = new ArrayList<ActionResponsePair>();
    private Object boundResponse;
    public boolean isGeneric = false;

    public Modifier(String word, int type) {
        super(word);
        this.modType = type;
    }

    public Modifier addTarget(String action, String app, Object response) {
        this.allowedTargets.add(new ActionResponsePair(action, app, response));
        return this;
    }

    public int getModType(){
        return this.modType;
    }

    @Override
    public boolean findRelated(Fragment head){
        Fragment cursor = head;
        while (cursor != null){
            if (cursor.isAcceptable(this)){
                Object res = this.getResponse(cursor.getBase());
                if (res != null){
                    cursor.addModifier(this);
                    this.boundResponse = res;
                    return true;
                }
            }
            cursor = cursor.next;
        }
        return false;
    }

    private boolean isAllowed(String action){
        return getResponse(action) != null;
    }

    public Object getResponse(String action){
        int i;
        String tmp;
        for (i = 0; i < this.allowedTargets.size(); ++i){
            tmp = this.allowedTargets.get(i).action;
            if (tmp.equals(action)){
                return this.allowedTargets.get(i).response;
            }
        }
        return null;
    }

    public boolean modify(Command c){
        if (boundResponse instanceof Command){
            //overwrite command
            c.overwrite((Command)boundResponse);
            return true;
        }
        else {
            c.setMod(boundResponse.toString());
        }
        return false;
    }


    public String getMod(){
        if (boundResponse instanceof Command){
            return "cmd";
        }
        else {
            return boundResponse.toString();
        }
    }
}