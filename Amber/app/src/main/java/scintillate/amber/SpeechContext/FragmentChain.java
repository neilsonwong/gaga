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
        Command c;
        Command best = new Command();
        while (cursor != null){
            c = cursor.response();
            if (!c.isBlank()){
                if (c.score() > best.score()){
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
