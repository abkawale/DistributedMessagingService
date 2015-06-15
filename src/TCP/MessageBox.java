/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TCP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *constructor
 * @author transcendence
 */
public class MessageBox {
    /**
     * constructor
     * @param name
     * @throws IOException
     */
    public MessageBox(String name) throws IOException{
        this.owner = name;
        try{
        BufferedReader rdr = new BufferedReader(new FileReader(name+".txt"));
        }catch (IOException ie){
            messageFile = new File(name+".txt");
            messageFile.createNewFile();
        }
    }

    /**
     * private variables
     */
    private String owner;
    private ArrayList<Message> msgList = new ArrayList<Message>();
    private File messageFile;
}
