package com.fsd.shashank.javacapsule.service;

import com.fsd.shashank.javacapsule.dto.TaskDto;
import com.fsd.shashank.javacapsule.entity.Task;

import java.util.List;

public interface TaskManagerService {
    boolean saveTask(TaskDto taskParam);

    List<TaskDto> getAllTasks();

    TaskDto getTaskById(Integer taskId);

    boolean deleteTask(Integer taskId);
}
