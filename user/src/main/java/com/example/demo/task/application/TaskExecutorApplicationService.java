/*
 * Copyright (c) 2022-2023 Baimei Tech .Inc. All rights Reserved.
 */

package com.example.demo.task.application;

import cn.hutool.json.JSONUtil;
import com.example.demo.infruastructure.enums.TaskTypeEnum;
import com.example.demo.infruastructure.exception.BaseCustomException;
import com.example.demo.task.domain.handler.TaskRetryHandler;
import com.example.demo.task.domain.service.TaskDomainService;
import com.example.demo.task.domain.vo.ConsumerMqTaskInfo;
import com.example.demo.task.domain.vo.SeparateEventTaskInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * description: 重试任务 操作入口
 *
 * @author: LJP
 * @date: 2024/9/27 10:10
 */
@Service
public class TaskExecutorApplicationService {
    @Resource
    private TaskDomainService taskDomainService;
    @Resource
    private TaskRetryHandler taskRetryHandler;

    /**
     * description: 任务报错后存储记录
     *
     * @author: LJP
     * @date: 2024/9/27 9:57
     */
    public void addTaskAfterError(String taskInfo, TaskTypeEnum taskType, String errorMessage) {
        taskDomainService.addTaskAfterError(taskInfo, taskType, errorMessage);
    }

    /**
     * description: 任务报错后存储记录
     *
     * @author: LJP
     * @date: 2024/9/27 9:57
     */
    public void addConsumerMqTaskAfterError(String message, String errorMessage, String topic, String consumerGroup) {
        // 当前是重试任务线程，只抛出异常，不重复保存任务
        if (taskRetryHandler.notSaveRetryTask(TaskTypeEnum.CONSUMER_MQ)) {
            throw new BaseCustomException(errorMessage);
        }
        ConsumerMqTaskInfo consumerMqTaskInfo = new ConsumerMqTaskInfo();
        consumerMqTaskInfo.setMessage(message);
        consumerMqTaskInfo.setTopic(topic);
        consumerMqTaskInfo.setConsumerGroup(consumerGroup);
        // 防止异常过长，截取前225位
        errorMessage = errorMessage.length() <= 225 ? errorMessage : errorMessage.substring(0, 225);
        addTaskAfterError(JSONUtil.toJsonStr(consumerMqTaskInfo), TaskTypeEnum.CONSUMER_MQ, errorMessage);
    }

    /**
     * description: 独立事件任务
     *
     * @author: LJP
     * @date: 2024/10/31 14:25
     */
    public void addSeparateEventTaskAfterError(String className, String methodName, List<String> parametersClass, String parameters, String errorMessage) {
        // 当前是重试任务线程，只抛出异常，不重复保存任务
        if (taskRetryHandler.notSaveRetryTask(TaskTypeEnum.SEPARATE_EVENT)) {
            throw new BaseCustomException(errorMessage);
        }
        SeparateEventTaskInfo taskInfo = new SeparateEventTaskInfo();
        taskInfo.setClassName(className);
        taskInfo.setMethodName(methodName);
        taskInfo.setParametersClassName(parametersClass);
        taskInfo.setParameters(parameters);
        taskDomainService.addTaskAfterError(JSONUtil.toJsonStr(taskInfo), TaskTypeEnum.SEPARATE_EVENT, errorMessage);
    }

    /**
     * description: 任务重试
     *
     * @author: LJP
     * @date: 2024/9/27 15:34
     */
    public void retry() {
        taskDomainService.retry();
    }

}
