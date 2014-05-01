package com.lingualearna.web.translation.provider.google;

import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.translate.Translate;

public class GoogleTranslateBuilderWrapper {

	private Translate.Builder builder;

	public GoogleTranslateBuilderWrapper(HttpTransport httpTransport, JsonFactory jsonFactory,
			HttpRequestInitializer httpRequestInitializer) {

		builder = new Translate.Builder(httpTransport, jsonFactory, httpRequestInitializer);
	}

	public GoogleTranslateBuilderWrapper setGoogleClientRequestInitializer(
			GoogleClientRequestInitializer googleClientRequestInitializer) {

		builder.setGoogleClientRequestInitializer(googleClientRequestInitializer);
		return this;
	}

	public GoogleTranslateBuilderWrapper setApplicationName(String applicationName) {

		builder.setApplicationName(applicationName);
		return this;
	}

	public GoogleTranslateBuilderWrapper setRootUrl(String apiUrlRootUrl) {

		builder.setRootUrl(apiUrlRootUrl);
		return this;
	}

	public GoogleTranslateBuilderWrapper setServicePath(String servicePath) {

		builder.setServicePath(servicePath);
		return this;
	}

	public Translate build() {

		return builder.build();
	}
}