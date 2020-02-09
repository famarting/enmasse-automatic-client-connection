package io.messaging.amqp.connect;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.enterprise.inject.Produces;

import io.vertx.amqp.AmqpClientOptions;
import io.vertx.core.json.JsonObject;

public class ClientOptionsProducer {

    //configuring via CDI not available yet in quarkus, not even released in smallrye side
//    @Produces
    public AmqpClientOptions connectOptions() throws IOException {
        System.out.println("Producing client options!!!!!!!!!!!!!!");
        String file = System.getenv("MESSAGING_CONNECT_FILE");
        if (file == null) {
            return new AmqpClientOptions();
        }
        JsonObject config = new JsonObject(new String(Files.readAllBytes(Paths.get(file))));
        AmqpClientOptions options = new AmqpClientOptions();

        options.setHost(config.getString("host"));
        options.setPort(config.getInteger("port"));

        options.setUsername(config.getString("user"));
        options.setPassword(config.getString("password"));

        options.setTrustAll(!config.getJsonObject("tls", new JsonObject()).getBoolean("verify", false));

        return options;
    }

}
