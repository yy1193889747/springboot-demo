package com.ocly;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by cy
 * 2017/12/12 10:55
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue helloQueue() {
        return new Queue("hello",false);
    }

    @Bean
    public Queue ObjectQueue() {
        return new Queue("user",false);
    }
    @Bean
    public Queue topicone() {
        return new Queue("topic.one",false);
    }
    @Bean
    public Queue topictwo() {
        return new Queue("topic.two",false);
    }
    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicexchange");
    }
    @Bean
    Binding bindingtopicone(Queue topicone, TopicExchange exchange){
        return BindingBuilder.bind(topicone).to(exchange).with("topic.one");
    }

    /**
     *  topic exchange 的 routing key 必须是由 . 分割*
     *  * 可以表示任何单独的单词
     *  # 可以表示0个或任意多个单词.
     */
    @Bean
    Binding bindingtopictwo(Queue topictwo, TopicExchange exchange){
        return BindingBuilder.bind(topictwo).to(exchange).with("topic.#");
    }

}