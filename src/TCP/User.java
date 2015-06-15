/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TCP;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author transcendence
 */
public class User {

    /**
     * constructor
     * @param name
     * @throws java.io.IOException
     */
    public User(String name) throws IOException{
        this.myName = name;
        this.status = false;
        myBox = new MessageBox(name);
    }

    /**
     * getter functions
     * @return 
     */
    public String getUserName(){
        return this.myName;
    }

    public MessageBox getMessageBox(){
        return this.myBox;
    }

    public boolean getStatus(){
        return this.status;
    }

    public void setStatus(Boolean s){
        this.status = s;
    }

    public void setSocket(Socket c){
        this.MySocket = c;
    }

    public Socket getSocket(){
        return this.MySocket;
    }

     /**
     * private variables
     */
    private final String myName;
    private boolean status;
    private Socket MySocket;
    private final MessageBox myBox;
}
