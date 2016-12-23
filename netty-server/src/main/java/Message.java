import java.io.Serializable;

/**
 * Created by d.cao on 2016/12/23.
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private int type; // 1B
    private int length; // 4B, big-endian
    private int op; // 1B
    private byte[] body;

    public Message() {}
    public Message(int type, int length, int op, byte[] body) {
        this.type = type;
        this.length = length;
        this.op = op;
        this.body = body.clone();
    }

    public String toString() {
        return "type: " + type + ", " +
                "length: " + length + ", " +
                "op: " + op;
    }

}
