package net.drodado.vas.test1.service.validator;

import net.drodado.vas.test1.beans.Metrics;
import net.drodado.vas.test1.service.validator.AbstractJsonLineValidator.ValidationResume;

public interface JsonLineValidator {

	public ValidationResume validateFields(final String jsonLine, final Metrics metrics);
	
}
