package com.fsd.shashank.javacapsule.repositiry;

import com.fsd.shashank.javacapsule.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends CrudRepository<Task, Integer> {
    public Task findByTask(String task);

    public List<Task> findByParentId(Integer parentId);
}
