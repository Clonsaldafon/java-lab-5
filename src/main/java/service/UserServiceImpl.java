package service;

import model.User;
import repository.UserRepository;
import repository.UserRepositoryImpl;

public class UserServiceImpl implements UserService {

    private final UserRepository _userRepository;

    public UserServiceImpl() {
        _userRepository = new UserRepositoryImpl();
    }

    @Override
    public void add(User user) {
        _userRepository.add(user);
    }

    @Override
    public User get(String login) {
        return _userRepository.get(login);
    }

}
