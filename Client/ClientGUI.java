import java.awt.EventQueue;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.*;
import java.net.Socket;
import java.util.*;
import java.io.*;



//客户端界面类
public class ClientGUI {



    private JFrame frame;
    private JTable table;
    public static JTextArea MsgInputArea,
            Message_Show;

    private JLabel title;
    public static JList FriendList,
                        FileList;

    public static DataModel model,
                            model1;

    private JTextField textField;
    private JButton btnNewButton;
    public static JLabel Server_Show;

    public static String toID = null;
    public static Integer type = 0;
    public static String filePath = null;
    public static String fileName = null;
    public static String getFileID = null;
    public static String downPath = null;

    /**
     * Create the application.
     */
    public ClientGUI(Socket s) {

        initialize();
        frame.setVisible(true);

        try
        {
            new MsgSender().start(); //发送消息线程
            new MsgReceiver().start(); //接收消息线程
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void announce(String msg)
    {
        Server_Show.setText(msg);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setFont(new Font("新宋体", Font.PLAIN, 18));
        frame.setTitle(ClientMain.MyName);
        frame.setBounds(100, 100, 931, 681);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        //消息输入区
        MsgInputArea = new JTextArea();
        MsgInputArea.setFont(new Font("微软雅黑 Light", Font.BOLD, 17));
        MsgInputArea.setBounds(29, 426, 537, 110);
        frame.getContentPane().add(MsgInputArea);

        //聊天窗口标题
        title = new JLabel("");
        title.setBounds(29, 372, 537, 52);
        frame.getContentPane().add(title);

        table = new JTable();
        table.setBounds(0, 0, 1, 1);
        frame.getContentPane().add(table);

//        //好友列表
//        FriendList = new JList();
//        FriendList.setBounds(620, 70, 234, 479);
//        frame.getContentPane().add(FriendList);

        //聊天显示区
        Message_Show = new JTextArea();
        Message_Show.setFont(new Font("微软雅黑 Light", Font.PLAIN, 16));
        Message_Show.setEditable(false);
        Message_Show.setBounds(29, 19, 537, 338);
        frame.getContentPane().add(Message_Show);

        JScrollPane TextscrollPane = new JScrollPane(Message_Show);
        TextscrollPane.setBounds(29, 19, 537, 338);
        frame.getContentPane().add(TextscrollPane);

        //发送消息按钮
        JButton Send_Button = new JButton("\u53D1\u9001\u6D88\u606F");
        Send_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                String Msg = MsgInputArea.getText();
                MsgInputArea.setText("");
                Message_Show.append("发送消息给"+toID + "：" + Msg + '\n');
                try
                {
                    MsgSender.dout.writeUTF(type.toString()+"#"+toID+'#'+Msg);
                }
                catch(IOException e)
                {

                }
            }
        });
        Send_Button.setBounds(30, 560, 113, 36);
        frame.getContentPane().add(Send_Button);

        //好友列表
        model = new DataModel(); //创建DataModel对象
        FriendList = new JList(model); //创建带有model的JList
        FriendList.setBounds(620, 70, 235, 280);
        FriendList.setBorder(BorderFactory.createTitledBorder("好友列表")); //设置列表框标题
        FriendList.addMouseListener(new MouseAdapter() //列表框添加鼠标事件
        {
            public void mousePressed(MouseEvent e)
            {
//                int index = FriendList.getSelectedIndex() + 1;
//                Object path[] = model.toArray();
                toID = FriendList.getSelectedValue().toString();
                if (toID.equals("公共聊天室"))
                {
                    type = 2;
                }
                else
                {
                    type = 1;
                }
                title.setText("发送消息给："+toID);
            }
        });
        JScrollPane scrollPane = new JScrollPane(FriendList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS); //创建带有JList对象的滚动条面板
        frame.getContentPane().add(FriendList);
        model.addElement("公共聊天室");


