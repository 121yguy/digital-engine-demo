package org.demo.logging.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.demo.common.constant.RabbitConstants;
import org.demo.common.domain.po.OperationLog;
import org.demo.logging.dao.LogDao;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LogConsumer {

    private LogDao logDao;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(RabbitConstants.LOGGING_QUEUE),
                    exchange = @Exchange(RabbitConstants.REGISTER_EXCHANGE)
            )
    )
    @RabbitHandler(isDefault = true)
    public void log(Message message) throws JsonProcessingException {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        OperationLog log = new ObjectMapper().readValue(json, OperationLog.class);
        logDao.insertLog(log);
    }

}
