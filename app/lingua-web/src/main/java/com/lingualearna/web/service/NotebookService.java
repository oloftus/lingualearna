package com.lingualearna.web.service;

import static com.lingualearna.web.security.SecuredConfigAttributes.ALLOW_OWNER;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.lingualearna.web.dao.NotebookDao;
import com.lingualearna.web.notes.Notebook;
import com.lingualearna.web.notes.Page;
import com.lingualearna.web.security.OwnedObjectType;

@Transactional
@Service
public class NotebookService {

    @Autowired
    private NotebookDao notebookDao;

    public List<Notebook> getAllNotebooksByUser(int userId) {

        return notebookDao.getAllNotebooksByUser(userId);
    }

    @OwnedObjectType(Page.class)
    @Secured({ ALLOW_OWNER })
    public Page getPageById(int pageId) {

        return notebookDao.getPageById(pageId);
    }
}
