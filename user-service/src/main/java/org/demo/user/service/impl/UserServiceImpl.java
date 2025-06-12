package org.demo.user.service.impl;

import lombok.AllArgsConstructor;
import org.demo.common.domain.po.User;
import org.demo.user.dao.UserDao;
import org.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    @Override
    public boolean register(User user) {
        return false;
    }

    @Override
    public String login(User user) {
        return "";
    }

    @Override
    public List<User> getUsers() {
        return Collections.emptyList();
    }

    @Override
    public User getUser(long id) {
        return null;
    }

    @Override
    public boolean updateUser(long id, User user) {
        return false;
    }

    @Override
    public boolean resetPassword(long id) {
        return false;
    }
}
