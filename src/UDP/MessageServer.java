package UDP;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author transcendence
 */
public class MessageServer {

    public MessageServer() {
        this.socketTable = new HashMap();
        this.userRecord = new ArrayList<>();
        this.onlineUsers = new ArrayList<>();
    }

    /**
     * main method
     *
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        MessageServer m = new MessageServer();
        m.starter();
    }

    /**
     * does all the work of the server
     *
     * @throws IOException
     */
    public void starter() throws IOException {
        SERVER_PORT_NUMBER = 13131;
        sock = new DatagramSocket(SERVER_PORT_NUMBER);

        /**
         * creating users and their message boxes
         */
        User Alice = new User("Alice");
        User Amy = new User("Amy");
        User Bob = new User("Bob");
        User Dave = new User("Dave");
        User Pam = new User("Pam");
        User Tom = new User("Tom");

        /**
         * A central array for maintaining a list of all users
         */
        userRecord.add(Alice);
        userRecord.add(Amy);
        userRecord.add(Bob);
        userRecord.add(Dave);
        userRecord.add(Pam);
        userRecord.add(Tom);
        /**
         * listen to the client and create a separate thread for that client
         */

        while (true) {
            byte[] buffer = new byte[300];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            sock.receive(receivePacket);
            serverWorker worker = new serverWorker(receivePacket);
            worker.start();
        }
    }

    class serverWorker extends Thread {

        DatagramPacket receivePacket;
        User u;
        InetAddress client_addr;
        int client_port;

        public serverWorker(DatagramPacket p) {
            this.receivePacket = p;
            client_addr = receivePacket.getAddress();
            client_port = receivePacket.getPort();
        }

        @Override
        public void run() {
            try {
                byte[] b = new byte[300];

                b = receivePacket.getData();
                ProcessInput pi = new ProcessInput();
                String req = pi.convertByteArrayToString(b);
                String sendToClient = processRequest(req);
                String exitString = "GoodBye";
                byte[] sendToClientByte = pi.convertStringToByteArray(sendToClient);
                DatagramPacket sendPacket = new DatagramPacket(sendToClientByte, sendToClientByte.length, client_addr, client_port);
                sock.send(sendPacket);

                //return;
            } catch (IOException ie) {
                System.out.println("ex 1");
            }
        }

        public String processRequest(String req) throws IOException {

            String command;

            int index1 = req.indexOf(" ");
            if (index1 != -1) {
                command = req.substring(0, index1);
            } else {
                command = req;
            }

            String retMsg = "";
            switch (command.toLowerCase()) {
                case "login":
                    retMsg = loginHandler(req, index1);
                    break;
                case "exit":
                    retMsg = exitHandler();
                    break;
                case "who":
                    retMsg = whoHandler();
                    break;
                case "check":
                    retMsg = checkHandler();
                    break;
                case "msg":
                    retMsg = msgHandler(req, index1);
                    break;
                case "read":
                    retMsg = readHandler(req, index1);
                    break;
                case "remove":
                    retMsg = removeHandler(req, index1);
                    break;
            }
            return retMsg;
        }

        /**
         * check if command is login if yes then execute it check if user exists
         * in the user file if doesn't exist in the user file then send error
         * message otherwise add it to user table, socket table and user log
         * file
         */
        private String loginHandler(String req, int index) {
            String UserName = req.substring(index + 1, req.length());
            User u = getUser(UserName);
            u.setPortNumber(client_port);
            String retmsg = "You are already Logged in or you are not a valid user!";
            Calendar c;
            c = Calendar.getInstance();
            if (userRecord.indexOf(u) != -1) {
                if (!onlineUsers.contains(u)) {
                    System.out.println(c.getTime() + UserName + "Logged in");
                    onlineUsers.add(u);
                    u.setStatus(Boolean.TRUE);
                    socketTable.put(UserName, client_port);
                    retmsg = "Welcome" + " " + UserName;
                    return retmsg;
                }
            }
            return retmsg;
        }

        /**
         * check if command is exit if yes then execute it remove entries from
         * user table and socket table and close the socket
         */
        private String exitHandler() {
            u = getUser(client_port);
            socketTable.remove(client_port);
            onlineUsers.remove(u);
            u.setStatus(Boolean.FALSE);
            String retmsg = "GoodBye";
            Calendar c;
            c = Calendar.getInstance();
            System.out.println(u.getUserName() + " logged out at " + c.getTime());
            return retmsg;
        }

        /**
         * check if the command is who if yes then execute it
         */
        private String whoHandler() {
            u = getUser(client_port);
            System.out.println(u.getUserName() + " is requesting the list of users online");
            String onlineUsr = "";
            for (User u1 : onlineUsers) {
                onlineUsr += (" " + u1.getUserName());
            }
            System.out.println("List returned to " + u.getUserName());
            return onlineUsr;
        }

