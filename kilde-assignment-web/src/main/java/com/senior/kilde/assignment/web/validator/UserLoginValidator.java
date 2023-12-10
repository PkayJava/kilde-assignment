package com.senior.kilde.assignment.web.validator;

import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.kilde.assignment.dao.entity.User;
import com.senior.kilde.assignment.dao.repository.UserRepository;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

public class UserLoginValidator implements IValidator<String> {

    @Override
    public void validate(IValidatable<String> validatable) {
        String login = validatable.getValue();
        if (login != null && !"".equals(login)) {
            ApplicationContext context = WicketFactory.getApplicationContext();
            UserRepository repository = context.getBean(UserRepository.class);
            Optional<User> optionalEntity = repository.findByLogin(login);
            optionalEntity.ifPresent(entity -> validatable.error(new ValidationError(login + " is not available")));
        }
    }

}
