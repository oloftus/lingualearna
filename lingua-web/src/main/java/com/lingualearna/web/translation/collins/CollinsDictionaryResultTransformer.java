package com.lingualearna.web.translation.collins;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.lingualearna.web.translation.TranslationResult;

@Component
public class CollinsDictionaryResultTransformer {

    private interface Action {

        void doo(Element elem);
    }

    private static final String CLASS_ID_PREFIX = "lingua-collins-";
    private static final String ANCHOR_WRAPPER = "<span class='reference'></span>";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String ID_ATTRIBUTE = "id";
    private static final String SPACE = " ";
    private static final String[] REMOVE_BLANK_TAGS = new String[] { "span", "br" };

    private static List<String> removeBlankTags = Arrays.asList(REMOVE_BLANK_TAGS);

    public TranslationResult transform(TranslationResult input) {

        Document html = Jsoup.parse(input.getTargetString());
        removePronunciationLink(html);
        removeElementsWithOnlySpacesSemicolonsAndTriangles(html);
        removeSpacesFromListNumbers(html);
        unwrapAnchors(html);
        unwrapSpansWithNoAttributes(html);
        removeComments(html);
        prefixAllClassesAndIds(html);

        input.setTargetString(html.toString());
        return input;
    }

    private void prefixAllClassesAndIds(Document html) {

        forEach(html.getElementsByAttribute(ID_ATTRIBUTE), new Action() {

            @Override
            public void doo(Element elem) {

                StringBuilder idBuilder = new StringBuilder();
                for (String id : elem.attr(ID_ATTRIBUTE).split(SPACE)) {
                    idBuilder.append(CLASS_ID_PREFIX + id);
                    idBuilder.append(SPACE);
                }
                elem.attr(ID_ATTRIBUTE, idBuilder.toString().trim());
            }
        });

        forEach(html.getElementsByAttribute(CLASS_ATTRIBUTE), new Action() {

            @Override
            public void doo(Element elem) {

                StringBuilder classBuilder = new StringBuilder();
                for (String clazz : elem.attr(CLASS_ATTRIBUTE).split(SPACE)) {
                    classBuilder.append(CLASS_ID_PREFIX + clazz);
                    classBuilder.append(SPACE);
                }
                elem.attr(CLASS_ATTRIBUTE, classBuilder.toString().trim());
            }
        });
    }

    private void forEach(Elements elements, Action doAction) {

        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element elem = iterator.next();
            doAction.doo(elem);
        }
    }

    private void unwrapSpansWithNoAttributes(Document html) {

        forEach(html.getElementsByTag("span"), new Action() {

            @Override
            public void doo(Element elem) {

                if (elem.attributes().size() == 0) {
                    elem.unwrap();
                }
            }
        });
    }

    private void unwrapAnchors(Document html) {

        Elements as = html.getElementsByTag("a");
        forEach(as, new Action() {

            @Override
            public void doo(Element elem) {

                elem.wrap(ANCHOR_WRAPPER);
            }
        });
        as.unwrap();
    }

    private void removeSpacesFromListNumbers(Document html) {

        forEach(html.getElementsByClass("bold"), new Action() {

            @Override
            public void doo(Element elem) {

                elem.text(elem.text().replaceAll("[\\s\u00a0]+", ""));
            }
        });
    }

    private void removeElementsWithOnlySpacesSemicolonsAndTriangles(Document html) {

        forEach(html.getElementsMatchingText(Pattern.compile("^[\\s\u00a0\u25B6;]*+$")), new Action() {

            @Override
            public void doo(Element elem) {

                if (removeBlankTags.contains(elem.tagName())) {
                    elem.remove();
                }
            }
        });
    }

    private void removePronunciationLink(Document html) {

        html.getElementsByClass("sound").remove();
        html.getElementsByTag("audio").remove();
    }

    private void removeComments(Node node) {

        int i = 0;
        while (i < node.childNodes().size()) {
            Node child = node.childNode(i);
            if (child.nodeName().equals("#comment"))
                child.remove();
            else {
                removeComments(child);
                i++;
            }
        }
    }
}
