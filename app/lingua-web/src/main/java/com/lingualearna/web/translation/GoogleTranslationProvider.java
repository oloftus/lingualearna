package com.lingualearna.web.translation;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.Translate.Translations.List;
import com.google.api.services.translate.TranslateRequestInitializer;
import com.google.common.collect.Lists;

@Service
@Qualifier("GoogleTranslate")
public class GoogleTranslationProvider implements TranslationProvider {

	private static final Logger LOG = LoggerFactory.getLogger(GoogleTranslationProvider.class);
	private static final String API_URL_REGEX = "(^https?://[^/]*)(/.*$)";

	@Value("${translation.service.google.appName}")
	private String applicationName;

	@Value("${translation.service.google.apiKey}")
	private String apiKey;

	@Value("${translation.service.google.apiUrl}")
	private String apiUrl;

	@Autowired
	private GoogleTranslateLibraryWrapper googleTranslateLibraryWrapper;

	private String apiUrlRootUrl;
	private String apiUrlServicePath;

	@PostConstruct
	public void init() {

		splitApiUrlComponents();
	}

	@Override
	public TranslationResult translate(Locale sourceLang, Locale targetLang, String query) throws TranslationException {

		String result;
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		try {
			HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			Translate client = googleTranslateLibraryWrapper.getTranslateBuilder(httpTransport, jsonFactory, null)
					.setGoogleClientRequestInitializer(new TranslateRequestInitializer(apiKey)).setApplicationName(
							applicationName).setRootUrl(apiUrlRootUrl).setServicePath(apiUrlServicePath).build();
			List translateList = client.translations().list(Lists.newArrayList(query), targetLang.getLanguage());
			translateList.setSource(sourceLang.getLanguage());
			result = googleTranslateLibraryWrapper.executeAndGetTranslation(translateList);
		} catch (IOException | GeneralSecurityException e) {
			LOG.error("Exception getting HTTPTransport object in GoogleTranslationProvider", e);
			throw new TranslationException("A problem occurred getting the translation, please try again later", e);
		}

		return new TranslationResult(result);
	}

	private void splitApiUrlComponents() {

		Pattern pattern = Pattern.compile(API_URL_REGEX);
		Matcher matcher = pattern.matcher(apiUrl);
		matcher.matches();
		apiUrlRootUrl = matcher.group(1);
		apiUrlServicePath = matcher.group(2);
	}
}
