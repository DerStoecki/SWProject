package de.othr.sw.WatchTHot.WatchTHotstarter.service.api;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Privilege;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.LoginFailException;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.PasswordIncorrectException;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.PrivilegeToLowException;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.RegisterFailException;

import java.io.IOException;
import java.util.Optional;

public interface IUserService {
    Optional<User> login(String username, String password) throws LoginFailException, IOException;
    Optional<User> register(String name, String pwd, Privilege privilegeToGive) throws IOException, RegisterFailException;

    void registerDifferentUser(String name, String pwd, User currentUser, Privilege privilegeToAllow) throws PrivilegeToLowException, IOException, RegisterFailException;

    void setPrivilegeOfUser(Privilege privilegeToAllow, User user);

    void changePassword(String oldPw, String newPwd, User user) throws IOException, PasswordIncorrectException;
}
