package com.lingualearna.web.translation;

public class TranslationResult {

    private String targetString;
    private Object providerSpecificResponse;

    public TranslationResult(String targetString) {

        this.targetString = targetString;
    }

    public Object getProviderSpecificResponse() {

        return providerSpecificResponse;
    }

    public String getTargetString() {

        return targetString;
    }

    public boolean hasProviderSpecificResponse() {

        return getProviderSpecificResponse() != null;
    }

    public void setProviderSpecificResponse(Object providerSpecificResponse) {

        this.providerSpecificResponse = providerSpecificResponse;
    }

    public void setTargetString(String targetString) {

        this.targetString = targetString;
    }
}
