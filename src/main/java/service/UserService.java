package service;

import model.User;

public interface UserService {

    void add(User user);

    User get(String login);

}
