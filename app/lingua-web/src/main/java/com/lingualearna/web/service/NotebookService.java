package com.lingualearna.web.service;

import static com.lingualearna.web.security.SecuredConfigAttributes.ALLOW_OWNER;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Validator;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.lingualearna.web.controller.exceptions.ValidationException;
import com.lingualearna.web.dao.NotebookDao;
import com.lingualearna.web.languages.LanguageNamesValidator;
import com.lingualearna.web.notes.Notebook;
import com.lingualearna.web.notes.Page;
import com.lingualearna.web.security.OwnedObjectType;
import com.lingualearna.web.security.User;
import com.lingualearna.web.util.locale.LocalizationService;

@Transactional
@Service
public class NotebookService extends AbstractService {

    @Autowired
    private NotebookDao dao;

    @Autowired
    private LocalizationService localizationService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private LanguageNamesValidator langNamesValidator;

    @Autowired
    private Validator validator;

    public void createNotebook(Notebook notebook) throws ValidationException {

        validateEntity(notebook);
        validateLanguageNames(notebook);
        dao.persist(notebook);
    }

    public void createPage(Page page) throws ValidationException {

        validateEntity(page);
        setPagePosition(page);
        dao.persist(page);
    }

    public List<Notebook> getAllNotebooksByUser(int userId) {

        return dao.getAllNotebooksByUser(userId);
    }

    @OwnedObjectType(Notebook.class)
    @Secured({ ALLOW_OWNER })
    public Notebook getNotebookById(int notebookId) {

        return dao.find(Notebook.class, notebookId);
    }

    @OwnedObjectType(Page.class)
    @Secured({ ALLOW_OWNER })
    public Page getPageById(int pageId) {

        return dao.getPageById(pageId);
    }

    @Override
    protected Validator getValidator() {

        return validator;
    }

    private void setPagePosition(Page page) {

        User user = page.getNotebook().getOwner();
        Integer currentMaxPosition = dao.doUntypedQueryWithParams(Page.MAX_POSITION_QUERY,
                Pair.of(Page.MAX_POSITION_QUERY, user));
        if (currentMaxPosition == null) {
            currentMaxPosition = 0;
        }
        page.setPosition(currentMaxPosition + 1);
    }

    @Secured(ALLOW_OWNER)
    public Page updatePage(Page page) {

        validateEntity(page);
        Page mergedPage = dao.merge(page);
        return mergedPage;
    }

    @Secured(ALLOW_OWNER)
    public Page updatePageWithPosition(Page page, int oldPosition) {

        Integer newPosition = page.getPosition();
        if (oldPosition < newPosition) {
            dao.decrementPagePositionsInInterval(page.getNotebook(), oldPosition, newPosition);
        }
        else {
            dao.incrementPagePositionsInInterval(page.getNotebook(), oldPosition, newPosition);
        }

        return updatePage(page);
    }

    private void validateLanguageNames(Notebook notebook) throws ValidationException {

        String foreignLang = notebook.getForeignLang().getLanguage();
        String localLang = notebook.getLocalLang().getLanguage();
        langNamesValidator.validateLanguageNames(foreignLang, localLang);
    }
}
