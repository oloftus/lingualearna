package com.lingualearna.web.notes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.ToTextContentHandler;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.lingualearna.web.shared.exceptions.UnexpectedProblemException;

@Component
public class NoteModelFlattener {

    private static final int TRIM_LENGTH = 300;
    private static final String SPACE = " ";
    private static final String BLANK = "";
    private static final Charset HTML_CHARSET = Charset.forName("UTF-8");

    private static final String[] UNWANTED_STRINGS = new String[] {
            "Your browser does not support HTML5 audio."
    };

    private Parser collinsTranslateParser;

    @PostConstruct
    public void init() {

        collinsTranslateParser = new HtmlParser();
    }

    private String stripWhitespace(String input) {

        return input
                .replaceAll("\\n+", SPACE)
                .replaceAll("\\s+", SPACE);
    }

    private String trimLength(String input) {

        int trimLength = Math.min(TRIM_LENGTH, input.length());

        return input.substring(0, trimLength).trim();
    }

    private String removeUnwantedStrings(String input) {

        String output = input;

        for (String unwantedString : UNWANTED_STRINGS) {
            return output = output.replace(unwantedString, BLANK);
        }

        return output;
    }

    private boolean nullOrEmpty(String input) {

        return input == null || input.trim().isEmpty();
    }

    private String stripHtml(String htmlInput) throws SAXException, TikaException {

        if (nullOrEmpty(htmlInput)) {
            return BLANK;
        }

        StringWriter writer = new StringWriter();
        DefaultHandler contentHandler = new ToTextContentHandler(writer);
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();

        byte[] additionalNoteBytes = htmlInput.getBytes(HTML_CHARSET);
        InputStream inputStream = new ByteArrayInputStream(additionalNoteBytes);

        try {
            collinsTranslateParser.parse(inputStream, contentHandler, metadata, parseContext);
        }
        catch (IOException ioException) {
            throw new UnexpectedProblemException("Exception parsing HTML", ioException);
        }

        return writer.toString();
    }

    private String processField(String localNote) {

        if (nullOrEmpty(localNote)) {
            return BLANK;
        }

        return trimLength(stripWhitespace(localNote));
    }

    private String processAdditionalNotes(String additionalNotes) throws SAXException, TikaException {

        return removeUnwantedStrings(stripHtml(additionalNotes));
    }

    public void flattenNoteModel(NoteModel note) {

        String flatLocalNote = processField(note.getLocalNote());
        String flatForeignNote = processField(note.getForeignNote());
        String flatAdditionalNotes;

        try {
            flatAdditionalNotes = processField(processAdditionalNotes(note.getAdditionalNotes()));
        }
        catch (SAXException | TikaException e) {
            flatAdditionalNotes = BLANK;
        }

        note.setFlatLocalNote(flatLocalNote);
        note.setFlatForeignNote(flatForeignNote);
        note.setFlatAdditionalNotes(flatAdditionalNotes);
    }
}
