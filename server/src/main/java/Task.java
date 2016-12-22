/**
 * Created by d.cao on 2016/12/21.
 */
public class Task implements Runnable {

    private Message msg;
    private Callback callback;

    public Task(Message msg, Callback callback) {
        this.msg = msg;
        this.callback = callback;
    }

    @Override
    public void run() {
        // Here, u can do something logic works
        // ...

        byte[] data = msg.toString().getBytes();
        callback.write(data);
    }

}
