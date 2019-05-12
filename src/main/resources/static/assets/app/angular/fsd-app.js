var fsdApp = angular.module('fsdApp', ['ngRoute']);
fsdApp.config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'pages/viewtask-view.html',
            controller: 'viewTaskController'
        })
        .when('/viewtask-view', {
            templateUrl: 'pages/viewtask-view.html',
            controller: 'viewTaskController'
        })
        .when('/addtask-view', {
            templateUrl: 'pages/addtask-view.html',
            controller: 'addTaskController'
        })
        .when('/edittask-view/:taskId', {
            templateUrl: 'pages/addtask-view.html',
            controller: 'addTaskController'
        })
});

fsdApp.service('TaskService', ['$http', function ($http) {
    this.addTask = function addTask(task) {
        return $http({
            method: 'POST',
            url: 'http://localhost:8080/taskmanager/saveTask',
            data: task
        });
    }
    this.getAllTasks = function getAllTasks() {
        return $http({
            method: 'GET',
            url: 'http://localhost:8080/taskmanager/getAllTasks'
        });
    }
    this.getTaskById = function getTaskById(taskId) {
        return $http({
            method: 'GET',
            url: 'http://localhost:8080/taskmanager/getTaskById/' + taskId
        });
    }
    this.deleteTaskById = function deleteTaskById(taskId) {
        return $http({
            method: 'DELETE',
            url: 'http://localhost:8080/taskmanager/deleteTaskById/' + taskId
        });
    }
}]);

fsdApp.controller('viewTaskController', ['$scope', 'TaskService',
    function ($scope, TaskService) {
        $scope.tasks = [];
        $scope.getAllTasks = function () {
            TaskService.getAllTasks()
                .then(function success(response) {
                        if (response.data.success === true) {
                            $scope.tasks = response.data.data;
                        }
                        $scope.message = '';
                        $scope.errorMessage = '';
                    },
                    function error(response) {
                        $scope.message = '';
                        $scope.errorMessage = 'Error getting tasks!';
                    });
        }
        $scope.getAllTasks();

        $scope.priorityFilter = function (task) {
            var min = $scope.filterPriorityFrom;
            var max = $scope.filterPriorityTo;
            if (min == undefined || max == undefined) {
                return true;
            }
            if (min != undefined && max != undefined
                && min != '' && max != '') {
                return (task.priority >= min && task.priority <= max);
            }
        }

        $scope.deleteTaskById = function (taskId) {
            TaskService.deleteTaskById(taskId)
                .then(function success(response) {
                        alert(JSON.stringify(response));
                        if (response.data.success === true) {
                            $scope.message = 'Task ended successfully!';
                        }
                        else {
                            $scope.errorMessage = 'Error while ending task!';
                        }
                    },
                    function error(response) {
                        $scope.message = '';
                        $scope.errorMessage = 'Error while ending task!';
                    });
        }
    }]);

fsdApp.controller('addTaskController', ['$scope', '$routeParams', 'TaskService',
    function ($scope, $routeParams, TaskService) {
        var updateTaskId = $routeParams.taskId;
        $scope.isUpdate = false;
        $scope.tasks = [];
        $scope.priority = 15;
        $scope.taskObject = {};
        $scope.taskObject.priority = $scope.priority;
        $scope.myslider = {
            min: 0,
            max: 30
        };

        $scope.getTaskById = function (taskId) {
            TaskService.getTaskById(taskId)
                .then(function success(response) {
                        if (response.data.success === true) {
                            $scope.taskObject = response.data.data;
                        }
                        $scope.message = '';
                        $scope.errorMessage = '';
                    },
                    function error(response) {
                        $scope.message = '';
                        $scope.errorMessage = 'Error getting tasks!';
                    });
        }

        if (updateTaskId != undefined && updateTaskId > 0) {
            $scope.getTaskById(updateTaskId);
            $scope.isUpdate = true;
        }

        $scope.addTask = function () {
            if ($scope.taskObject != null && $scope.taskObject.task) {
                TaskService.addTask($scope.taskObject)
                    .then(function success(response) {
                            $scope.message = 'Task added!';
                            $scope.errorMessage = '';
                        },
                        function error(response) {
                            $scope.errorMessage = 'Error adding task!';
                            $scope.message = '';
                        });
            }
            else {
                $scope.errorMessage = 'Please enter a task!';
                $scope.message = '';
            }
        }
        $scope.resetTask = function () {
            $scope.taskObject = {};
        }
    }]);

