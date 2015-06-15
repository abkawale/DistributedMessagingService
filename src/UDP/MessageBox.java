/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package UDP;

import java.io.File;
import java.io.IOException;

/**
 *constructor
 * @author transcendence
 */
public class MessageBox {

    public MessageBox(String name) throws IOException{
        this.owner = name;
        messageFile = new File(name+".txt");
        messageFile.createNewFile();
    }


    private final String owner;
    private final File messageFile;
}
