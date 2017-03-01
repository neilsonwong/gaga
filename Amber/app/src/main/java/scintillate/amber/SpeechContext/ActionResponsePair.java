package scintillate.amber.SpeechContext;

/**
 * Created by neilson on 01/03/17.
 */

public class ActionResponsePair {
    public String action;
    public String application;
    public Object response;

    public ActionResponsePair(String action, String application){
        this.action = action;
        this.application = application;
        this.response = "generic";
    }

    public ActionResponsePair(String action, String application, Object res){
        this.action = action;
        this.application = application;
        this.response = res;
    }
}