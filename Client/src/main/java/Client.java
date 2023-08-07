import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static final int PORT = 8085;
    public int state = 1;

    //1- pagina principala
    //2- log in
    //3- sign up
    //4- forgot pass
    public void start() throws Exception {
        System.out.println("Please choose one of the following :\n" +
                "For Log In please type log in.\n" +
                "For Sign Up please type sign up.\n" +
                "For Forgot Password please type forgot password.\n");
        Socket socket = null;

        //For receiving and sending data
        boolean isClose = false;
        socket = new Socket("localhost", PORT);
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        while (!isClose) {

            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();


            if (state == 1) {
                if (Validation(message)) {
                    outputStream.writeObject(new Packet(message));
                }
            }else if (state == 2)
            {
                outputStream.writeObject(new Packet(message));
            }else if (state == 3)
            {
                outputStream.writeObject(new Packet(message));
            }else if (state == 4)
            {
                outputStream.writeObject(new Packet(message));
            }


            Packet recivePacket = (Packet) inputStream.readObject();
            System.out.println(recivePacket.message);
        }
        socket.close();
    }

    public boolean Validation(String message) {
        switch (message) {
            case "log in":
                state = 2;
                return true;
            case "sign up":
                state = 3;
                return true;
            case "forgot password":
                state = 4;
                return true;
            default:
                System.out.println("Please insert a valid statement");
                state = 1;
                return false;
        }
    }
}
