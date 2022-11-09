package com.fastfood.configuration;

import com.messagebird.MessageBirdClient;
import com.messagebird.MessageBirdService;
import com.messagebird.MessageBirdServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fastfood.util.AppConstants.MESSAGE_BIRD_API_KEY;

@Configuration
public class MessageBirdConfiguration {


    @Bean
    public MessageBirdService getMessageBirdService(){
        return new MessageBirdServiceImpl(MESSAGE_BIRD_API_KEY);
    }

    @Bean MessageBirdClient getMessageBirdClient(){
        return new MessageBirdClient(getMessageBirdService());
    }


}
