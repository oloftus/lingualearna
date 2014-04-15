package com.lingualearna.web.util.locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LocalizationService.class })
public class LocalizationServiceTest {

    private static final String EXPECTED_LOCALIZED_VALUE = "expectedValue";
    private static final String LOOKUP_KEY = "lookupKey";
    private static final String LOCALE_RESOLVER_FIELD_NAME = "localeResolver";
    private static final String BUNDLE_NAME = "StringsBundle";

    @Mock
    private Locale expectedLocale;

    @Mock
    private UserLocaleResolver localeResolver;

    @Mock
    private ResourceBundle resourceBundle;

    private Locale actualLocale;
    private String actualLocalizedValue;

    private LocalizationService localizationService = new LocalizationService();

    private void givenTheLocaleResolverIsSetup() {

        when(localeResolver.getUserLocale()).thenReturn(expectedLocale);
    }

    private void givenTheResourceBundleIsSetup() {

        givenTheLocaleResolverIsSetup();
        mockStatic(ResourceBundle.class);
        when(ResourceBundle.getBundle(BUNDLE_NAME, expectedLocale)).thenReturn(resourceBundle);
        when(resourceBundle.getString(LOOKUP_KEY)).thenReturn(EXPECTED_LOCALIZED_VALUE);
    }

    @Before
    public void setup() throws Exception {

        ReflectionTestUtils.setField(localizationService, LOCALE_RESOLVER_FIELD_NAME, localeResolver);
    }

    @Test
    public void testGetUserLocaleFunctions() {

        givenTheLocaleResolverIsSetup();
        whenICallGetUserLocale();
        thenTheValueFromTheLocaleResolverIsReturned();
    }

    @Test
    public void testLookupLocalizedStringFunctions() {

        givenTheResourceBundleIsSetup();
        whenICallLookupLocalizedString();
        thenTheLocalizedValueIsReturned();
    }

    private void thenTheLocalizedValueIsReturned() {

        assertEquals(EXPECTED_LOCALIZED_VALUE, actualLocalizedValue);
    }

    private void thenTheValueFromTheLocaleResolverIsReturned() {

        assertEquals(expectedLocale, actualLocale);
    }

    private void whenICallGetUserLocale() {

        actualLocale = localizationService.getUserLocale();
    }

    private void whenICallLookupLocalizedString() {

        actualLocalizedValue = localizationService.lookupLocalizedString(LOOKUP_KEY);
    }
}
