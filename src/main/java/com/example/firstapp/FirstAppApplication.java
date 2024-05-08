package com.example.firstapp;

import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.EventListener;

import java.util.List;


@SpringBootApplication
@EnableFeignClients
@Log4j2
public class FirstAppApplication {

    @Autowired
    ShawnMendesProxy shawnMendesClient;

    public static void main(String[] args) {
        SpringApplication.run(FirstAppApplication.class, args);
    }

    @EventListener(ApplicationStartedEvent.class)
    public void makeRequestToShawnMendesEndpoint() {
        try {
            ShawnMendesResponse response = shawnMendesClient.makeSearchRequest("shawnmendes", 5);
            List<ShawnMendesResult> result = response.results();
            result.forEach(
                    shawnMendesResult ->
                            System.out.println(shawnMendesResult.trackName())
            );
        } catch (FeignException.FeignClientException feignException) {
            System.out.println("Client exception: " + feignException.status());
            log.error("Client exception: " + feignException.status());
        } catch (FeignException.FeignServerException feignException) {
            System.out.println("Server exception: " + feignException.status());
        } catch (FeignException feignException) {
            System.out.println(feignException.getMessage());
            System.out.println(feignException.status());
        }
    }
}
