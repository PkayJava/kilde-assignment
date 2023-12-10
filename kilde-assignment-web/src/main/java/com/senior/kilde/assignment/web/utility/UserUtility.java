package com.senior.kilde.assignment.web.utility;

import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.kilde.assignment.dao.entity.User;
import com.senior.kilde.assignment.dao.repository.UserRepository;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserUtility {

    public static User authenticate(String login, String password) {
        ApplicationContext context = WicketFactory.getApplicationContext();
        UserRepository userRepository = context.getBean(UserRepository.class);

        Optional<User> optionalUser = userRepository.findByLogin(login);

        User user = optionalUser.orElse(null);

        if (user == null) {
            return null;
        }

        if (!user.isEnabled() || !user.isAccountNonExpired() || !user.isCredentialsNonExpired() || !user.isAccountNonLocked()) {
            return null;
        }

        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        try {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return null;
            }
        } catch (EncryptionOperationNotPossibleException e) {
            return null;
        }
        return user;
    }

}
