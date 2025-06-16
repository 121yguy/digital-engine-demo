package org.demo.user.service.impl;

import lombok.AllArgsConstructor;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.demo.api.clients.PermissionClient;
import org.demo.common.domain.po.User;
import org.demo.common.utils.BcryptUtil;
import org.demo.common.utils.EncryptUtil;
import org.demo.user.config.SecretKeysProperties;
import org.demo.user.dao.UserDao;
import org.demo.user.service.AdminInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminInitServiceImpl implements AdminInitService {

    private UserDao userDao;
    private PermissionClient permissionClient;
    private SecretKeysProperties keys;

    @Override
    public boolean checkAdmin(String username) {
        return !Objects.isNull(userDao.getUserByUsername(username));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ShardingSphereTransactionType(TransactionType.BASE)
    public void initAdmin(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setPhone("11111111111");
        user.setGmtCreate(LocalDateTime.now(ZoneId.of("GMT")));
        user.setPassword(BcryptUtil.encode(password));
        EncryptUtil.encryptUserInfo(user, keys.getEmailSecretKey(), keys.getPhoneSecretKey());

        userDao.insertUser(user);

        permissionClient.bindSuperAdmin(user.getUserId());
    }
}
