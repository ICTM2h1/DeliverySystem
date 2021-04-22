package Authenthication;

import Authenthication.User;

public class Administrator extends User {

    public Administrator(String username, String password, String type) {
        super(username, password, 1);
    }

}
