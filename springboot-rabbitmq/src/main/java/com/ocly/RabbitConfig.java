package com.ocly;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by cy
 * 2017/12/12 10:55
 */
//@Configuration
public class RabbitConfig {

    @Bean
    public Queue helloQueue() {
        return new Queue("hello",false);
    }

    @Bean
    public Queue ObjectQueue() {
        return new Queue("user",false);
    }
    /**
     * 主题模式 更灵活
     */
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


    /**
     * 发布订阅模式
     */
    @Bean
    public Queue AMessage() {
        return new Queue("fanout.A");
    }

    @Bean
    public Queue BMessage() {
        return new Queue("fanout.B");
    }

    @Bean
    public Queue CMessage() {
        return new Queue("fanout.C");
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    @Bean
    Binding bindingExchangeA(Queue AMessage,FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(AMessage).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeB(Queue BMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(BMessage).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeC(Queue CMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(CMessage).to(fanoutExchange);
    }
}