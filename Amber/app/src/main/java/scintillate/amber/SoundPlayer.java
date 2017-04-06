package scintillate.amber;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.content.res.AssetFileDescriptor;

import java.io.IOException;

/**
 * Created by rin on 3/20/2017.
 */

public class SoundPlayer {
    Context context = null;
    MediaPlayer mediaPlayer = null;
    private static SoundPlayer instance = null;

    public static SoundPlayer getInstance(Context context){
        if (instance == null){
            instance = new SoundPlayer(context);
        }
        return instance;
    }

    public static void playThis(String soundFile){
        if(instance != null){
            instance.play(soundFile);
        }
    }

    public SoundPlayer(Context context){
        this.context = context;
    }

    public void play(String soundFile){
        if (context == null){
            Log.e("Sound FX Player", "no context, please create instance first");
        }
        else {
            Log.i("Sound FX Player", "playing : " + soundFile);
        }

        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            }
            mediaPlayer = new MediaPlayer();

            AssetFileDescriptor descriptor = context.getAssets().openFd(soundFile);
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            mediaPlayer.prepare();
            mediaPlayer.setVolume(1f, 1f);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//        mediaPlayer.setOnCompletionListener(
//            new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    mediaPlayer.stop();
//                    if (mediaPlayer != null) {
//                        mediaPlayer.release();
//                    }
//                }
//            }
//        );
    }
}
