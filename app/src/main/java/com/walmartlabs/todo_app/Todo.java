package com.walmartlabs.todo_app;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by akulka2 on 9/30/15.
 */
public class Todo implements Serializable{

    private static final long serialVersionUID = -3336410811829725994L;
    private String description;
    private String priority;
    private String dueDate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
