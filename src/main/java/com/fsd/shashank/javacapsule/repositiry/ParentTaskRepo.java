package com.fsd.shashank.javacapsule.repositiry;

import com.fsd.shashank.javacapsule.entity.ParentTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentTaskRepo extends CrudRepository<ParentTask, Integer> {
    public ParentTask findByParentTask(String parentTasks);
}
