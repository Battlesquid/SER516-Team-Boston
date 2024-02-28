package ui.metrics.pbchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import settings.Settings;
import taiga.model.query.epic.EpicDetail;
import taiga.model.query.project.Project;
import taiga.model.query.sprint.Sprint;
import taiga.model.query.sprint.UserStoryDetail;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PBChangeCalculatorTest {

    private PBChangeCalculator pbChangeCalculator;
    private Project mockProject;
    private Sprint mockSprint;

    @BeforeEach
    void setUp() {
        Settings.get().load(); // Ensure Settings are loaded before accessing AppModel
        
        pbChangeCalculator = new PBChangeCalculator();

        // Set up mock project and sprint objects
        mockProject = new Project();
        mockProject.setId(123); 
        mockSprint = new Sprint();
        mockSprint.setEstimatedStart(new Date(System.currentTimeMillis() - (48 * 60 * 60 * 1000))); // Sprint estimated start two days ago
    }

    @Test
    void testPBChangeItemsReturned() {
        List<PBChangeItem> pbChangeItems = pbChangeCalculator.calculate(mockProject, mockSprint);
        assertNotNull(pbChangeItems);
    }

    @Test
    void testFilterEpicAfterSprintStart() {
        // Create a list of sample epics
        EpicDetail epic1 = new EpicDetail();
        epic1.setCreatedDate(new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000))); // Epic created one day before
        EpicDetail epic2 = new EpicDetail();
        epic2.setCreatedDate(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000))); // Epic created one day after
        EpicDetail epic3 = new EpicDetail();
        epic3.setCreatedDate(new Date(System.currentTimeMillis() - (72 * 60 * 60 * 1000))); // Epic created one day after
        List<EpicDetail> epics = Arrays.asList(epic1, epic2, epic3);

        // Filter epics after sprint start
        List<EpicDetail> filteredEpics = PBChangeCalculator.filterEpicAfterSprintStart(epics, mockSprint);

        // Assert that only epics created after sprint start are filtered
        assertEquals(2, filteredEpics.size());
        assertEquals(epic1, filteredEpics.get(0));
        assertEquals(epic2, filteredEpics.get(1));
    }

    @Test
    void testNotRemovedFromPbAfterStart() {
        // Create a sample user story with a created date
        UserStoryDetail userStory = new UserStoryDetail();
        userStory.setId(1); 
        userStory.setCreatedDate(new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000))); // User story created one day before
        assertFalse(pbChangeCalculator.wasRemovedFromPbAfterStart(userStory, mockSprint));
    }
}