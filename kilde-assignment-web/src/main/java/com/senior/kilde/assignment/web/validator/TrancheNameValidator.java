package com.senior.kilde.assignment.web.validator;

import com.senior.cyber.frmk.common.base.WicketFactory;
import com.senior.kilde.assignment.dao.entity.Borrower;
import com.senior.kilde.assignment.dao.entity.Tranche;
import com.senior.kilde.assignment.dao.repository.BorrowerRepository;
import com.senior.kilde.assignment.dao.repository.TrancheRepository;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

public class TrancheNameValidator implements IValidator<String> {

    private String uuid;

    public TrancheNameValidator() {
    }

    public TrancheNameValidator(String groupId) {
        this.uuid = groupId;
    }

    @Override
    public void validate(IValidatable<String> validatable) {
        String name = validatable.getValue();
        if (name != null && !name.isEmpty()) {
            ApplicationContext context = WicketFactory.getApplicationContext();
            TrancheRepository repository = context.getBean(TrancheRepository.class);
            Optional<Tranche> optionalEntity = repository.findByName(name);
            Tranche entity = optionalEntity.orElse(null);
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
