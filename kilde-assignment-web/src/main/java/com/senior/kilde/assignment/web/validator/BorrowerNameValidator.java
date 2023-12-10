package com.senior.kilde.assignment.web.validator;

import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.kilde.assignment.dao.entity.Borrower;
import com.senior.kilde.assignment.dao.entity.Group;
import com.senior.kilde.assignment.dao.repository.BorrowerRepository;
import com.senior.kilde.assignment.dao.repository.GroupRepository;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

public class BorrowerNameValidator implements IValidator<String> {

    private String uuid;

    public BorrowerNameValidator() {
    }

    public BorrowerNameValidator(String groupId) {
        this.uuid = groupId;
    }

    @Override
    public void validate(IValidatable<String> validatable) {
        String name = validatable.getValue();
        if (name != null && !name.isEmpty()) {
            ApplicationContext context = WicketFactory.getApplicationContext();
            BorrowerRepository repository = context.getBean(BorrowerRepository.class);
            Optional<Borrower> optionalEntity = repository.findByName(name);
            Borrower entity = optionalEntity.orElse(null);
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
