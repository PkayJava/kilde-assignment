package com.senior.kilde.assignment.web.validator;

import com.senior.kilde.assignment.web.factory.WicketFactory;
import com.senior.kilde.assignment.dao.entity.Group;
import com.senior.kilde.assignment.dao.entity.Investor;
import com.senior.kilde.assignment.dao.repository.GroupRepository;
import com.senior.kilde.assignment.dao.repository.InvestorRepository;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

public class InvestorNameValidator implements IValidator<String> {

    private String uuid;

    public InvestorNameValidator() {
    }

    public InvestorNameValidator(String groupId) {
        this.uuid = groupId;
    }

    @Override
    public void validate(IValidatable<String> validatable) {
        String name = validatable.getValue();
        if (name != null && !name.isEmpty()) {
            ApplicationContext context = WicketFactory.getApplicationContext();
            InvestorRepository repository = context.getBean(InvestorRepository.class);
            Optional<Investor> optionalEntity = repository.findByName(name);
            Investor entity = optionalEntity.orElse(null);
            if (entity != null) {
                if (this.uuid == null) {
                    validatable.error(new ValidationError(name + " is not available"));
                } else if (!entity.getId().equals(this.uuid)) {
                    validatable.error(new ValidationError(name + " is not available"));
                }
            }
        }
    }

}
