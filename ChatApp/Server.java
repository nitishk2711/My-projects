import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Server extends JFrame {

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

     //Decleare the components
    //it is basically the heading of the UI
    private JLabel heading=new JLabel("Server Area");
    //it is the text area where we type the message
    private JTextArea messageArea=new JTextArea();
    //it the text feild where the read the data or the input
    private JTextField messageInput=new JTextField();

    //font type 
    private Font font =new Font("Roboto", Font.PLAIN,20);

    // constructor
    public Server() {

        try {

            //if we use this chat application code to talk to system otherwise same system port number should be same in both code
            server = new ServerSocket(7777);
            System.out.println("server is ready to accept connection");
            System.out.println("waiting.....");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvent();
            startReading();
            // startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //it is used to handle the events int message  area and the message input area
    //we are using the keylidtener interface and the override tye unimplement methods
    private void handleEvent(){
        messageInput.addKeyListener(new KeyListener() {

            //these three is a method to do all the type after the any activity
            //means the after we type the key and pressed the key otherwise we realease the key 
            @Override
            public void keyTyped(KeyEvent e) {
               
            }

            @Override
            public void keyPressed(KeyEvent e) {
               
            }

            //if we used the keypresed it is give the realease the code
            @Override
            public void keyReleased(KeyEvent e) {
                // System.out.println("key realeased"+e.getKeyCode());
                if(e.getKeyCode()==10){
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me: "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }
            
        });
    }
    
    private void createGUI()
    {
        //create gui.....
        //set the tittle for the gui
        this.setTitle("Server Messanger[END]");
        //size of UI
        this.setSize(600, 700);
        //this method is used to center the our window
        this.setLocationRelativeTo(null);
        //it is set to close the our program
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for component
        //set the font for the heading, message area and the messageinput
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        //set the heading tag to our component to center to center in our window
        heading.setIcon(new ImageIcon("logo.png"));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        
        heading.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //this line is used to anyone not type anything in messagearea and not anything edi in our message 
        messageArea.setEditable(false);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        //code for message input 
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        
        

        //set for message input
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        

        //set the layout frame and set the border
        this.setLayout(new BorderLayout());

        //adding the component to frame for the every sides
        this.add(heading, BorderLayout.NORTH);

        //this line is used to set the scroll bar in our message area
        //because of this message area is full so scroll it and see the next message
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void startReading() {
        // thread is used to read the data form the socket/client
        Runnable r1 = () -> {
            System.out.println("reader started..");
            try {

                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit"))
                    {
                        System.out.println("Client terminated the chat");
                        JOptionPane.showMessageDialog(this, "server Terminated the Chat");
                        messageInput.setEditable(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Client:- " + msg);
                    messageArea.append("Client: "+msg+"\n");
                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection closed");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {

        // thread - data take the user and give the data to client
        Runnable r2 = () -> {
            System.out.println("writer started...");
            try {

                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }
                    System.out.println("Connection Lost");

                }
            } catch (Exception e) {
                System.out.println("Connection closed: ");
            }
        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("This is Server .. going to start");
        new Server();

    }
}