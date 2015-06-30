package com.lingualearna.web.translation.logging;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.lingualearna.web.translation.TranslationProviderName;

@Entity
@Table(name = "translation_request_log")
public class LogEntry {

    private int entryId;
    private int userId;
    private String emailAddress;
    private DateTime time;
    private TranslationProviderName provider;

    public LogEntry(int userId, String emailAddress, DateTime time, TranslationProviderName provider) {

        this.userId = userId;
        this.emailAddress = emailAddress;
        this.time = time;
        this.provider = provider;
    }

    @Column(name = "email_address")
    public String getEmailAddress() {

        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {

        this.emailAddress = emailAddress;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    public int getEntryId() {

        return entryId;
    }

    public void setEntryId(int entryId) {

        this.entryId = entryId;
    }

    @Column(name = "user_id")
    public int getUserId() {

        return userId;
    }

    public void setUserId(int userId) {

        this.userId = userId;
    }

    @Column(name = "time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getTime() {

        return time;
    }

    public void setTime(DateTime time) {

        this.time = time;
    }

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    public TranslationProviderName getProvider() {

        return provider;
    }

    public void setProvider(TranslationProviderName provider) {

        this.provider = provider;
    }
}
