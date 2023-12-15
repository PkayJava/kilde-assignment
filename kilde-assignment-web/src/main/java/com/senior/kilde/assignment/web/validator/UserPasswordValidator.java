package com.senior.kilde.assignment.web.validator;

import com.senior.kilde.assignment.dao.entity.User;
import com.senior.kilde.assignment.dao.repository.UserRepository;
import com.senior.kilde.assignment.web.factory.WicketFactory;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserPasswordValidator implements IValidator<String> {

    private final String userId;

    public UserPasswordValidator(String userId) {
        this.userId = userId;
    }

    @Override
    public void validate(IValidatable<String> validatable) {
        String password = validatable.getValue();
        ApplicationContext context = WicketFactory.getApplicationContext();
        UserRepository repository = context.getBean(UserRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        Optional<User> optionalEntity = repository.findById(userId);
        User entity = optionalEntity.orElseThrow();
        try {
            if (!passwordEncoder.matches(password, entity.getPassword())) {
                validatable.error(new ValidationError("invalid"));
            }
        } catch (Throwable e) {
            validatable.error(new ValidationError("invalid"));
        }
    }

}
