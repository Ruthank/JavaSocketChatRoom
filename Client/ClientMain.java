import java.io.IOException;
import java.net.Socket;

public class ClientMain
{
    static String MyName = null;
    public static Socket s = null;

    public static void main(String[] args)
            throws IOException
    {
        ClientLogin cl = new ClientLogin(s);
    }
}
