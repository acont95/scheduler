package scheduler;

import java.io.IOException;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class ConfigLoader {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private SimulationConfig config;

    public ConfigLoader() throws IOException {
        objectMapper.configure(Feature.ALLOW_COMMENTS, true);
        objectMapper.registerModule(new JavaTimeModule());

        config = objectMapper.readValue(Paths.get("config.jsonc").toFile(), SimulationConfig.class);
    }

    public SimulationConfig getConfig() {
        return config;
    }
}