        //文件列表
        model1 = new DataModel(); //创建DataModel对象
        FileList = new JList(model1); //创建带有model的JList
        FileList.setBounds(620, 370, 235, 170);
        FileList.setBorder(BorderFactory.createTitledBorder("文件列表")); //设置列表框标题
        FileList.addMouseListener(new MouseAdapter() //列表框添加鼠标事件
        {
            public void mousePressed(MouseEvent e)
            {
                getFileID = FileList.getSelectedValue().toString();
            }
        });
        JScrollPane scrollPane1 = new JScrollPane(FileList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS); //创建带有JList对象的滚动条面板
        frame.getContentPane().add(FileList);

        //好友ID输入区
        textField = new JTextField();
        textField.setBounds(619, 15, 163, 36);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        //添加好友按钮
        btnNewButton = new JButton("\u6DFB\u52A0\u597D\u53CB");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String NewUserName = textField.getText();
                try
                {
                    MsgSender.dout.writeUTF("4" + '#' + NewUserName + "#s");
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
        btnNewButton.setBounds(787, 18, 105, 29);
        frame.getContentPane().add(btnNewButton);

        //系统消息区域
        Server_Show = new JLabel("");
        Server_Show.setBounds(619, 568, 235, 42);
        frame.getContentPane().add(Server_Show);

        //选择文件
        JButton selectFile = new JButton("\u9009\u62E9\u6587\u4EF6");
        selectFile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
                jfc.showDialog(new JLabel(), "选择");
                File file=jfc.getSelectedFile();
                if(file.isDirectory())
                {
                    title.setText("选择的是文件夹，无法发送。");
                }
                else if(file.isFile())
                {
                    filePath = file.getAbsolutePath();
                    title.setText("文件:"+filePath);
                }
                fileName = jfc.getSelectedFile().getName();
            }
        });
        selectFile.setBounds(230, 560, 113, 36);
        frame.getContentPane().add(selectFile);

        JButton sendFile = new JButton("\u53D1\u9001\u6587\u4EF6");
        sendFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (filePath == null)
                {
                    title.setText("未选择文件！");
                }
                else
                {
                    try
                    {
                        MsgSender.sendFile(filePath, fileName);
                    }
                    catch (IOException e2)
                    {
                        e2.printStackTrace();
                    }

                }
            }
        });
        sendFile.setBounds(430, 560, 113, 36);
        frame.getContentPane().add(sendFile);

        //下载文件
        JButton DownloadFile = new JButton("\u4E0B\u8F7D\u6587\u4EF6");
        DownloadFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
                jfc.showDialog(new JLabel(), "选择");
                File file=jfc.getSelectedFile();
                if(file.isDirectory())
                {
                    downPath = file.getAbsolutePath();
                    try
                    {
                        MsgSender.dout.writeUTF("5#" + getFileID + "#" + ClientMain.MyName);
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                else if(file.isFile())
                {
                    title.setText("请选择文件夹！");
                }


            }
        });
        DownloadFile.setBounds(700, 560, 113, 36);
        frame.getContentPane().add(DownloadFile);
    }

    class DataModel extends DefaultListModel<Object>
    {
        ArrayList<String> NameList = new ArrayList<>();

        public DataModel()
        {
//            try
//            {
//                InputStream FI = new FileInputStream(Path);
//                InputStreamReader IS = new InputStreamReader(FI, "UTF-8");
//                BufferedReader BR = new BufferedReader(IS);
//
//                String buf = null;
//                try
//                {
//                    while ((buf = BR.readLine()) != null)
//                    {
//                        addElement(buf);
//                    }
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//            catch (FileNotFoundException fn)
//            {
//                fn.printStackTrace();
//            }
//            catch (UnsupportedEncodingException ue)
//            {
//                ue.printStackTrace();
//            }

        }

//        public void addFriend(String id)
//        {
//            addElement(id);
//        }
    }

}

