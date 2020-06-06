package au.com.totemsoft.myplanner.service.impl;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.com.totemsoft.myplanner.api.bean.IUser;
import au.com.totemsoft.myplanner.dao.UserDao;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Inject private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        IUser user = userDao.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return User.withUsername(username)
            .password(user.getPassword())
            .roles("USER")
            .build();
    }

}
