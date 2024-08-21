package com.quizmaster.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {


    @Value("${http://127.0.0.1:8081}")
    private String localServer;


    @Bean
    public OpenAPI openAPI()
    {


        Server localserver=new Server();
        localserver.setUrl(localServer);


        Info info=new Info();

        info.setTitle("QuizMaster App");
        info.setDescription("QuizMaster application api");

        info.setContact(new Contact().name("Sonia"));


        return new OpenAPI().info(info).servers(Arrays.asList(new Server[]{localserver}));
    }



}
