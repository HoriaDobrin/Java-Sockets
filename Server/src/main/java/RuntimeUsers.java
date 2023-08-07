import java.util.ArrayList;

public enum RuntimeUsers {
    INSTANCE;
    public static ArrayList<User> userList = new ArrayList<>();

    public void addUser(User u){
        userList.add(u);
    }

    public boolean contains(User u){
        return userList.contains(u);
    }

    public boolean isInList(String username){
        for (User user : userList)
            if (user.getUsername().equals(username))
                return true;

        return false;
    }
    public int getIndex(String username){
        for (int i = 0; i < userList.size(); i++)
            if (userList.get(i).getUsername().equals(username))
                return i;

        return -1;
    }

    public User getUser(int index){
        return userList.get(index);
    }
}
