package com.lingualearna.web.translation;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.translate.Translate.Translations.List;

/**
 * Wraps the Google Translate library which has final classes to permit mocking.
 * 
 * @author Oliver Loftus <o@oloft.us>
 */
@Component
public class GoogleTranslateLibraryWrapper {

	public GoogleTranslateBuilderWrapper getTranslateBuilder(HttpTransport httpTransport, JsonFactory jsonFactory,
			HttpRequestInitializer httpRequestInitializer) {

		return new GoogleTranslateBuilderWrapper(httpTransport, jsonFactory, httpRequestInitializer);
	}

	public String executeAndGetTranslation(List translateList) throws IOException {

		return translateList.execute().getTranslations().get(0).getTranslatedText();
	}
}
