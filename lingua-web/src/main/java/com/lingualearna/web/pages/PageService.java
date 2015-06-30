package com.lingualearna.web.pages;

import static com.lingualearna.web.security.ownership.SecuredConfigAttributes.ALLOW_OWNER;

import javax.transaction.Transactional;
import javax.validation.Validator;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.lingualearna.web.notebooks.Notebook;
import com.lingualearna.web.notebooks.NotebookService;
import com.lingualearna.web.security.ownership.OwnedObjectType;
import com.lingualearna.web.shared.components.AbstractService;
import com.lingualearna.web.shared.exceptions.ResourceNotFoundException;
import com.lingualearna.web.shared.objectmappers.ObjectMapper;
import com.lingualearna.web.shared.validation.groups.Repositioning;

@Transactional
@Service
public class PageService extends AbstractService {

    public static final String PAGE_NOT_FOUND = "Page not found";

    @Autowired
    private PageDao pageDao;

    @Autowired
    private NotebookService notebookService;

    @Autowired
    private ObjectMapper<Page, Page> pageMapper;

    @Autowired
    private Validator validator;

    @Override
    protected Validator getValidator() {

        return validator;
    }

    public void createPage(Page page, int notebookId) {

        setNotebook(page, notebookId);
        setPagePositionToBottom(page);
        validateEntity(page);
        pageDao.persist(page);
    }

    @OwnedObjectType(Page.class)
    @Secured(ALLOW_OWNER)
    public void deletePage(int pageId) {

        Page page = pageDao.find(Page.class, pageId);

        if (page == null) {
            throw new ResourceNotFoundException(PageService.PAGE_NOT_FOUND);
        }

        pageDao.delete(Page.class, pageId);
        pageDao.doDecrementPagesAfterDeleted(page.getNotebook(), page.getPosition());
    }

    @OwnedObjectType(Page.class)
    @Secured(ALLOW_OWNER)
    public Page retrievePage(int pageId) {

        Page page = pageDao.getPageById(pageId);

        if (page == null) {
            throw new ResourceNotFoundException(PAGE_NOT_FOUND);
        }

        return page;
    }

    @Secured(ALLOW_OWNER)
    public Page updatePage(Page pageShell) {

        Page pageEntity = retrievePageByShell(pageShell);
        int oldPosition = pageEntity.getPosition();
        pageMapper.copyPropertiesLtr(pageShell, pageEntity);
        setNotebook(pageEntity, pageShell.getNotebookId());

        validateEntity(pageEntity);
        reorderPagesIfPositionChanged(pageEntity, oldPosition);

        return pageDao.merge(pageEntity);
    }

    private Page retrievePageByShell(Page pageShell) {

        return retrievePage(pageShell.getPageId());
    }

    private void reorderPagesIfPositionChanged(Page pageEntity, int oldPosition) {

        validateEntity(pageEntity, Repositioning.class);

        int newPosition = pageEntity.getPosition();
        if (oldPosition != newPosition) {

            if (oldPosition < newPosition) {
                pageDao.decrementPagePositionsInInterval(pageEntity.getNotebook(), oldPosition, newPosition);
            }
            else {
                pageDao.incrementPagePositionsInInterval(pageEntity.getNotebook(), oldPosition, newPosition);
            }
        }
    }

    private void setNotebook(Page page, int notebookId) {

        Notebook notebook = notebookService.retrieveNotebook(notebookId);
        page.setNotebook(notebook);
    }

    private void setPagePositionToBottom(Page page) {

        Integer currentMaxPosition = pageDao.doUntypedQueryWithParams(Page.MAX_POSITION_QUERY,
                Pair.of(Page.NOTEBOOK_PARAM, page.getNotebook()));

        if (currentMaxPosition == null) {
            currentMaxPosition = 0;
        }

        page.setPosition(currentMaxPosition + 1);
    }
}
