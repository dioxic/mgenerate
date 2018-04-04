package uk.dioxic.mgenerate;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

public class DocumentValueCacheTest {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static JsonWriterSettings jws = JsonWriterSettings.builder()
            .indent(true)
            .build();

    @Test
    public void documentTest() throws IOException {
        Document doc = BsonUtil.parseFile("src/test/resources/lookup-test.json");

        DocumentValueCache.getKeys(doc).forEach(logger::debug);

        String outJson = BsonUtil.toJson(doc, jws);
        logger.debug(outJson);

        DocumentValueCache.setEncodingContext(doc);
        Object cachedValue = DocumentValueCache.get(doc, "c3");
        Object expected  = DocumentValueCache.get(doc, "c.cc.ccc");
        assertThat(cachedValue).as("is resolvable").isEqualTo(expected);
    }
}
