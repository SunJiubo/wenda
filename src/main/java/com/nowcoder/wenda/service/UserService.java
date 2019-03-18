package com.nowcoder.wenda.service;

import com.nowcoder.wenda.dao.UserDAO;
import com.nowcoder.wenda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    public User getUser(int id){
        return userDAO.selectById(id);
    }
}
