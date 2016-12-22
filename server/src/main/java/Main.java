import java.io.IOException;

/**
 * Created by d.cao on 2016/9/20.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        new Thread(new NIOServer(8080, "NIO-Test-Server")).start();
    }

}