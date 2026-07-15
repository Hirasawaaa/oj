package org.example.oj.service;

import org.example.oj.entity.Submit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JudgeConsumer {
    @Autowired
    private SubmitService submitService;

    @RabbitListener(queues ="judge.queue",concurrency = "4")
    public void handleJudge(Long submitId){
        Submit submit=submitService.getById(submitId);
        if(submit==null)return;
        submitService.judge(submit);
    }
}
