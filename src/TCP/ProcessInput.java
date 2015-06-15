package TCP;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author transcendence
 */
public class ProcessInput {

   
    public byte[] convertStringToByteArray(String s){
        byte[] sendPacket;
        Integer len = s.length();
        String leng;
        if (len<10)
            leng = "00"+len;
        else if (len<100)
             leng = "0"+len;
        else
             leng = ""+len;
        sendPacket = (leng+s).getBytes();

        return sendPacket;
    }

    public String convertByteArrayToString(byte[] b){
        byte[] intval = new byte[3];
         String receivePacket = "";
        for (int i = 0; i < 3; i++)
            intval[i] = b[i] ;
        String len = new String(intval);
        int i = Integer.parseInt(len);
        for (int j = 0; j < i ; j++)
            receivePacket += (char)b[j+3];
       // System.out.println(receivePacket);
        return receivePacket;
    }
}
