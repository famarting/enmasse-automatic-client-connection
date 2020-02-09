package io.famartin.test;

import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.reactivex.Flowable;

/**
 * TestApplication
 */
@ApplicationScoped
public class TestApplication {

    @Outgoing("outputaddr")
    public Flowable<String> generate() {
        return Flowable.interval(3, TimeUnit.SECONDS)
                .map(tick -> "Hello world");
    }

    @Incoming("inputaddr")
    void log(String msg){
        System.out.println("Message received "+msg);
    }

}