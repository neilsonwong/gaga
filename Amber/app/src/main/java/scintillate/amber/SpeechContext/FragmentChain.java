package scintillate.amber.SpeechContext;

import scintillate.amber.ActionHandler;

/**
 * Created by neilson on 01/03/17.
 */

public class FragmentChain {
    private Fragment head;
    public Vocab vocab = new Vocab();

    public FragmentChain(String input){
        this.buildChain(input);
        this.findRelated();
    }

    public Command getCommand(ActionHandler.HandlerContext hc){
        Fragment cursor = this.head;
        Fragment actionator = null;
        Command c;
        Command best = new Command();
        while (cursor != null){
            c = cursor.response();
            if (!c.isBlank()){
                if (c.score() > best.score()){
                    actionator = cursor;
                    best = c;
                }
            }
            cursor = cursor.next;
        }

        //check whether this command is valid
        if (best.getApp() == null){
            //if missing context try to add application context
            best.setApp(hc.currentApplication);
        }

        //some custom code for now to hack it together
        System.out.println("action:" + best.getAction());
        System.out.println("target:" + best.getTarget());

        if (best.getAction() != null && (best.getAction().equals("open") || best.getAction().equals("play")) && best.getTarget() == null){
            System.out.println("we are in");
            String openThis = "";
            cursor = actionator.next;
            while (cursor != null){
                if (cursor.isGeneric){

                    openThis += cursor.getBase() + " ";
                    System.out.println(openThis);
                }
                cursor = cursor.next;
            }

            Magic.MagicalObject ooo = Magic.resolve(openThis.trim());
            if (ooo != null) {
                best.setTarget(ooo.target);
                System.out.println("setting target magical : " + ooo.target);

                if (ooo.app != null) {
//                    best.setApp("video");
                    best.setApp(ooo.app);
                }
            }
        }

        return best;
    }

    private void findRelated(){
        Fragment cursor = this.head;
        while (cursor != null){
            cursor.findRelated(this.head);
//        	System.out.println(cursor.toString());
            cursor = cursor.next;
        }
    }

    private void buildChain(String line){
        String[] words = line.split(" ");
        Fragment f;

        if (words.length == 0){
            this.head = null;
        }

        else {
            this.head = vocab.get(words[0]);
        }

        int i;
        String key;
        Fragment pre = null;

        for (i = 0; i < words.length; ++i){
            key = words[i];
            f = vocab.get(key);

            //chain previous and this
            f.chainPre(pre);
            if (f.prev != null){
                f.prev.chainPost(f);
            }

            //done, set up next loop iteration
            pre = f;
        }
    }
}
