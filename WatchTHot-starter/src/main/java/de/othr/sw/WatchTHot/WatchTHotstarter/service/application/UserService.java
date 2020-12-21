package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Privilege;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.UserRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IUserService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

//TODO AccessCheck
// Register User
// Login User
// Change Password

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private User currentloggedInUser;

    public UserService(UserRepository repository){
        this.userRepository = repository;
    }

    /**
     * Loggs in User; Usually Called by Visualizer Service
     * @param username the Username; Usually by Visualizer Service; Usually from REST
     * @param password same with Password
     * @return The Optional<User>
     * @throws LoginFailException if the Password is incorrect or User doesn't exist
     * @throws IOException if password --> Pepper doesn't exist
     */
    @Override
    public User login(String username, String password) throws LoginFailException, IOException {
        Optional<User> wantToLogin;

        wantToLogin = this.userRepository.getUserByUsername(username);
        if (wantToLogin.isPresent()) {
            if (wantToLogin.get().passwordIdentical(password)) {
                this.currentloggedInUser = wantToLogin.get();
                return wantToLogin.get();
            } else {
                throw new LoginFailException();
            }

        } else {
            throw new UserNotExisting();
        }
    }

    /**
     * Register a User; Usually called when "Register different User" is successful and allows to register user
     * @param username username for the new user
     * @param password default password for the new user
     * @param privilegeToGive the privilege the new User gets
     * @return the new User
     * @throws IOException if pepper can't be found
     * @throws RegisterFailException if the user by username already exists!
     */
    @Override
    @Transactional
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


    /**
     * Register different User, when you are logged in and have the privileges (only possible for useres of tier 1-2)
     * @param name username of new User
     * @param pwd password of the new User
     * @param currentUser the current loggedin User
     * @param privilegeToAllow Privilege you want to give the user
     * @throws PrivilegeToLowException if the Privilege you want to give is greater than the priv. of current logged in User
     * @throws IOException if the pepper doesn't exist
     * @throws RegisterFailException if the username already exists.
     */
    @Override
    @Transactional
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

    /**
     * Changes the Password of the current user
     * @param oldPw old password
     * @param newPwd new Password (will be checked earlier in the visualization if old password was typed in 2 correctly)
     * @param user the current user where the pwd needs to be changed.
     * @throws IOException Pepper not there
     * @throws PasswordIncorrectException If the password (old password) was incorrect
     */
    @Override
    @Transactional
    public void changePassword(String oldPw, String newPwd, User user) throws IOException, PasswordIncorrectException {
        if(user.passwordIdentical(oldPw)){
            user.setPwd(newPwd);
            this.userRepository.save(user);
        } else {
            throw new PasswordIncorrectException();
        }
    }

    @Override
    @Transactional
    public void saveUserChanges(User user) {
        this.userRepository.save(user);
    }

    /**
     * I dunno if this is needed so i leave it here for future stuff...but probably not relevant.
     * @return the repository
     */
    protected UserRepository getUserRepository() {
        return this.userRepository;
    }


}
