import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by d.cao on 2016/12/22.
 */
public class Encoder {

    public ByteBuffer encode(byte[] data) {
        ByteBuffer buf = ByteBuffer.allocate(4 + data.length);
        buf.order(ByteOrder.BIG_ENDIAN).putInt(data.length);
        buf.put(data);
        buf.flip();

        return buf;
    }

}
