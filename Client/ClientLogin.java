import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class ClientLogin {

    private JFrame frame;
    private JTextField textField;
    private JLabel label;
    private JLabel lblNewLabel;
    private JTextField ServerIP;
    private JTextField ServerPort;
    private DataOutputStream dout = null;
    private DataInputStream din = null;
    private Socket socket = null;

    /**
     * Create the application.
     */
    public ClientLogin(Socket socket) {
        initialize(socket);
        frame.setVisible(true);
    }

    void ConnectServer()
    {
        try
        {
            ClientMain.s = new Socket(ServerIP.getText(), Integer.valueOf(ServerPort.getText()));
            this.socket = ClientMain.s;
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
        }
        catch(java.rmi.UnknownHostException un)
        {
            un.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(Socket socket)
    {
        frame = new JFrame();
        frame.setBounds(100, 100, 514, 425);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        textField = new JTextField();
        textField.setFont(new Font("宋体", Font.PLAIN, 20));
        textField.setBounds(138, 225, 236, 40);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        label = new JLabel("\u7528\u6237\u540D\uFF1A");
        label.setFont(new Font("楷体", Font.BOLD, 21));
        label.setBounds(25, 220, 98, 48);
        frame.getContentPane().add(label);

        JButton Login_button = new JButton("\u767B\u5F55");
        Login_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0)
            {
                ConnectServer();
                try
                {
                    dout.writeUTF(textField.getText());
                    ClientMain.MyName = textField.getText();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                new ClientGUIThread(socket).start();
                frame.setVisible(false);
            }
        });
        Login_button.setFont(new Font("幼圆", Font.PLAIN, 20));
        Login_button.setBounds(175, 306, 140, 29);
        frame.getContentPane().add(Login_button);

        lblNewLabel = new JLabel("");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(138, 512, 200, 48);
        frame.getContentPane().add(lblNewLabel);


        ServerIP = new JTextField();
        ServerIP.setText("127.0.0.1");
        ServerIP.setEditable(false);
        ServerIP.setBounds(138, 92, 236, 40);
        frame.getContentPane().add(ServerIP);
        ServerIP.setColumns(10);

        ServerPort = new JTextField();
        ServerPort.setText("9999");
        ServerPort.setEditable(false);
        ServerPort.setColumns(10);
        ServerPort.setBounds(138, 159, 236, 40);
        frame.getContentPane().add(ServerPort);

        JLabel label_2 = new JLabel("\u7AEF\u53E3\u53F7\uFF1A");
        label_2.setFont(new Font("楷体", Font.BOLD, 21));
        label_2.setBounds(25, 157, 98, 48);
        frame.getContentPane().add(label_2);

        JLabel label_3 = new JLabel("\u670D\u52A1\u5668\u5730\u5740\uFF1A");
        label_3.setFont(new Font("楷体", Font.BOLD, 21));
        label_3.setBounds(15, 86, 140, 48);
        frame.getContentPane().add(label_3);

        JButton Change_1 = new JButton("\u4FEE\u6539");
        Change_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //修改服务器地址
                ServerIP.setEditable(true);
            }
        });
        Change_1.setBounds(389, 97, 85, 29);
        frame.getContentPane().add(Change_1);

        JButton Change_2 = new JButton("\u4FEE\u6539");
        Change_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //修改端口号
                ServerPort.setEditable(true);
            }
        });
        Change_2.setBounds(389, 162, 85, 29);
        frame.getContentPane().add(Change_2);

    }
}


class ClientGUIThread
        extends Thread
{
    private Socket s = null;

    public ClientGUIThread(Socket soc)
    {
        this.s = soc;
    }

    public void run()
    {
        new ClientGUI(s);
    }
}
