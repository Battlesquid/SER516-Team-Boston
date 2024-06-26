package ui.metrics.scopechange;

import taiga.TaigaClient;
import taiga.models.sprint.Sprint;
import taiga.models.sprint.UserStory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ScopeChangeCalculator {

    public ScopeChangeCalculator() {
    }

    /**
     * Makes api calls to the HistoryAPI to find out if a sprint's user story was added before or after the start date
     *
     * @param userStory The userstory being analyzed
     * @param sprint    The sprint the userstory belongs too
     * @return The date the userstory was added if added after the sprint start, else returns null.
     */
    protected Date getAddedAfterStartDate(UserStory userStory, Sprint sprint) {
        AtomicReference<Date> addedDate = new AtomicReference<>();
        TaigaClient.getHistoryAPI().getUserStoryHistory(userStory.getId(), result -> {
            if (result.getStatus() != 200) {
                return;
            }
            Arrays.stream(result.getContent())
                    .forEach(historyItem -> {
                        List<Long> milestoneDiff = historyItem.getDiff().getMilestone();
                        if (milestoneDiff == null) {
                            return;
                        }
                        boolean afterStart = historyItem.getCreatedAt().after(sprint.getEstimatedStart());
                        if (afterStart && milestoneDiff.get(0) == null && milestoneDiff.get(1) != null
                                && addedDate.get() == null) {
                            addedDate.set(historyItem.getCreatedAt());
                        }
                    });
        }).join();
        return addedDate.get();
    }

    /**
     * Analyzes the sprints User Stories and returns a list of those that were added after the start of the sprint
     *
     * @param sprint The Sprint to analyze for scope change
     * @return A list of ScopeChangeItems which contain the added UserStories
     */
    public List<ScopeChangeItem> calculate(Sprint sprint) {
        List<UserStory> stories = sprint.getUserStories();

        List<ScopeChangeItem> addedAfterStart = stories
                .parallelStream()
                .map(s -> {
                    Date addedDate = getAddedAfterStartDate(s, sprint);
                    if (addedDate == null) {
                        return null;
                    }
                    return new ScopeChangeItem(addedDate, s, false);
                })
                .filter(Objects::nonNull)
                .toList();

        return addedAfterStart;
    }

}