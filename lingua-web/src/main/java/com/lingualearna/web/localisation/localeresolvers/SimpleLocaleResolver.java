package com.lingualearna.web.localisation.localeresolvers;

import java.util.Locale;

import org.springframework.stereotype.Component;

/**
 * Replace with proper locale resolution later
 * http://www.mkyong.com/spring-mvc/spring-mvc-internationalization-example/
 */
@Component
public class SimpleLocaleResolver implements UserLocaleResolver {

    @Override
    public Locale getUserLocale() {

        return Locale.getDefault();
    }
}
