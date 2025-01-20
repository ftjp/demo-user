package com.example.demo.demo.task.domain.handler;


import com.example.demo.demo.task.domain.entity.RetryTask;
import com.example.demo.infruastructure.enums.TaskTypeEnum;
import org.springframework.stereotype.Service;

/**
 * description: 任务重试策略
 *
 * @author: LJP
 * @date: 2024/9/27 14:37
 */
@Service
public interface TaskRetryService {

    TaskTypeEnum getTaskTypeEnum();

    void retry(RetryTask retryTask);


}
