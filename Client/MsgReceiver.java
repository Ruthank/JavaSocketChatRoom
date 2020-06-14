import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.io.*;


class MsgReceiver
        extends Thread
{

    Socket socket = null;
    DataInputStream din = null;
    FileWriter FW = null;
    BufferedWriter BW = null;


    public MsgReceiver()
            throws IOException
    {
        this.socket = ClientMain.s;
        din = new DataInputStream(socket.getInputStream());
    }

    public void run()
    {
        try
        {
            String str = null;
            while ((str = din.readUTF()) != null)
            {
                ExcuteMsg(str);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void ExcuteMsg(String s)
    {
        System.out.println(s);

        String str[] = s.split("#");
        String op = str[0], msg = str[1];

        if (op.equals("ADD"))
        {
            ClientGUI.model.addElement(msg);
        }

        if (op.equals("SHOW"))
        {
            ClientGUI.announce(msg);
        }

        if (op.equals("TALK"))
        {
            //聊天格式：TALK#MSG#ID
            String id = str[2];
            ClientGUI.Message_Show.append(id + "：" + msg + "\n");
        }

        if (op.equals("FILE"))
        {
            ClientGUI.model1.addElement(msg);
        }

        if (op.equals("SAVE"))
        {
            String fileName = msg;

            if (str[2].equals("NEW")) //文件不存在，新建
            {
                File fl = new File(ClientGUI.downPath+"\\"+ fileName);
                //System.out.println(ClientGUI.getFileID + fileName);
                try
                {
                    fl.createNewFile();
                    FW = new FileWriter(ClientGUI.downPath+"\\" + fileName);
                    BW = new BufferedWriter(FW);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else if (str[2].equals("OVER"))
            {
                try
                {
                    BW.close();
                    FW.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    BW.write(str[2]);
                    BW.newLine();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
