import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by d.cao on 2016/12/21.
 */
public class NIOServer implements Runnable {

    private Selector selector;
    private ExecutorService service;
    private String name;
    private int port;
    private boolean stop;

    NIOServer(int port, String name) throws IOException {
        this.port = port;
        this.name = name;
        ServerSocketChannel listener = ServerSocketChannel.open();
        listener.socket().bind(new InetSocketAddress(port), 1024);
        listener.socket().setReuseAddress(true);
        listener.socket().setSoTimeout(3000);
        listener.configureBlocking(false);
        selector = Selector.open();
        listener.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void stop() {
        stop = true;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(name);
        service = Executors.newScheduledThreadPool(10);

        while (!stop) {
            try {
                selector.select();
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
                break;
            }
            Set keys = selector.selectedKeys();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                if (!handleIO(key)) {
                    break;
                }
            }
        }

        // clean work
        try {
            selector.close();
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
        service.shutdown();
    }

    private boolean handleIO(SelectionKey key) {
        if (key.isAcceptable()) { // accept
            try {
                SocketChannel sock = ((ServerSocketChannel) key.channel()).accept();
                sock.configureBlocking(false);
                Session session = new Session();
                sock.register(selector, SelectionKey.OP_READ, session);
            } catch (ClosedChannelException | NotYetBoundException e) {
                // no need try exception
                System.out.println(e.getStackTrace());
                return false;
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
                return true;
            }
        }
        if (key.isReadable()) { // read
            SocketChannel sock = (SocketChannel) key.channel();
            Session session = (Session) key.attachment();
            try {
                Message msg = session.read(sock);
                if (msg != null) {
                    System.out.println("receive msg: " + msg.toString());
                    service.submit(new Task(msg, new ResponseWriter(selector, key, session)));
                }
            } catch (IOException e) {
                try {
                    sock.close();
                } catch (IOException e1) {
                    System.out.println(e1.getStackTrace());
                }
                return true;
            }
        }
        if (key.isWritable()) { // write
            SocketChannel sock = (SocketChannel) key.channel();
            Session session = (Session) key.attachment();
            try {
                session.write(sock, key);
            } catch (IOException e) {
                try {
                    sock.close();
                } catch (IOException e1) {
                    System.out.println(e1.getStackTrace());
                }
            }
        }
        return true;
    }

}
