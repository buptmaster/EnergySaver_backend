package cn.edu.njupt.energy_saver.mqtt;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class MqttConfiguration {

    @Value("${mqtt.url}")
    private String url;

    @Value("${mqtt.clientId}")
    private String clientId;

    @Value("${mqtt.topic")
    private String topic;

    @Value("${mqtt.inbound.topic}")
    private String inboundTopic;

    @Autowired
    HandlerService handlerService;

    private MqttPahoMessageDrivenChannelAdapter adapter;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setServerURIs(url);
        return factory;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutBoundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(clientId, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(topic);
        return messageHandler;
    }

    @Bean
    public MessageProducer inbound() {
        adapter = new MqttPahoMessageDrivenChannelAdapter(url, clientId, inboundTopic, "alive");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            System.out.println(JSONObject.toJSON(message));
            handlerService.handleMessage(
                    message.getPayload().toString(),
                    message.getHeaders().get("mqtt_topic").toString());
        };
    }

    @Bean
    public MessageChannel mqttInputChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel mqttOutBoundChannel(){
        return new DirectChannel();
    }

    public void addTopic(String ...topics){
        if (adapter == null) adapter = ((MqttPahoMessageDrivenChannelAdapter) inbound());
        adapter.addTopic(topics);
    }

    public void removeTopic(String ...topic){
        if (adapter == null) adapter = ((MqttPahoMessageDrivenChannelAdapter) inbound());
        adapter.removeTopic(topic);
    }


}
