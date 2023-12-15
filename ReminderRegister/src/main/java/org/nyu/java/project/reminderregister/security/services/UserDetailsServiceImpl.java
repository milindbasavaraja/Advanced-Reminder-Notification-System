package org.nyu.java.project.reminderregister.security.services;


import org.nyu.java.project.reminderregister.dao.UserDao;
import org.nyu.java.project.reminderregister.entity.User;
import org.nyu.java.project.reminderregister.security.model.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDao userDao;

    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        try {
            user = userDao.findUserByUsername(username)
                    .orElseThrow(()->new UsernameNotFoundException("User not found with username: "+username));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return UserDetailsImpl.build(user);
    }
}
