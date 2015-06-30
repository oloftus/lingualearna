package com.lingualearna.web.knowledgetest;

import static com.lingualearna.web.security.ownership.SecuredConfigAttributes.ALLOW_OWNER;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.lingualearna.web.notebooks.Notebook;
import com.lingualearna.web.notes.Note;
import com.lingualearna.web.notes.NoteDao;
import com.lingualearna.web.security.ownership.OwnedObjectType;

@Service
@Transactional
public class KnowledgeTestService {

    private static final int NUMBER_OF_TEST_ENTRIES = 10;

    @Autowired
    private NoteDao dao;

    @OwnedObjectType(Notebook.class)
    @Secured(ALLOW_OWNER)
    public List<? extends TestEntry> getRandomNotesIncludedInTest(int notebookId) {

        return dao.getRandomNotesFromNotebookForTest(notebookId, NUMBER_OF_TEST_ENTRIES);
    }

    @OwnedObjectType(Note.class)
    @Secured(ALLOW_OWNER)
    public String getSourceContext(int noteId) {

        return dao.getSourceContext(noteId);
    }
}
