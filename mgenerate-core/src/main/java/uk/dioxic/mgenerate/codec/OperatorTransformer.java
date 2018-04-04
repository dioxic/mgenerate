package uk.dioxic.mgenerate.codec;

import org.bson.Document;
import org.bson.Transformer;
import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.StringResolver;

import java.util.Map;

public class OperatorTransformer implements Transformer {
    @Override
    public Object transform(Object objectToTransform) {

        if (objectToTransform instanceof Document) {
            Document doc = (Document) objectToTransform;
            if (doc.size() == 1) {
                Map.Entry<String, Object> entry = doc.entrySet().iterator().next();
                String key = entry.getKey();
                if (entry.getValue() instanceof Document && OperatorFactory.canHandle(key)) {
                    return OperatorFactory.create(key, (Document) entry.getValue());
                }
            }
        }
        else if (objectToTransform instanceof String) {
            String value = (String) objectToTransform;
            if (OperatorFactory.canHandle(value)) {
                return OperatorFactory.create(value);
            }
            else if (StringResolver.canHandle(value)) {
                return new StringResolver(value, FakerUtil.instance());
            }
        }

        return objectToTransform;
    }
}
