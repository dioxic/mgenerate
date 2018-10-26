package uk.dioxic.mgenerate.core.resolver;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.core.DocumentValueCache;

public class DocumentKeyResolver implements Resolvable {
    private final String documentKey;

    DocumentKeyResolver(String documentKey) {
        this.documentKey = documentKey;
    }

    @Override
    public Object resolve() {
        return DocumentValueCache.get(documentKey);
    }
}