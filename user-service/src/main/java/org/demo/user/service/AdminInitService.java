package org.demo.user.service;

public interface AdminInitService {

    boolean checkAdmin(String username);

    void initAdmin(String username, String password);

}
