import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * Created by d.cao on 2016/12/21.
 */
public class ResponseWriter implements Callback {

    private Selector selector;
    private SelectionKey key;
    private Session session;

    public ResponseWriter(Selector selector, SelectionKey key, Session session) {
        this.selector = selector;
        this.key = key;
        this.session = session;
    }

    @Override
    public void write(byte[] data) {
        key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
        session.write(data); // maybe block
        selector.wakeup();
    }

}
