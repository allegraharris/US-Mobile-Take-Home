package com.harris.usmob.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Listener for Application Ready Event
 */
@Component
public class Listener implements ApplicationListener<ApplicationReadyEvent> {
    /**
     * Method to handle Application Ready Event
     * @param applicationReadyEvent Application Ready Event
     */
    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("Application Started! Access it at http://localhost:8080/home");
    }
}
