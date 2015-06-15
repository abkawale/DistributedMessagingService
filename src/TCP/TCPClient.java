/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TCP;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author transcendence
 */
public class TCPClient{

    public static void main(String[] args) throws IOException{
            /**
             * initializing socket
             */
            InetAddress server_addr = InetAddress.getByName("localhost");
            Socket client = new Socket(server_addr, 34543);

            tcpreciever reciever=new tcpreciever(client);
            reciever.start();
            /**
             * input from user
             */
            try{
                ProcessInput pi =new ProcessInput();
                Scanner in1 = new Scanner(System.in);
                OutputStream out=client.getOutputStream();
                while(true){
                    String input=in1.nextLine();

                    if (input.length()>255)
                        System.out.println("Message to long to be accepted");
                    else{
                        out.write(pi.convertStringToByteArray(input));
                    }
                }
        }catch(IOException ie){}
    }
}

class tcpreciever extends Thread{
Socket client;
    public tcpreciever(Socket client) {
        this.client=client;
    }

    @Override
    public void run() {
        byte[] b =new byte[300];
        try{
            InputStream in =client.getInputStream();
            while(true){
                ProcessInput pi = new ProcessInput();
                in.read(b);
                String serverMsg = pi.convertByteArrayToString(b);
                String exitMsg = "GoodBye";
                System.out.println("SERVER SAYS: " + serverMsg);
                if (serverMsg.equals(exitMsg))
                    break;
            }
        }catch(IOException ie){}
        try {
            client.close();
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(tcpreciever.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
