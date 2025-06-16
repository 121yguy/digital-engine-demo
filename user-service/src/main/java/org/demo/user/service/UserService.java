package org.demo.user.service;

import org.demo.common.domain.dto.LoginDTO;
import org.demo.common.domain.dto.RegisterDTO;
import org.demo.common.domain.dto.ResetPasswordDTO;
import org.demo.common.domain.dto.UserInfoDTO;
import org.demo.common.domain.vo.UserVO;

import java.util.List;

public interface UserService {

    boolean register(RegisterDTO registerDTO, String ip);

    String login(LoginDTO loginDTO);

    /**
     *
     * @param startUserId 表示从这个id向后查询数据，初次查询可从0开始，以后的每次查询可用上次查询的最后一个用户的id作为{@code startUserId}进行查询
     *
     *
     **/
    List<UserVO> getUsers(Long startUserId, Long roleId, Long userId);

    /**
     *
     * @param userId 被查询的用户的userId
     * @param uid 查询者的userId
     *
     **/
    UserVO getUserInfo(Long userId, Long uid, Long roleId);

    /**
     *
     * @param userId 被修改的用户的userId
     * @param uid 修改者的userId
     *
     **/
    boolean updateUser(Long userId, Long uid, Long roleId, UserInfoDTO userInfoDTO);

    /**
     *
     * @param resetPasswordDTO 对修改密码所需数据的封装，如果{@code resetPasswordDTO.getUserId()}为空，表示用户修改自己的密码
     * @param userId 修改者的用户的userId
     *
     **/
    boolean resetPassword(ResetPasswordDTO resetPasswordDTO, Long userId, Long roleId);
}
