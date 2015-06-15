/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TCP;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author transcendence
 */
public class Message {

    /*
     * constructor
     */
    public Message(int sid, int rid, String msg){
        this.senderID = sid;
        this.receiverID = rid;
        this.msssageText = msg;
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.DateString = dateFormat.format(date);

    }

    /*
     * getter functions
     */
    public int getSenderID(){
        return this.senderID;
    }

    public int getReceiverID(){
        return this.receiverID;
    }

    public String getMessage(){
        return this.msssageText;
    }

    public String getDate(){
        return this.DateString;
    }

    public int getMessageID(){
        return this.messageID;
    }

    /*
     * message id setter
     */
    public void setMessageID(int id){
        this.messageID = id;
    }

    /*
     * private variables
     */
    private final int senderID;
    private final int receiverID;
    private final String msssageText;
    private int messageID;
    private final String DateString;
}
