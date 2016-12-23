/**
 * Created by d.cao on 2016/12/23.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        client.connect("127.0.0.1", 8080);
    }

}
