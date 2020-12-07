package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Privilege;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.UserRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IUserService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

//TODO AccessCheck
// Register User
// Login User
// Change Password


public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    //TODO
    @Override
    public Optional<User> login(String username, String password) throws LoginFailException, IOException {
        Optional<User> wantToLogin;

        wantToLogin = this.userRepository.getUserByUsername(username);
        if (wantToLogin.isPresent()) {
            if (wantToLogin.get().passwordIdentical(password)) {
                return wantToLogin;
            } else {
                throw new LoginFailException();
            }

        } else {
            throw new UserNotExisting();
        }
    }


    @Override
    public Optional<User> register(String username, String password, Privilege privilegeToGive) throws IOException, RegisterFailException {
        AtomicReference<Boolean> foundUser = new AtomicReference<>(false);
        this.userRepository.findAll().forEach(user -> {
            if (!foundUser.get()) {
                if (user.getUsername().equals(username)) {
                    foundUser.set(true);
                }
            }
        });
        if (foundUser.get()) {
            throw new RegisterFailException();
        }

        User newUser = new User(username, password);
        newUser.setPrivilege(privilegeToGive);
        //speichern in DB --> persistence
        newUser = userRepository.save(newUser);
        return Optional.of(newUser);

    }

    @Override
    public void registerDifferentUser(String name, String pwd, User currentUser, Privilege privilegeToAllow) throws PrivilegeToLowException, IOException, RegisterFailException {
        if (currentUser.getPrivilege().equals(Privilege.READ)) {
            throw new PrivilegeToLowException();
        } else {
            if (currentUser.getPrivilege().allowedToGivePrivilege(privilegeToAllow)) {
                this.register(name, pwd, privilegeToAllow);
            } else {
                throw new PrivilegeToLowException();
            }
        }
    }

    @Override
    public void setPrivilegeOfUser(Privilege privilegeToAllow, User user) {

    }

    @Override
    public void changePassword(String oldPw, String newPwd, User user) throws IOException, PasswordIncorrectException {
        if(user.passwordIdentical(oldPw)){
            user.setPwd(newPwd);
            this.userRepository.save(user);
        } else {
            throw new PasswordIncorrectException();
        }
    }

    protected UserRepository getUserRepository() {
        return this.userRepository;
    }


}
