import java.io.*;
import java.net.Socket;
import java.util.Scanner;

class MsgSender
        extends Thread
{

    Socket socket = null;
    static public DataOutputStream dout = null;
    Scanner in = new Scanner(System.in);

    public MsgSender()
            throws IOException
    {
        this.socket = ClientMain.s;
        dout = new DataOutputStream(socket.getOutputStream());
    }

    public static void sendMsg(String msg)
    {
        try
        {
            dout.writeUTF(msg);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void sendFile(String filePath, String fileName)
            throws FileNotFoundException, IOException
    {
        dout.writeUTF("3#"+fileName+"#"+"NEW");

        InputStream FI = new FileInputStream(filePath);
        InputStreamReader IS = new InputStreamReader(FI, "UTF-8");
        BufferedReader BR = new BufferedReader(IS);

        String buf = null;
        while ((buf = BR.readLine()) != null)
        {
            dout.writeUTF("3#"+fileName+"#"+buf);
        }
        dout.writeUTF("3#"+fileName+"#OVER");

        BR.close();
        IS.close();
        FI.close();
    }

    public void run()
    {
        while(true)
        {

        }
    }
}
