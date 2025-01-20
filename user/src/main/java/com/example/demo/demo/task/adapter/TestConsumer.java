/*
 * Copyright (c) 2022-2023 Baimei Tech .Inc. All rights Reserved.
 */

package com.example.demo.demo.task.adapter;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.demo.demo.task.application.TaskExecutorApplicationService;
import com.example.demo.infruastructure.util.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component
@RocketMQMessageListener(topic = "testTopic", selectorExpression = "*", consumerGroup = "test_topic_group")
public class TestConsumer implements RocketMQListener<String> {

    @Lazy
    @Resource
    private TaskExecutorApplicationService taskExecutorApplicationService;

    @Override
    public void onMessage(String message) {
        log.info("TestConsumer MQ:{},message{}", "testTopic", message);
        try {
            JSONObject jsonObject = JSONUtil.parseObj(message);
            Long longParam = jsonObject.getLong("longParam");
            Integer intParam = jsonObject.getInt("intParam");
            String stringParam = jsonObject.getStr("stringParam");
            ParamCheckUtil.checkParamNotNull("longParam", longParam, "intParam", intParam, "stringParam", stringParam);
            log.info("TestConsumer MQ:{},message{},消息消费成功", "testTopic", message);
        } catch (Exception e) {
            log.error("CancelTradeOrderSuccessConsumer error, param=" + message, e);
            taskExecutorApplicationService.addConsumerMqTaskAfterError(message, e.getMessage(),
                    "testTopic", "check_expire_account_point_success_cons_group");
        }
    }
}
