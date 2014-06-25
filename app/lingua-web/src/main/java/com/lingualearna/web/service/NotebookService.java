package com.lingualearna.web.service;

import static com.lingualearna.web.security.SecuredConfigAttributes.ALLOW_OWNER;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.lingualearna.web.controller.exceptions.ValidationException;
import com.lingualearna.web.dao.NotebookDao;
import com.lingualearna.web.notes.Notebook;
import com.lingualearna.web.notes.Page;
import com.lingualearna.web.security.OwnedObjectType;
import com.lingualearna.web.util.locale.LocalizationService;

@Transactional
@Service
public class NotebookService extends AbstractService {

    private static final String NOTEBOOK_NAME_FIELD = "name";

    @Autowired
    private NotebookDao dao;

    @Autowired
    private LocalizationService localizationService;

    @Autowired
    private Validator validator;

    private void assertHasUniqueName(Notebook notebook) throws ValidationException {

        if (dao.getCountOfNotebooksWithName(notebook.getName()) != 0) {
            ValidationException exception = new ValidationException();
            exception.addFieldError(NOTEBOOK_NAME_FIELD,
                    localizationService.lookupLocalizedString("notebook.duplicateNotebook"));
            throw exception;
        }
    }

    public void createNotebook(Notebook notebook) throws ValidationException {

        validateEntity(notebook);
        assertHasUniqueName(notebook);
        dao.persist(notebook);
    }

    public List<Notebook> getAllNotebooksByUser(int userId) {

        return dao.getAllNotebooksByUser(userId);
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
}
