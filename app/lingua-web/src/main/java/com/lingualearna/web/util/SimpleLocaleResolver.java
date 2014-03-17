package com.lingualearna.web.util;

import java.util.Locale;

import org.springframework.stereotype.Component;

/**
 * Replace with proper locale resolution later
 * http://www.mkyong.com/spring-mvc/spring-mvc-internationalization-example/
 * 
 * @author Oliver Loftus <o@oloft.us>
 */
@Component
public class SimpleLocaleResolver {

	public Locale getUserLocale() {

		return Locale.getDefault();
	}
}
