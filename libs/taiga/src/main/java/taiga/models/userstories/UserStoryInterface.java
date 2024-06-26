package taiga.models.userstories;

import taiga.models.common.Epic;

import java.util.Date;
import java.util.List;

public interface UserStoryInterface {
    Integer getAssignedTo();

    void setAssignedTo(Integer assignedTo);

    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    Object getDueDate();

    void setDueDate(Object dueDate);

    List<Epic> getEpics();

    void setEpics(List<Epic> epics);

    Date getFinishDate();

    void setFinishDate(Date finishDate);

    Integer getId();

    void setId(Integer id);

    Integer getMilestone();

    void setMilestone(Integer milestone);

    Integer getProject();

    void setProject(Integer project);

    Integer getStatus();

    void setStatus(Integer status);

    Double getTotalPoints();

    void setTotalPoints(Double totalPoints);

    Boolean getIsClosed();

    Integer getRef();

    String getSubject();
}
