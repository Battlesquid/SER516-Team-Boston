package ui.services;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import taiga.models.sprint.Sprint;
import ui.metrics.pbchange.PBChangeCalculator;
import ui.metrics.pbchange.PBChangeItem;

import java.util.List;


public class PBChangeService extends Service<Object> {
    private Sprint sprint;
    private final PBChangeCalculator pbChangeCalculator;

    private final ObservableList<PBChangeItem> addedUserStories;
    private final ObservableList<PBChangeItem> removedUserStories;

    public PBChangeService() {
        this.pbChangeCalculator = new PBChangeCalculator();
        this.addedUserStories = FXCollections.observableArrayList();
        this.removedUserStories = FXCollections.observableArrayList();
    }

    public ObservableList<PBChangeItem> getAddedUserStories() {
        return addedUserStories;
    }

    public ObservableList<PBChangeItem> getRemovedUserStories() {
        return removedUserStories;
    }

    public void recalculate(Sprint sprint) {
        this.sprint = sprint;
        this.restart();
    }

    @Override
    protected void failed() {
        super.failed();
        Throwable exception = getException();
        if (exception != null) {
            exception.printStackTrace();
        }
    }

    @Override
    protected Task<Object> createTask() {
        return new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                if (sprint == null) {
                    return null;
                }

                List<PBChangeItem> changes = pbChangeCalculator.calculate(sprint.getProject(), sprint);

                Platform.runLater(() -> {
                    addedUserStories.setAll(
                            changes.stream()
                                    .filter(PBChangeItem::isAddedAfterSprint)
                                    .toList()
                    );

                    removedUserStories.setAll(
                            changes.stream()
                                    .filter(PBChangeItem::isRemovedAfterSprint)
                                    .toList()
                    );
                });

                return null;
            }
        };
    }
}
