package scintillate.amber.SpeechContext;

import java.util.HashMap;

/**
 * Created by rin on 2/28/2017.
 */

public class Vocab {
    private HashMap<String, Fragment> list;

    public Vocab() {
        initActions();
        initTargets();
    }

    private void initActions() {
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

        list.put("last", new Action("last", "previous", Action.REQUIRE_TARGET)
                .addTargetable(Target.MUSIC, Target.VIDEO)
                .addPostReq("track")
                .addPostReq("song")
                .addPostReq("ep")
                .addPostReq("episode"));

        list.put("go", new Action("go", null, Action.REQUIRE_TARGET)
                .addPostReq("back")
                .addPostReq("forward")
                .addPostReq("to", "start")
                .addPostReq("to", "the", "start")
                .addPostReq("to", "beginning")
                .addPostReq("to", "the", "beginning")
                .addPostReq("back"));

        list.put("volume", new Action("volume", null, Action.REQUIRE_TARGET)
                .addTargetable(Target.VECTOR)
                .addPostReq("up")
                .addPostReq("down"));

        list.put("jump", new Action("jump", "jump to beginning", Action.REQUIRE_TARGET)
                .addPostReq("to", "beginning")
                .addPostReq("to", "start")
                .addPostReq("to", "the", "beginning")
                .addPostReq("to", "the", "start"));

        list.put("open", new Action("open", Action.REQUIRE_TARGET)
                .addTargetable(Target.MEDIA, Target.MUSIC, Target.VIDEO));

        list.put("shutdown", new Action("shutdown", "shutdown", Action.ACCEPT_TARGET)
                .addTargetable(Target.DEVICE));
        list.put("shut", new Action("shut", "shutdown", Action.ACCEPT_TARGET)
                .addTargetable(Target.DEVICE)
                .addPostReq("down"));
        list.put("turn", new Action("turn", null, Action.ACCEPT_TARGET)
                .addTargetable(Target.DEVICE)
                .addPostReq("on")
                .addPostReq("off"));
        list.put("power", new Action("power", null, Action.ACCEPT_TARGET)
                .addTargetable(Target.DEVICE)
                .addPostReq("on")
                .addPostReq("off"));

        list.put("close", new Action("close", null, Action.ACCEPT_TARGET)
                .addTargetable(Target.APPLICATION));
        list.put("exit", new Action("exit", null, Action.ACCEPT_TARGET)
                .addTargetable(Target.APPLICATION));
    }

    private void initTargets() {
        //vectors
        list.put("down", new Target("down", Target.VECTOR)
                .addAction("volume", -1));

        list.put("up", new Target("up", Target.VECTOR)
                .addAction("volume", 1));

        list.put("on", new Target("on", Target.VECTOR)
                .addAction("turn", 1)
                .addAction("power", 1));

        list.put("off", new Target("off", Target.VECTOR)
                .addAction("turn", 0)
                .addAction("power", 0));

        //music
        list.put("music", new Target("music", Target.MUSIC)
                .addAction("play", "generic")
                .addAction("pause", "generic")
                .addAction("stop", "generic")
        );
        list.put("track", new Target("track", Target.MUSIC)
                .addAction("next", "generic")
                .addAction("previous", "generic")
                .addAction("last", "generic"));

        list.put("song", new Target("song", Target.MUSIC)
                .addAction("next", "generic")
                .addAction("skip", "generic")
                .addAction("previous", "generic")
                .addAction("last", "generic"));

        //video
        list.put("ep", new Target("ep", Target.VIDEO)
                .addAction("play", "generic")
                .addAction("pause", "generic")
                .addAction("stop", "generic")
                .addAction("next", "generic")
                .addAction("previous", "generic")
                .addAction("last", "generic"));

        list.put("episode", new Target("episode", Target.VIDEO)
                .addAction("play", "generic")
                .addAction("pause", "generic")
                .addAction("stop", "generic")
                .addAction("next", "generic")
                .addAction("previous", "generic")
                .addAction("last", "generic"));

        list.put("video", new Target("video", Target.VIDEO)
                .addAction("play", "generic")
                .addAction("pause", "generic")
                .addAction("stop", "generic")
                .addAction("next", "generic")
                .addAction("previous", "generic")
                .addAction("last", "generic"));

        //device
        list.put("computer", new Target("video", Target.DEVICE)
                .addAction("turn", "generic")
                .addAction("power", "generic")
                .addAction("shutdown", "generic"));

        /*later
        //media
        "file",

        "media", // player classic
        "playing",
        "subtitle",
        "audio" //track
        */
    }
}
