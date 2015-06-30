package com.lingualearna.web.knowledgetest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingualearna.web.shared.objectmappers.ObjectMapper;

@Controller
@RequestMapping("/api/knowledge")
public class KnowledgeTestController {

    @Autowired
    private KnowledgeTestService knowledgeTestService;

    @Autowired
    private ObjectMapper<TestEntryModel, TestEntry> testEntryMapper;

    @RequestMapping(value = "/notebook/{notebookId}", produces = "application/json")
    @ResponseBody
    public List<TestEntryModel> getRandomSelectionOfTestEntries(@PathVariable int notebookId) throws Exception {

        List<? extends TestEntry> testEntries = knowledgeTestService.getRandomNotesIncludedInTest(notebookId);

        List<TestEntryModel> testEntryModels = new ArrayList<>();
        for (TestEntry testEntry : testEntries) {
            TestEntryModel testEntryModel = new TestEntryModel();
            testEntryMapper.copyPropertiesRtl(testEntry, testEntryModel);
            testEntryModels.add(testEntryModel);
        }

        return testEntryModels;
    }

    @RequestMapping(value = "/sourceContext/{noteId}", produces = "application/text; charset=utf-8")
    @ResponseBody
    public String getSourceContext(@PathVariable int noteId) throws Exception {

        String sourceContext = knowledgeTestService.getSourceContext(noteId);
        return sourceContext;
    }
}
