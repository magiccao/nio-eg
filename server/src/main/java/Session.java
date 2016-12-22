import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by d.cao on 2016/12/21.
 */
public class Session {

    public static final int READ_BUFFER = 10; // 1 << 12; // 4kB
    private ByteBuffer r;
    private ByteBuffer w;

    private LinkedBlockingDeque<byte[]> queue;
    private Decoder decoder = new Decoder();
    private Encoder encoder = new Encoder();

    public Session() {
        r = ByteBuffer.allocateDirect(READ_BUFFER);
        queue = new LinkedBlockingDeque<>(10);
    }

    public Message read(SocketChannel sock) throws IOException {
        int n = sock.read(r);
        if (n == -1) {
            // FIN
            throw new IOException("EOF");
        }

        return decoder.decode(r);
    }

    public void write(SocketChannel sock, SelectionKey key) throws IOException {
        if (w == null) {
            byte[] data = queue.poll();
            if (data == null) {
                key.interestOps(SelectionKey.OP_READ);
                return;
            } else {
                w = encoder.encode(data);
            }
        }

        if (w.hasRemaining()) {
            if (sock.write(w) == -1) {
                throw new IOException("unexpected EOF");
            }
            if (!w.hasRemaining()) {
                w = null;
            }
        }
    }

    public void write(byte[] data) {
        try {
            queue.put(data);
        } catch (InterruptedException e) {
            System.out.println(e.getStackTrace());
        }
    }

}
