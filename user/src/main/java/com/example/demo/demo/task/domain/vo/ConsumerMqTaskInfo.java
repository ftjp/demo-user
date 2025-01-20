package com.example.demo.demo.task.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description:  mq消息消费 任务信息
 *
 * @author: LJP
 * @date: 2025/1/16 10:17
 */
@Accessors(chain = true)
@Data
public class ConsumerMqTaskInfo {


    /**
     * mq消息
     */
    private String message;

    /**
     * topic
     */
    private String topic;

    /**
     * 消费组
     */
    private String consumerGroup;


}
