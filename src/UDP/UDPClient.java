/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package UDP;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


/**
 *
 * @author transcendence
 */
public class UDPClient{

    public static void main(String[] args) throws IOException{
            /**
             * initializing socket
             */

            DatagramSocket client = new DatagramSocket();
            InetAddress server_addr = InetAddress.getByName("localhost");

            UDPreciever reciever=new UDPreciever(client);
            reciever.start();
            /**
             * input from user
             */
            try{
                ProcessInput pi =new ProcessInput();
                Scanner in1 = new Scanner(System.in);
                byte inputByte[] = new byte[300];
                while(true){
                    String input=in1.nextLine();
                    inputByte = pi.convertStringToByteArray(input);
                    if (input.length()>255)
                        System.out.println("Message to long to be accepted");
                    else{
                        DatagramPacket sendPacket1 = new DatagramPacket(inputByte, inputByte.length, server_addr, 13131);
                        client.send(sendPacket1);
                    }
                }
        }catch(IOException ie){}
    }
}

class UDPreciever extends Thread{

    public UDPreciever(DatagramSocket client) {
        this.client=client;
    }

    @Override
    public void run() {
        byte[] b =new byte[300];
        try{
             DatagramPacket receivePacket1 = new DatagramPacket(b, b.length);
            while(true){
                ProcessInput pi = new ProcessInput();
                client.receive(receivePacket1);
                String serverMsg = pi.convertByteArrayToString(b);
                String exitMsg = "GoodBye";
                System.out.println("SERVER SAYS: " + serverMsg);
                if (serverMsg.equals(exitMsg))
                    break;
            }
        }catch(IOException ie){}
        client.close();
        System.exit(0);
    }
    private DatagramSocket client;
}
