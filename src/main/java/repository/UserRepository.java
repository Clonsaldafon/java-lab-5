package repository;

import model.User;

public interface UserRepository {

    void add(User user);

    User get(String login);

}
