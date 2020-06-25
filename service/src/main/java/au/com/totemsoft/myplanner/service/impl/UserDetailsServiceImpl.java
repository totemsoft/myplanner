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
import au.com.totemsoft.myplanner.api.bean.UserPreferences;
import au.com.totemsoft.myplanner.dao.UserDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Inject private UserDao userDao;

    @Inject private UserPreferences userPreferences;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        IUser user = userDao.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        //
        userPreferences.clear();
        userPreferences.user(user);
        log.info("New user logged in: " + userPreferences.user());
        return User.withUsername(username)
            .password(user.getPassword())
            .roles("USER")
            .build();
    }

}
