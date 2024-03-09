package com.quizmaster.Auth;

import com.quizmaster.entities.User;
import com.quizmaster.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;


@Service
public class RegisteredUserDetailService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional=usersRepository.findByEmail(username);
        if(userOptional.isEmpty())
        {
            throw new UsernameNotFoundException("Email "+username+" does not exist");
        }

        User myUser=userOptional.get();

        return ApplicationUserDetails.builder()
                .username(username)
                .password(myUser.getPassword())
                .authorities(null)
                .build();
    }
}
