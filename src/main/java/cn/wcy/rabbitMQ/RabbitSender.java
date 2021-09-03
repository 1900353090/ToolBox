package cn.wcy.rabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;
import java.util.Objects;
import java.util.UUID;

@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper mapper;

    //全局交换机
    private static String overallExchange;

    //配置全局交换机
    public void setOverallExchange(String exchange) {
        this.overallExchange = exchange;
    }

    /**
     * 配置消息到达对应队列回调类，只要发送到达就会触发
     * @author 王晨阳
     * @date 2021/5/16-18:26
     * @version 0.0.1
     */
    public <T extends RabbitTemplate.ConfirmCallback> void setConfirmCallback(T confirmCallback) {
        this.rabbitTemplate.setConfirmCallback(confirmCallback);
    }

    /**
     * 消息无法路由到对于的队列回调类
     * @author 王晨阳
     * @date 2021/5/16-18:26
     * @version 0.0.1
     */
    public <T extends RabbitTemplate.ReturnCallback> void setReturnCallback(T returnCallback) {
        this.rabbitTemplate.setReturnCallback(returnCallback);
    }

    /**
     * 根据全局交换机发送消息队列
     * @author 王晨阳
     * @date 2021/5/16-18:26
     * @version 0.0.1
     */
    public void sendByOverallExchange(String routeKey, @NonNull Object message) {
        this.send(this.overallExchange, routeKey, message);
    }

    /**
     * 指定交换机发送消息队列
     * @author 王晨阳
     * @date 2021/5/16-18:26
     * @version 0.0.1
     */
    public void send(String exchange, String routeKey, Object message) {
        if (Objects.isNull(message)) {
            throw new NullPointerException("message is null");
        }
        //第一个参数交换机，第二个参数路由key，第三个参数需要发送的消息，第四个参数CorrelationData消息唯一编号
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setType(MediaType.APPLICATION_JSON_VALUE);
        String json;
        try {
            if (message instanceof String){
                json = message.toString();
            }else {
                json = mapper.writeValueAsString(message);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("json 序列化 异常");
        }
        //设置 messageId
        if (StringUtils.isEmpty(messageProperties.getMessageId())) {
            messageProperties.setMessageId(UUID.randomUUID().toString().replace("-", ""));
        }
        if (StringUtils.isEmpty(messageProperties.getCorrelationId())) {
            // 设置 关联 id
            messageProperties.setCorrelationId(UUID.randomUUID().toString().replace("-", ""));
        }
        Message messageMQ = new Message(json.getBytes(), messageProperties);
        rabbitTemplate.convertAndSend(exchange, routeKey, messageMQ,
                new CorrelationData(messageProperties.getCorrelationId()));
    }

}
