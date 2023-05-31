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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

//create the User interface using jrame
public class Client extends JFrame{
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //Decleare the components
    //it is basically the heading of the UI
    private JLabel heading=new JLabel("Client Area");
    //it is the text area where we type the message
    private JTextArea messageArea=new JTextArea();
    //it the text feild where the read the data or the input
    private JTextField messageInput=new JTextField();

    //font type 
    private Font font =new Font("Roboto", Font.PLAIN,20);

    // constructor of client 
    public Client() {
        try {
            System.out.println("Sending request to server");

            //and here pass the IP Address to communicate to each other 
            //this chat app is work on the same networking connection
            //i am not use to this application to connect to different application and may be this is our drawback.
            socket = new Socket("192.168.0.112", 7777);
            System.out.println("connection done..");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            //it basically calling the method to create the UI from the user
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
        this.setTitle("Client Messanger[END]");
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

    //start reading method to read the message
    public void startReading() {
        // thread is used to read the data and give the data to the client
        Runnable r1 = () -> {
            System.out.println("reader started...");
            try {

                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "server Terminated the Chat");
                        messageInput.setEditable(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Server:- " + msg);
                    messageArea.append("Server: " +msg+"\n");

                }
            } catch (Exception e) {
            //    e.printStackTrace();
            System.out.println("Connection Closed");
            }

        };

        new Thread(r1).start();

    }

    //start writing send method

    public void startWriting() {
        // thread - dtaa take the user and ive the data to the server

        Runnable r2 = () -> {

            try {

                while (!socket.isClosed()) {
                    System.out.println("writer started...");

                    // it is used to take the input from the user help of Bufferreader stream
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }

                }
            } catch (Exception e) {
                System.out.println("Connection Closed");
            }
        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        System.out.println("this is client");
        new Client();
    }
}
