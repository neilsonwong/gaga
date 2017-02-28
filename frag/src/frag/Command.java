package frag;

public class Command {
    private String action = null;
    private String target = null;

    public Command(){}

    public Command(String a){
        this.action = a;
    }

    public Command(String a, String t){
        this.action = a;
        this.target = t;
    }

    public String getAction(){
        return this.action;
    }

    public String getTarget(){
        return this.target;
    }

    public void setAction(String a){
        this.action = a;
    }

    public void setTarget(String t){
        this.target = t;
    }
}
