import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by d.cao on 2016/12/22.
 */
public class Decoder {

    private ByteBuffer header;
    private ByteBuffer body;

    public Message decode(ByteBuffer r) {
        r.flip();
        if (!readHeader(r)) {
            r.compact();
            return null;
        }

        // header already
        header.rewind();
        int type = header.get();
        int length = header.order(ByteOrder.BIG_ENDIAN).getInt();
        if (body == null) {
            body = ByteBuffer.allocate(length);
        }

        int left = length - body.position();
        int size = left > r.remaining() ? r.remaining() : left;
        for (int i = 0; i < size; i++) {
            body.put(r.get());
        }
        r.compact();

        // body already
        if (!body.hasRemaining()) {
            // create message
            Message msg = new Message(type, length, header.get(), body.array());
            header = null;
            body = null;
            return msg;
        }

        return null;
    }

    private boolean readHeader(ByteBuffer r) {
        if (header != null) {
            return true;
        }

        if (r.remaining() < 6) {
            return false;
        }

        header = ByteBuffer.allocate(6);
        for (int i = 0; i < 6; i++) {
            header.put(r.get());
        }
        return true;
    }

}
