package io.messaging.amqp.connect;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.microprofile.config.spi.ConfigSource;

import io.vertx.core.json.JsonObject;

public class MessagingConfigSource implements ConfigSource {

//    amqp-port=5672
//            amqp-host=localhost
//            amqp-username=demo-user
//            amqp-password=demo-user

    private static final Set<String> propertyNames;

    static {
        propertyNames = new HashSet<>();
        propertyNames.add("amqp-username");
        propertyNames.add("amqp-password");
        propertyNames.add("amqp-host");
        propertyNames.add("amqp-port");
        propertyNames.add("amqp-use-ssl");
    }

    @Override
    public Set<String> getPropertyNames() {
        return propertyNames;
    }

    @Override
    public Map<String, String> getProperties() {
        try {
            return loadConfig();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String getValue(String key) {
        try {
            return loadConfig().get(key);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String getName() {
        return "messaging-credentials-config";
    }

    private Map<String, String> loadConfig() throws IOException {
        System.out.println("Loading config!!!!!!!!!!!!!!");
        String file = System.getenv("MESSAGING_CONNECT_FILE");
        if (file == null) {
            return new HashMap<>();
        }
        JsonObject config = new JsonObject(new String(Files.readAllBytes(Paths.get(file))));
        Map<String, String> options = new HashMap<>();
        options.put("amqp-host", config.getString("host"));
        options.put("amqp-port", String.valueOf(config.getInteger("port")));
//        options.put("amqp-username", config.getString("user"));
//        options.put("amqp-password", config.getString("password"));
        options.put("amqp-use-ssl", String.valueOf("amqps".equals(config.getString("scheme"))));

        options.put("amqp-username", "@@serviceaccount@@");
        options.put("amqp-password", readTokenFromFile());

        return options;
    }

    private static String readTokenFromFile() throws IOException {
        return new String(Files.readAllBytes(Paths.get("/var/run/secrets/kubernetes.io/serviceaccount/token")), StandardCharsets.UTF_8);
    }

}
