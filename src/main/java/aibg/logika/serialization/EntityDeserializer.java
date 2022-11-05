package aibg.logika.serialization;

import aibg.logika.Map.Entity.*;
import aibg.logika.Map.Tile.Tile;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class EntityDeserializer extends StdDeserializer {

    public EntityDeserializer() {
        this(null);
    }

    public EntityDeserializer(Class vc) {
        super(vc);
    }
    ObjectMapper mapper = new ObjectMapper();
    @Override
    public Entity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String entity = node.get("entity").asText();
        switch (entity) {
            case "NONE": {
                return new Empty();
            }
            case "BLACKHOLE": {
                return new Blackhole();
            }
            case "WORMHOLE": {
                return new Wormhole();
            }
            case "FENCE": {
                return new Fence();
            }
            default: {
                return null;
            }
        }

    }
}
