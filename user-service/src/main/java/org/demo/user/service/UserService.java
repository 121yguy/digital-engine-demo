package org.demo.user.service;

import org.demo.common.domain.po.User;

import java.util.List;

public interface UserService {

    boolean register(User user);
    String login(User user);
    List<User> getUsers();
    User getUser(long id);
    boolean updateUser(long id, User user);
    boolean resetPassword(long id);
}
