package cn.wx.ycloudtech.service;

import cn.wx.ycloudtech.domain.User;

public interface UserService {
    User getUserById(String openId);

    int insertUser(User user);

    int updateUser(User user);
}
