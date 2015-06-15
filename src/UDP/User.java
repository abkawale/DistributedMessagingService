/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package UDP;

import java.io.IOException;

/**
 *
 * @author transcendence
 */
public class User {

    /**
     * constructor
     * @param name
     */
    public User(String name) throws IOException{
        this.myName = name;
        this.mystatus = false;
        myBox = new MessageBox(name);
    }

    /**
     * getter and setter functions
     */
    public String getUserName(){
        return this.myName;
    }

    public MessageBox getMessageBox(){
        return this.myBox;
    }

    public boolean getStatus(){
        return this.mystatus;
    }

    public void setStatus(Boolean s){
        this.mystatus = s;
    }
    public int getPortNumber(){
        return this.port;
    }
    public void setPortNumber(int portNumb){
        this.port = portNumb;
    }

     /**
     * private variables
     */
    private String myName;
    private boolean mystatus;
    private int port;
    private MessageBox myBox;
}
