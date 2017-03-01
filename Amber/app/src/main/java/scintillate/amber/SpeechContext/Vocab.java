package scintillate.amber.SpeechContext;

import java.util.HashMap;

/**
 * Created by rin on 2/28/2017.
 */

public class Vocab {
    private HashMap<String, Fragment> list;

    public Vocab() {
        list = new HashMap<String, Fragment>();
        initActions();
        initTargets();
        initModifiers();
    }

    public Fragment get(String key){
        Fragment f = list.get(key);
        if (f == null){
            f = new Fragment(key);
        }
//        System.out.println("b" + f);
        return f;
    }

    private void initActions() {    //actions should generally be verbs
//    	list.put("test", new Fragment("test"));
        list.put("play", new Action("play", Action.ACCEPT_TARGET)
                .addTargetable(Target.MEDIA, Target.MUSIC, Target.VIDEO));
        list.put("pause", new Action("pause", Action.ACCEPT_TARGET)
                .addTargetable(Target.MEDIA, Target.MUSIC, Target.VIDEO));
        list.put("stop", new Action("stop", Action.ACCEPT_TARGET)
                .addTargetable(Target.MEDIA, Target.MUSIC, Target.VIDEO));
        list.put("next", new Action("next", Action.ACCEPT_TARGET)
                .addTargetable(Target.MEDIA, Target.MUSIC, Target.VIDEO));
        list.put("skip", new Action("skip", "next", Action.ACCEPT_TARGET)
                .addTargetable(Target.MUSIC));
        list.put("previous", new Action("previous", Action.ACCEPT_TARGET)
                .addTargetable(Target.MEDIA, Target.MUSIC, Target.VIDEO));
        list.put("louder", new Action("louder", Action.ACCEPT_TARGET));
        list.put("softer", new Action("softer", Action.ACCEPT_TARGET));

        list.put("fullscreen", new Action("fullscreen", Action.NO_TARGETS));
        list.put("full", new Action("full", "fullscreen", Action.NO_TARGETS)
                .addPostOpt("screen"));
        list.put("screenshot", new Action("screenshot", Action.NO_TARGETS));
        list.put("screen", new Action("screen", "screenshot", Action.NO_TARGETS)
                .addPostOpt("shot"));
        list.put("switch", new Action("switch", "switch audio", Action.NO_TARGETS)
                .addPostReq("audio"));

        list.put("go", new Action("go", null, Action.REQUIRE_TARGET)
                .addTargetable(Target.TRACKING)
                .addPostReq("back")
                .addPostReq("forward")
                .addPostReq("to", "start")
                .addPostReq("to", "beginning")
                .addPostReq("to", "the", "start")
                .addPostReq("to", "the", "beginning"));

        list.put("volume", new Action("volume", null, Action.NO_TARGETS)
                .addModifiable(Modifier.VECTOR)
                .addPostReq("up")
                .addPostReq("down"));

        list.put("jump", new Action("jump", "jump to beginning", Action.REQUIRE_TARGET)
                .addTargetable(Target.TRACKING)
                .addPostReq("back")
                .addPostReq("forward")
                .addPostReq("to", "start")
                .addPostReq("to", "beginning")
                .addPostReq("to", "the", "start")
                .addPostReq("to", "the", "beginning"));

        list.put("open", new Action("open", Action.REQUIRE_TARGET)
                .addTargetable(Target.MEDIA, Target.MUSIC, Target.VIDEO));

        list.put("shutdown", new Action("shutdown", "shutdown", Action.ACCEPT_TARGET)
                .addTargetable(Target.DEVICE));
        list.put("shut", new Action("shut", "shutdown", Action.ACCEPT_TARGET)
                .addTargetable(Target.DEVICE)
                .addPostReq("down"));
        list.put("turn", new Action("turn", null, Action.ACCEPT_TARGET)
                .addTargetable(Target.DEVICE)
                .addModifiable(Modifier.VECTOR)

                .addPostReq("on")
                .addPostReq("off"));
        list.put("power", new Action("power", null, Action.ACCEPT_TARGET)
                .addTargetable(Target.DEVICE)
                .addModifiable(Modifier.VECTOR)
                .addPostReq("on")
                .addPostReq("off"));

        list.put("close", new Action("close", null, Action.ACCEPT_TARGET)
                .addTargetable(Target.APPLICATION));
        list.put("exit", new Action("exit", null, Action.ACCEPT_TARGET)
                .addTargetable(Target.APPLICATION));
    }

