package Authenthication;

import Authenthication.User;

public class Deliverer extends User {

    public Deliverer(String username, String password, int type) {
        super(username, password, 2);
    }

}
