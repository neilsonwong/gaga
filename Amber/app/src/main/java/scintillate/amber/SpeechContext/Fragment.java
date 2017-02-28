package scintillate.amber.SpeechContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rin on 2/27/2017.
 */

public class Fragment {
    private String base;
    public boolean finalized = false;

    //reqs = multipart
    public ArrayList<Requirement> requires;
    public ArrayList<Requirement> optional;
    public Fragment prev;
    public Fragment next;

    public Fragment(String word){
        base = word;
    }

    public void chain(Fragment pre, Fragment post){
        this.prev = pre;
        this.next = post;
    }

    public Fragment addPostReq(String... postReqs){
        this.requires.add(new Requirement(new String[0], postReqs));
        return this;
    }

    public Fragment addPostOpt(String... postReqs){
        this.optional.add(new Requirement(new String[0], postReqs));
        return this;
    }

    public void addReq(String[] pre, String[] post){
        this.requires.add(new Requirement(pre, post));
    }

    public void addOption(String[] pre, String[] post){
        this.optional.add(new Requirement(pre, post));
    }

    public Command response(){
        return new Command();
    }

    public boolean satisfiesReqs(){
        int reqLen = this.requires.size();
        int i;
        for (i = 0; i < reqLen; ++i){
            //we only need to satisfy 1 req
            if (this.requires.get(i).isSatisfied(this)){
                return true;
            }
        }
        return false;
    }

    public String getBase(){
        return this.base;
    }

    public void resolve(){
        //take in a context and change it?
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!Fragment.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Fragment f = (Fragment) obj;
        return this.base.equals(f.base);
    }
}

class Requirement {
    private ArrayList<String> before;
    private ArrayList<String> after;

    public Requirement(String[] pre, String[] post){
        this.before = new ArrayList<String>(Arrays.asList(pre));
        this.after = new ArrayList<String>(Arrays.asList(post));
    }

    public boolean isSatisfied(Fragment candidate){
        Fragment cur;
        if (!this.before.isEmpty()){
            cur = candidate;
            int beforeLen = this.before.size();
            int i;
            for (i = beforeLen -1; i >= 0; --i){
                //iterate backwards because that's how we'll walk the fragments
                cur = candidate.prev;
                if (!this.before.get(i).equals(cur.getBase())){
                    return false;
                }
            }
        }
        if (!this.after.isEmpty()){
            cur = candidate;
            int afterLen = this.after.size();
            int i;
            for (i = 0; i < afterLen; ++i){
                //iterate forwards
                cur = candidate.next;
                if (!this.after.get(i).equals(cur.getBase())){
                    return false;
                }
            }
        }
        return false;
    }
}