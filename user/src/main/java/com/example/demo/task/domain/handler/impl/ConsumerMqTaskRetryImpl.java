package com.example.demo.task.domain.handler.impl;

import cn.hutool.json.JSONUtil;
import com.example.demo.infruastructure.enums.TaskTypeEnum;
import com.example.demo.infruastructure.exception.BaseCustomException;
import com.example.demo.task.domain.entity.RetryTask;
import com.example.demo.task.domain.handler.TaskRetryService;
import com.example.demo.task.domain.vo.ConsumerMqTaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * description: 消费mq消息失败，重试策略实现
 *
 * @author: LJP
 * @date: 2024/9/27 15:50
 */
@Component
@Slf4j
public class ConsumerMqTaskRetryImpl implements TaskRetryService {

    private final Map<String, RocketMQListener> consumerMqMap;

    @Override
    public TaskTypeEnum getTaskTypeEnum() {
        return TaskTypeEnum.CONSUMER_MQ;
    }

    /**
     * description: 自动注入所有实现 RocketMQListener 接口的方法
     *
     * @author: LJP
     * @date: 2024/9/9 15:43
     */
    public ConsumerMqTaskRetryImpl(List<RocketMQListener> listenerList, Map<String, RocketMQListener> consumerMqMap) {
        this.consumerMqMap = consumerMqMap;
        for (RocketMQListener rocketMQListener : listenerList) {
            Annotation[] annotations = rocketMQListener.getClass().getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof RocketMQMessageListener) {
                    RocketMQMessageListener rocketMQMessageListener = (RocketMQMessageListener) annotation;
                    String topic = rocketMQMessageListener.topic();
                    String consumerGroup = rocketMQMessageListener.consumerGroup();
                    this.consumerMqMap.put(topic + "+" + consumerGroup, rocketMQListener);
                    break;
                }
            }
        }
    }

    @Override
    public void retry(RetryTask task) {
        // 根据不同的主题+消费组，执行不同的重试逻辑
        ConsumerMqTaskInfo consumerMqTaskInfo = JSONUtil.toBean(task.getTaskInfo(), ConsumerMqTaskInfo.class);
        String topic = consumerMqTaskInfo.getTopic();
        String consumerGroup = consumerMqTaskInfo.getConsumerGroup();
        RocketMQListener rocketMQListener = consumerMqMap.get(topic + "+" + consumerGroup);
        if (rocketMQListener == null) {
            throw new BaseCustomException(String.format("消费mq消息失败，重试策略实现，不支持的消息主题:[%s]", topic));
        }
        rocketMQListener.onMessage(consumerMqTaskInfo.getMessage());
    }


}
