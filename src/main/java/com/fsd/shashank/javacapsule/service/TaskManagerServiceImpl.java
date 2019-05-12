package com.fsd.shashank.javacapsule.service;

import com.fsd.shashank.javacapsule.dto.TaskDto;
import com.fsd.shashank.javacapsule.entity.ParentTask;
import com.fsd.shashank.javacapsule.entity.Task;
import com.fsd.shashank.javacapsule.repositiry.ParentTaskRepo;
import com.fsd.shashank.javacapsule.repositiry.TaskRepo;
import com.fsd.shashank.javacapsule.utils.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskManagerServiceImpl implements TaskManagerService {

    @Autowired
    private ParentTaskRepo parentTaskRepo;

    @Autowired
    private TaskRepo taskRepo;


    @Override
    public boolean saveTask(TaskDto taskParam) {
        Integer parentId = null;
        if (StringUtils.isNotBlank(taskParam.getParentTask())) {
            Task taskParent = taskRepo.findByTask(taskParam.getParentTask());
            if (taskParent == null) {
                ParentTask parent = parentTaskRepo.findByParentTask(taskParam.getParentTask());
                if (parent == null) {
                    ParentTask parentTask = new ParentTask();
                    parentTask.setParentTask(taskParam.getParentTask());
                    parentTask = parentTaskRepo.save(parentTask);
                    parentId = parentTask.getParentId();
                } else {
                    parentId = parent.getParentId();
                }
            } else {
                ParentTask parentTask = parentTaskRepo.findByParentTask(taskParent.getTaskId().toString());
                if (parentTask != null && parentTask.getParentId() > 0) {
                    parentId = parentTask.getParentId();
                } else {
                    parentTask = new ParentTask();
                    parentTask.setParentTask(String.valueOf(taskParent.getTaskId()));
                    parentTask = parentTaskRepo.save(parentTask);
                    parentId = parentTask.getParentId();
                }
            }
        }
        Task task = new Task();
        task.setTaskId(taskParam.getTaskId());
        task.setTask(taskParam.getTask());
        task.setParentId(parentId);
        task.setStartDate(DateConverter.convert(taskParam.getStartDate()));
        task.setEndDate(DateConverter.convert(taskParam.getEndDate()));
        task.setPriority(taskParam.getPriority());

        task = taskRepo.save(task);
        if (task.getTaskId() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<TaskDto> getAllTasks() {
        List<TaskDto> taskList = new ArrayList<>();
        List<Task> tasks = (List<Task>) taskRepo.findAll();
        for (Task task : tasks) {
            TaskDto taskDto = getTaskDtoFromTask(task);
            taskList.add(taskDto);
        }
        return taskList;
    }

    private TaskDto getTaskDtoFromTask(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setTaskId(task.getTaskId());
        taskDto.setTask(task.getTask());

        if (task.getParentId() != null) {
            ParentTask parentTask = parentTaskRepo.findById(task.getParentId()).get();
            taskDto.setParentId(parentTask.getParentId());
            try {
                Integer parentTaskId = Integer.parseInt(parentTask.getParentTask());
                Task taskParent = taskRepo.findById(parentTaskId).get();
                taskDto.setParentTask(taskParent.getTask());
            } catch (NumberFormatException nfe) {
                taskDto.setParentTask(parentTask.getParentTask());
            }
        }

        taskDto.setStartDate(DateConverter.convert(task.getStartDate()));
        taskDto.setEndDate(DateConverter.convert(task.getEndDate()));
        taskDto.setPriority(task.getPriority());
        return taskDto;
    }

    @Override
    public TaskDto getTaskById(Integer taskId) {
        Task task = taskRepo.findById(taskId).get();
        TaskDto taskDto = getTaskDtoFromTask(task);
        return taskDto;
    }

    @Override
    public boolean deleteTask(Integer taskId) {
        taskRepo.deleteById(taskId);
        ParentTask parentTask = parentTaskRepo.findByParentTask(taskId.toString());
        List<Task> tasks = taskRepo.findByParentId(parentTask.getParentId());
        for (Task task : tasks) {
            task.setParentId(null);
            taskRepo.save(task);
        }
        return true;
    }
}
