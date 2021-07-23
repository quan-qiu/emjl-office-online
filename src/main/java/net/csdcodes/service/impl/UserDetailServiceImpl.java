package net.csdcodes.service.impl;

import net.csdcodes.model.MyUserDetail;
import net.csdcodes.model.User;
import net.csdcodes.repository.UserRepository;
import net.csdcodes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImpl implements UserDetailsService {

/*    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserService userService;*/

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        //User user = userService.getUserByUsername(username);
        User user = userRepository.getUserByUsername(username);
        //System.out.println("UserDetailServiceImpl.class :" + user);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new MyUserDetail(user);
    }
}
