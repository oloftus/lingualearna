package com.lingualearna.web.controller.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingualearna.web.util.locale.LocalizationService;

@Component
public class GenericMapper<S, T> {

	@Autowired
	private LocalizationService localizationService;

	public void fromModel(S model, T entity, String... ignore) {

		copyProperties(model, entity, ignore);
	}

	public void fromEntity(T entity, S model, String... ignore) {

		copyProperties(entity, model, ignore);
	}

	private void copyProperties(Object source, Object target, String... ignore) {

		BeanUtils.copyProperties(source, target, ignore);
	}
}
