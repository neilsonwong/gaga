package scintillate.amber;

/**
 * Created by rin on 2/18/2017.
 */

public class MessageEvent {
    public final String message;
    public final int recipient;
    public int eventCode = -1;

    public final static int MAIN_ACTIVITY = 0;
    public final static int HOTWORD_SERVICE = 1;
    public final static int ACTION_HANDLER = 2;

    public final static int HOTWORD_EVENT = 99;
    public final static int DO_SOMETHING_EVENT = 100;
    public final static int START_HOTWORD_EVENT = 1000;
    public final static int COMPUTER_AWAKE_EVENT = 1001;

    public MessageEvent(String message, int recipientId) {
        this.message = message;
        this.recipient = recipientId;
    }

    public MessageEvent(String message, int recipientId, int eventCode) {
        this.message = message;
        this.recipient = recipientId;
        this.eventCode = eventCode;
    }
}