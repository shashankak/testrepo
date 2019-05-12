package com.fsd.shashank.javacapsule.controller;

import com.fsd.shashank.javacapsule.dto.FsdResponse;
import com.fsd.shashank.javacapsule.dto.TaskDto;
import com.fsd.shashank.javacapsule.service.TaskManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    @Autowired
    private TaskManagerService taskManagerService;

    @RequestMapping(path = "saveTask", method = RequestMethod.POST)
    public FsdResponse saveTestCase(@RequestBody TaskDto task) {
        FsdResponse response = new FsdResponse();
        response.setSuccess(taskManagerService.saveTask(task));
        return response;
    }

    @RequestMapping(path = "getAllTasks", method = RequestMethod.GET)
    public FsdResponse getAllTasks() {
        FsdResponse response = new FsdResponse();
        try {
            response.setData(taskManagerService.getAllTasks());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setSuccess(false);
        }
        return response;
    }

    @RequestMapping(path = "getTaskById/{taskId}", method = RequestMethod.GET)
    public FsdResponse getTaskById(@PathVariable("taskId") int taskId) {
        FsdResponse response = new FsdResponse();
        try {
            response.setData(taskManagerService.getTaskById(taskId));
            response.setSuccess(true);
        } catch (Exception e) {
            response.setSuccess(false);
        }
        return response;
    }

    @RequestMapping(path = "deleteTaskById/{taskId}", method = RequestMethod.DELETE)
    public FsdResponse deleteTaskById(@PathVariable("taskId") int taskId) {
        FsdResponse response = new FsdResponse();
        try {
            //response.setData(taskManagerService.getTaskById(taskId));
            response.setSuccess(taskManagerService.deleteTask(taskId));
        } catch (Exception e) {
            response.setSuccess(false);
        }
        return response;
    }
}
