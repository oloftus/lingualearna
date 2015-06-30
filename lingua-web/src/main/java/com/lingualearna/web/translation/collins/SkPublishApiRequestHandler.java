package com.lingualearna.web.translation.collins;

import org.apache.http.client.methods.HttpGet;

import fr.idm.sk.publish.api.client.light.SkPublishAPI;

final class SkPublishApiRequestHandler implements SkPublishAPI.RequestHandler {

    @Override
    public void prepareGetRequest(HttpGet request) {

        request.setHeader("Accept", "application/json");
    }
}