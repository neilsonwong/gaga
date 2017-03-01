package frag;

public class Command {
    private String action = null;
    private String target = null;
    private String mod = null;

    public Command(){}

    public Command(String a){
        this.action = a;
    }

    public Command(String a, String t){
        this.action = a;
        this.target = t;
    }
    
    public Command(String a, String t, String m){
        this.action = a;
        this.target = t;
        this.mod = m;
    }

    public String getAction(){
        return this.action;
    }

    public String getTarget(){
        return this.target;
    }
    
    public String getMod(){
        return this.mod;
    }

    public void setAction(String a){
        this.action = a;
    }

    public void setTarget(String t){
        this.target = t;
    }
    
    public void setMod(String t){
        this.mod = t;
    }
    
    @Override
    public String toString(){
    	if (this.action != null || this.target != null || this.mod!= null){
    		return "action: " + action + "\n" +
    	              "target: " + target + "\n" +
    	              "mod: " + mod;
    	}
    	return "";
    }
    
    public boolean isBlank(){
    	return !(this.action != null || this.target != null || this.mod!= null);
    }
    
    public Command overwrite(Command c){
    	if (c.action != null){
    		this.setAction(c.getAction());
    	}
    	if (c.target != null){
    		this.setTarget(c.getTarget());
    	}
    	if (c.mod != null){
    		this.setMod(c.getMod());
    	}
    	return this;
    }
    
    public int score(){
    	int score = 0;
    	if (this.action != null)
    		++score;
    	if (this.target != null)
    		++score;
    	if (this.mod != null)
    		++score;
    	return score;
    }
}
