package com.example.code.services;

import com.example.code.repositories.ActivationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActivationCodeRabbitReceiver implements RabbitListenerConfigurer
{
    private final ActivationCodeRepository activationCodeRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.consumer.name}")
    private String exchange;
    @Value("${rabbitmq.consumer.routing.key}")
    private String routingkey;

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar)
    {
    }

    @RabbitListener(queues = "${rabbitmq.queue.producer.name}")
    public void receivedMessage(Map<Long, Long> subscriptionIdByTariffId) throws Exception
    {
        log.info("Got from rabbit subscriptionIdByTariffId = {}", subscriptionIdByTariffId);

        var tariffId = subscriptionIdByTariffId.values().stream().findAny().orElseThrow(Exception::new);
        var subscriptionId = subscriptionIdByTariffId.keySet().stream().findAny().orElseThrow(Exception::new);
        var codeId = activationCodeRepository.findFirstByTariffIdAndIsActive(tariffId, true)
                .orElseThrow(() -> new Exception("no active code by tariffId " + tariffId)).getActivation_code_id();

        log.info("Successfully found active codeId = {} and sent to the rabbit with subscriptionId = {}",
                codeId, subscriptionId);
        rabbitTemplate.convertAndSend(exchange, routingkey, Map.of(subscriptionId, codeId));
    }
}