    private void initTargets() {    //targets should generally by nouns
        //music
        list.put("music", new Target("music", Target.MUSIC)
                .addAction("play", "music")
                .addAction("pause", "music")
                .addAction("stop", "music")
        );
        list.put("track", new Target("track", Target.MUSIC)
                .addAction("next", "music")
                .addAction("previous", "music")
                .addAction("last", "music"));

        list.put("song", new Target("song", Target.MUSIC)
                .addAction("next", "music")
                .addAction("skip", "music")
                .addAction("previous", "music")
                .addAction("last", "music"));

        //video
        list.put("ep", new Target("ep", Target.VIDEO)
                .addAction("play", "video")
                .addAction("pause", "video")
                .addAction("stop", "video")
                .addAction("next", "video")
                .addAction("previous", "video")
                .addAction("last", "video"));

        list.put("episode", new Target("episode", Target.VIDEO)
                .addAction("play", "video")
                .addAction("pause", "video")
                .addAction("stop", "video")
                .addAction("next", "video")
                .addAction("previous", "video")
                .addAction("last", "video"));

        list.put("video", new Target("video", Target.VIDEO)
                .addAction("play", "video")
                .addAction("pause", "video")
                .addAction("stop", "video")
                .addAction("next", "video")
                .addAction("previous", "video")
                .addAction("last", "video"));

        //tracking
        list.put("start", new Target("start", Target.TRACKING)
                .addAction("go", null, "beginning")
                .addAction("jump", null,"beginning"));

        list.put("beginning", new Target("beginning", Target.TRACKING)
                .addAction("go", null,"beginning")
                .addAction("jump", null,"beginning"));

        list.put("back", new Target("back", Target.TRACKING)
                .addAction("go", null,"back")
                .addAction("jump", null,"back"));

        list.put("forward", new Target("forward", Target.TRACKING)
                .addAction("go", null,"forward")
                .addAction("jump", null,"forward"));

        //device
        list.put("computer", new Target("computer", Target.DEVICE)
                .addAction("turn", "computer")
                .addAction("power", "computer")
                .addAction("shutdown", "computer"));

        /*later
        //media
        "file",

        "media", // player classic
        "playing",
        "subtitle",
        "audio" //track
        */
    }

    private void initModifiers(){
        list.put("last", new Modifier("last", Modifier.ORDER)
                .addTarget("track", "music", new Command("previous", "music"))
                .addTarget("song", "music", new Command("previous", "music"))
                .addTarget("ep", "video", new Command("previous", "video"))
                .addTarget("episode", "video", new Command("previous", "video"))
                .addPostReq("track")
                .addPostReq("song")
                .addPostReq("ep")
                .addPostReq("episode"));

        //vectors = mod
        list.put("down", new Modifier("down", Modifier.VECTOR)
                .addTarget("volume", null, new Command("softer")));

        list.put("up", new Modifier("up", Modifier.VECTOR)
                .addTarget("volume", null, new Command("louder")));

        list.put("on", new Modifier("on", Modifier.VECTOR)
                .addTarget("turn", "computer", new Command("turn", null, "computer", "on"))
                .addTarget("power", "computer", new Command("turn", null, "computer", "on")));

        list.put("off", new Modifier("off", Modifier.VECTOR)
                .addTarget("turn", "computer", new Command("turn", null, "computer", "off"))
                .addTarget("power", "computer", new Command("turn", null, "computer", "off")));


    }
}
