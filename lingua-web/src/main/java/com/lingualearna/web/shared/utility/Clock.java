package com.lingualearna.web.shared.utility;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
public class Clock {

    public DateTime now() {

        return new DateTime();
    }
}
