package com.lingualearna.web.notebooks;

import static com.lingualearna.web.security.ownership.SecuredConfigAttributes.ALLOW_OWNER;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.lingualearna.web.language.LanguageService;
import com.lingualearna.web.localisation.LocalisationService;
import com.lingualearna.web.security.ownership.OwnedObjectType;
import com.lingualearna.web.security.users.UserService;
import com.lingualearna.web.shared.components.AbstractService;
import com.lingualearna.web.shared.exceptions.ResourceNotFoundException;
import com.lingualearna.web.shared.validation.ValidationException;
import com.lingualearna.web.shared.validation.validators.LanguageNamesValidator;

@Transactional
@Service
public class NotebookService extends AbstractService {

    public static final String NOTEBOOK_NOT_FOUND = "Notebook not found";

    @Autowired
    private NotebookDao notebookDao;

    @Autowired
    private LocalisationService localizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private LanguageNamesValidator langNamesValidator;

    @Autowired
    private Validator validator;

    @Override
    protected Validator getValidator() {

        return validator;
    }

    public void createNotebook(Notebook notebook, String ownerUsername) {

        notebook.setOwner(userService.getUserByUsername(ownerUsername));
        validateEntity(notebook);
        validateLanguageNames(notebook);
        notebookDao.persist(notebook);
    }

    public List<Notebook> getAllNotebooksByUser(String username) {

        return notebookDao.getAllNotebooksByUser(username);
    }

    @OwnedObjectType(Notebook.class)
    @Secured(ALLOW_OWNER)
    public Notebook retrieveNotebook(int notebookId) {

        Notebook notebook = notebookDao.find(Notebook.class, notebookId);

        if (notebook == null) {
            throw new ResourceNotFoundException(NOTEBOOK_NOT_FOUND);
        }

        return notebook;
    }

    private void validateLanguageNames(Notebook notebook) {

        String foreignLang = notebook.getForeignLang().getLanguage();
        String localLang = notebook.getLocalLang().getLanguage();
        langNamesValidator.validateLanguageNames(foreignLang, localLang);
    }
}
