package scintillate.amber;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import ai.kitt.snowboy.SnowboyDetect;

/**
 * Created by rin on 2/18/2017.
 */

public class SnowboyDetector {
    private static final String TAG = "snowboy";

    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static SnowboyDetector instance = null;

    public static final int RECORDER_BPP = 16;
    public static int RECORDER_SAMPLERATE = 16000;
    public static int RECORDER_CHANNELS = 1;
    public static int RECORDER_SECONDS = 1;
    public static int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    public static int TIMES_CALLED = 0;


    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    private boolean isExit = false;

    private SnowboyDetect snowboyDetector;

    public SnowboyDetector() {
        //init snowboy
        // Assume you put the model related files under /sdcard/snowboy/
        snowboyDetector = new SnowboyDetect("/sdcard/snowboy/common.res", "/sdcard/snowboy/Amber.pmdl,/sdcard/snowboy/asuka.pmdl");
        snowboyDetector.SetSensitivity("0.5,0.3");         // Sensitivity for each hotword
        snowboyDetector.SetAudioGain(1.0f);              // Audio gain for detection
        Log.i(TAG, "NumHotwords = " + snowboyDetector.NumHotwords() + ", BitsPerSample = " +
                snowboyDetector.BitsPerSample() + ", NumChannels = " +
                snowboyDetector.NumChannels() + ", SampleRate = " + snowboyDetector.SampleRate());

        /*bufferSize = AudioRecord.getMinBufferSize
                (sampleRate, channels, audioEncoding) * 3;*/
        bufferSize = snowboyDetector.NumChannels() * snowboyDetector.SampleRate() * RECORDER_SECONDS;
        Log.i(TAG, "buffer size:" + bufferSize);
    }

    public static SnowboyDetector getInstance(){
        if (instance == null){
            Log.i(TAG, "new snowboy");
            instance = new SnowboyDetector();
        }
        return instance;
    }

    private void initRecorder() {
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING,
                bufferSize);
    }

    public void startRecord() {
        if (isRecording) {
            Log.i(TAG, "already recording");
            return;
        }

        Log.i(TAG, "start recording");
        ++TIMES_CALLED;
        isRecording = true;

//        if (recorder == null){
            initRecorder();
//        }

        int i = recorder.getState();
        if (i == AudioRecord.STATE_INITIALIZED) {
            recorder.startRecording();
        }

        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                writeAudioDataToFile(TIMES_CALLED);
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    public void stopRecord() {
        Log.i(TAG, "stop recording");
        isRecording = false;
        if (recorder != null) {
            recorder.stop();
            recorder.release();
//            recorder = null;
        }
    }

    private void hotwordDetected(int index){
        stopRecord();
        Log.i(TAG, "YAY WE HAVE DETECTED THE HOTWORD!!!");
        EventBus.getDefault().post(new MessageEvent(index + " Hotword Detected", MessageEvent.MAIN_ACTIVITY, MessageEvent.HOTWORD_EVENT));
    }

    private void writeAudioDataToFile(int id) {
        /*FileOutputStream os = null;
        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        short data[] = new short[bufferSize / 2];
        int silenceNumber = 0;
        int sleepy;

        int read = 0;
        try {
            while (isRecording) {
                read = recorder.read(data, 0, data.length);
//                Log.i(TAG, "read length = " + read);
                if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                    /*os.write(data, 0, read);
                    os.flush();*/
                    int result = snowboyDetector.RunDetection(data, data.length);
                    Log.i(TAG, "recorder."+id+" ----> result = " + result);
                    if (result > 0){
                        hotwordDetected(result);
                        break;
                    }
//                    else if (result == -2){
//                        ++silenceNumber;
//                        if (silenceNumber > 15){
//                            Log.i(TAG, "sleeping: " + silenceNumber);
//                            Thread.sleep(1000);
//                        }
//                    }
//                    else {
//                        silenceNumber = 0;
//                    }
                }
                Thread.sleep(30);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally
        {
            if(recorder!=null && recorder.getState()==AudioRecord.STATE_INITIALIZED)
            {
                recorder.stop();
                recorder.release();
            }
        }

//        stopRecord();
    }
}
