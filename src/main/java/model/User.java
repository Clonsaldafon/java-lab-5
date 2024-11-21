package model;

public class User {

    private final String _login;
    private final String _password;
    private final String _email;

    public User(String login, String password, String email) {
        _login = login;
        _password = password;
        _email = email;
    }

    public String getLogin() {
        return _login;
    }

    public String getPass() {
        return _password;
    }

    public String getEmail() {
        return _email;
    }

}
