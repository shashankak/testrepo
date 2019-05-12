package com.fsd.shashank.javacapsule.entity;

import javax.persistence.*;

@Entity
@Table(name = "ParentTask")
public class ParentTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ParentID")
    private Integer parentId;

    @Column(name = "ParentTask")
    private String parentTask;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentTask() {
        return parentTask;
    }

    public void setParentTask(String parentTask) {
        this.parentTask = parentTask;
    }
}
