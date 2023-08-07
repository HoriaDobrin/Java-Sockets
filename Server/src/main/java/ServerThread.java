import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
public class ServerThread extends Thread {
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    //ArrayList<User> users = new ArrayList<User>();

    public ServerThread(Socket socket) {
        try {
            //For receiving and sending data
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        execute();
    }

    private void execute() {
        boolean openSwitch = true;
        while (openSwitch){
            String message = Receive();

            switch (message) {
            case "sign up":
                Sign_Up();
                break;
            case "log in":
                openSwitch = Log_In();
                break;
            case "forgot password":
                Forgot_pass();
                break;
            default:
                Send("Please type one of the provided options");
                break;
        };
        }
    }

    public void Send(String message) {
        try {
            this.out.writeObject(new Packet(message));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public String Receive() {
        Packet receivedPacket = null;
        try {
            receivedPacket = (Packet) this.in.readObject();
            System.out.println("Received: " + receivedPacket.message);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return receivedPacket.message;
    }

    public void Sign_Up() {
        String user,pass,quest,ans;
        Send("Please insert your username:\n");
        user = Receive();
        Send("Please insert your password:\n");
        pass = Receive();
        Send("Please insert your safety question:\n");
        quest = Receive();
        Send("Please insert your answer to the safety quesion:\n");
        ans = Receive();

        if (!Validate_user(user)) {
            User signup_user = new User(user,pass,quest,ans);
//            signup_user.setUsername(user);
//            signup_user.setPassword(pass);
//            signup_user.setQuestion(quest);
//            signup_user.setAnswer(ans);
            RuntimeUsers.INSTANCE.addUser(signup_user);
            initial_page("");
        }
        else
        {
            Send("Your username already exists, please choose Forgot password\n" +
                    "Please choose one of the following :\n" +
                    "For Log In please type log in.\n" +
                    "For Sign Up please type sign up.\n" +
                    "For Forgot Password please type forgot pass.\n");
        }

    }

    public void initial_page(String message){
        Send(message + "Please choose one of the following :\n" +
                "For Log In please type log in.\n" +
                "For Sign Up please type sign up.\n" +
                "For Forgot Password please type forgot pass.\n");
    }


    public boolean Validate_user(String user)
    {
        if ( RuntimeUsers.INSTANCE.isInList(user))
            return true;
        return false;


    }

    public boolean Validate_Ans(String question, String answer)
    {
        String ans;
        Send("Please answer your following Question : " + question);
        ans = Receive();

        if (ans.equals(answer))
            return true;
        return false;
    }
    public void Forgot_pass(){
        String user;
        String question = null,answer = null, newpassword;
        int i ;
        Send("Please enter your username");
        user = Receive();
        int index = RuntimeUsers.INSTANCE.getIndex(user);
        question = RuntimeUsers.INSTANCE.getUser(index).getQuestion();
        answer = RuntimeUsers.INSTANCE.getUser(index).getAnswer();

        if (Validate_Ans(question,answer))
        {
            Send("Please enter your new password:");
            newpassword = Receive();
            RuntimeUsers.INSTANCE.getUser(index).setPassword(newpassword);
            initial_page("");
        }
        else {
            Send("Wrong answer to your question\n" +
                    "Please choose one of the following :\n" +
                    "For Log In please type log in.\n" +
                    "For Sign Up please type sign up.\n" +
                    "For Forgot Password please type forgot pass.\n");
        }
    }
    public boolean Log_In(){
        String user,pass;

        Send("Please insert your username:\n");
        user = Receive();
        Send("Please insert your password:\n");
        pass = Receive();

        Validate_logIn(user,pass);

        if (Validate_logIn(user,pass))
        {
            Send("You have successfully loggged in");
            return false;
        }
        else {
            initial_page("You have entered the wrong credentials.");
            return true;
        }
    }

    public boolean Validate_logIn(String user, String pass){
        //fctie de validate pentru log in
        for (User usr :  RuntimeUsers.INSTANCE.userList )
        {
            if (usr.getUsername().equals(user)  && usr.getPassword().equals(pass))
            {
                return true;
            }
        }
        return false;

    }
}
