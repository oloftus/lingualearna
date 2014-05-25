package com.lingualearna.web.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lingualearna.web.dao.NotebookDao;
import com.lingualearna.web.notes.Notebook;

@Service
@Transactional
public class NotebookService {

    @Autowired
    private NotebookDao notebookDao;

    public List<Notebook> getAllNotebooksByUser(int userId) {

        return notebookDao.getAllNotebooksByUser(userId);
    }
}
