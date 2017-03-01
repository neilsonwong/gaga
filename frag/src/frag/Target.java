package frag;

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

    public String getTarget(Action boundTo){
    	//deal with modifiers first
    	modifierTakeover(boundTo);
    	
    	int i;
    	String tmp;
    	
    	for (i = 0; i < this.allowedActions.size(); ++i){
    		tmp = this.allowedActions.get(i).action;
    		if (tmp.equals(boundTo.getAction())){
    			return this.allowedActions.get(i).response.toString();
    		}
    	}
    	return "";
    }
    
    private void modifierTakeover(Action boundTo){
    	if (!this.modifiers.isEmpty()){
    		int len = this.modifiers.size();
    		int i;
    		for (i = 0; i < len; ++i){
    			this.modifiers.get(i).getMod().equals("cmd");
    			//this will change the original action
    			//attach the takeover
    			
    			//boundRes should be the same as the one bound to target
    			boundTo.addModifier(this.modifiers.get(i));
    		}
    	}
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
        	if (cursor != null && cursor.isAcceptable(this)){
        		((Action)cursor).setTarget(this);
        		return true;
        	}
        	else if (cursor2 != null && cursor2.isAcceptable(this)){
        		((Action)cursor).setTarget(this);
        		return true;
        	}
        	cursor = cursor.prev;
        	cursor2 = cursor2.next;
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

class ActionResponsePair {
    public String action;
    public Object response;
    public ActionResponsePair(String action, Object res){
        this.action = action;
        this.response = res;
    }
}