        /**
         * check if the command is check if yes count the number of lines in the
         * file i.e. message box for that user
         */
        private String checkHandler() {
            InputStream is = null;
            try {
                u = getUser(client_port);
                System.out.println(u.getUserName() + " is requesting the number of messages in his/her mailbox");
                is = new BufferedInputStream(new FileInputStream(u.getUserName() + ".txt"));
                byte[] c = new byte[1024];
                Integer count = 0;
                int readChars = 0;
                try {
                    while ((readChars = is.read(c)) != -1) {
                        for (int i = 0; i < readChars; ++i) {
                            if (c[i] == '\n') {
                                ++count;
                            }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MessageServer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    return ex.getMessage();
                } finally {
                    try {
                        is.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MessageServer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        return ex.getMessage();
                    }
                }
                System.out.println("Number of messages returned to " + u.getUserName());
                return count.toString();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MessageServer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                return ex.getMessage();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MessageServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        /**
         * check if the command is msg if yes then check if the recipient is
         * online if yes then send it the message if no then write it to his/her
         * mailbox
         */
        private String msgHandler(String req, int index) {
            u = getUser(client_port);
            System.out.println(u.getUserName() + " wants to send a message");
            User receiver;
            int index2 = req.indexOf(" ", index + 1);
            String ReceiverName = req.substring(index + 1, index2);
            ReceiverName = ReceiverName.toLowerCase();
            receiver = getUser(ReceiverName);
            String Message = req.substring(index2, req.length());
            if (onlineUsers.contains(getUser(ReceiverName))) {
                try {
                    // user is online. send msg to its console
                    //Socket toSend = receiver.getSocket();
                    int recPortNo = receiver.getPortNumber();
                    DatagramSocket toSend = new DatagramSocket();
                    ProcessInput pi = new ProcessInput();
                    byte[] sendToUser = pi.convertStringToByteArray(u.getUserName() + " says " + Message);
                    DatagramPacket sendPacket = new DatagramPacket(sendToUser, sendToUser.length, recPortNo);
                    toSend.send(sendPacket);
                    System.out.println("message sent to " + receiver.getUserName());
                    return "Message Sent to " + ReceiverName;
                } catch (SocketException ex) {
                    Logger.getLogger(MessageServer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    return ex.getMessage();
                } catch (IOException ex) {
                    Logger.getLogger(MessageServer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    return ex.getMessage();
                }
            } else {
                try {
                    // user is offline. write message to its messagebox
                    File wfile = new File(ReceiverName + ".txt");
                    BufferedWriter wrt = new BufferedWriter(new FileWriter(wfile, true));
                    wrt.write(Message);
                    wrt.newLine();
                    wrt.close();
                    System.out.println("message sent to " + receiver.getUserName());
                    return "Message written to messagebox of " + ReceiverName;
                } catch (IOException ex) {
                    Logger.getLogger(MessageServer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    return ex.getMessage();
                }
            }
        }

        /**
         * check if the command is read if yes then read the message with
         * message number provided in the argument
         */
        private String readHandler(String req, int index) {
            try {
                u = getUser(client_port);
                String lineNo = req.substring(index + 1, req.length());
                int lineNum = Integer.parseInt(lineNo);
                int count = 0;
//                lineNum--;
                System.out.println(u.getUserName() + " wants to read a message");
                BufferedReader rdr = new BufferedReader(new FileReader(u.getUserName() + ".txt"));
                try {
                    while (count < lineNum) {
                        rdr.readLine();
                        count++;
                    }
                } catch (IOException ie) {
                    return "Message number does not exist";
                }
                String retmsg = rdr.readLine();
                return retmsg;
            } catch (IOException ex) {
                Logger.getLogger(MessageServer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                return ex.getMessage();
            }
        }

        /**
         * check if the command is remove if yes then remove the message with
         * message number provided in the argument
         */
        private String removeHandler(String req, int index) {
            try {
                u = getUser(client_port);
                System.out.println(u.getUserName() + " wants to remove a message");
                ArrayList<String> msgs = new ArrayList<>();
                String lineNo = req.substring(index + 1, req.length());
                int lineNum = Integer.parseInt(lineNo);
                int count = 0;
                lineNum--;
                String temp;
                BufferedReader rdr = new BufferedReader(new FileReader(u.getUserName().toLowerCase() + ".txt"));

                while ((temp = rdr.readLine()) != null) {
                    if (count != lineNum) {
                        msgs.add(temp);
                        count++;
                    } else {
                        rdr.readLine();
                        count++;
                    }
                }
                rdr.close();
                if (count < lineNum) {
                    return "The file did not have " + lineNum + " mseeages";
                }

//                String FileName = u.getUserName().toLowerCase()+".txt";
//                File f1 = new File(FileName);
                BufferedWriter wrt = new BufferedWriter(new FileWriter(u.getUserName().toLowerCase() + ".txt"));
                while (!msgs.isEmpty()) {

                    wrt.write(msgs.get(0));
                    wrt.write("\n");
                    msgs.remove(0);
                }
                wrt.close();
                return "Message deleted";
            } catch (IOException ex) {
                Logger.getLogger(MessageServer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                return ex.getMessage();
            }
        }

        /**
         * given a user name the method returns a user object
         *
         * @param UserName
         * @return
         */
        public User getUser(String UserName) {
            for (User u : userRecord) {
                if (u.getUserName().equalsIgnoreCase(UserName)) {
                    return u;
                }
            }
            return null;
        }

        public User getUser(int portNo) {
            for (User u : userRecord) {
                if (u.getPortNumber() == portNo) {
                    return u;
                }
            }
            return null;
        }
    }
    /**
     * private and public variables
     */

    public static DatagramSocket sock;
    public static InetAddress SERVER_IP_ADDRESS;
    public static int SERVER_PORT_NUMBER;
    private final ArrayList<User> onlineUsers;
    private final ArrayList<User> userRecord;
    private final HashMap socketTable;
}
