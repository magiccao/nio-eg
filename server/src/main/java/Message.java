import java.util.Arrays;

/**
 * Created by d.cao on 2016/12/21.
 */
public class Message {

    private int type; // 1B
    private int length; // 4B, big-endian
    private int op; // 1B
    private byte[] body;

    public Message(int type, int length, int op, byte[] body) {
        this.type = type;
        this.length = length;
        this.op = op;
        this.body = Arrays.copyOf(body, length);
    }

    public String toString() {
        return "type: " + (char) type + ", " +
                "op: " + (char) op + ", " +
                "len: " + length + ", " +
                "body: " + new String(body);
    }

}
