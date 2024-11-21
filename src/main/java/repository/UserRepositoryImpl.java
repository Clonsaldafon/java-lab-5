package repository;

import db.DBContext;
import model.User;

public class UserRepositoryImpl implements UserRepository {

    private final DBContext _dbContext;

    public UserRepositoryImpl() {
        _dbContext = new DBContext();
    }

    @Override
    public void add(User user) {
        String query = "INSERT INTO users (login, password, email) VALUES (?, ?, ?)";

        _dbContext.execUpdate(query, user.getLogin(), user.getPass(), user.getEmail());
    }

    @Override
    public User get(String login) {
        String query = "SELECT * FROM users WHERE login = ?";

        User user = _dbContext.execQuery(query, set -> new User(
                        set.getString("login"),
                        set.getString("password"),
                        set.getString("email")),
                login);

        return user;
    }
}
