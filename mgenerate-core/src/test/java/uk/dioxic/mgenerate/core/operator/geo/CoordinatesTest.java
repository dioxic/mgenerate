package uk.dioxic.mgenerate.core.operator.geo;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class CoordinatesTest {

    @Test
    void resolve() {
        List<Number> longBounds = asList(0d, 10d);
        List<Number> latBounds = asList(-20, 0);

        Coordinates coordinates = new CoordinatesBuilder(ReflectiveTransformerRegistry.getInstance()).longBounds(longBounds).latBounds(latBounds).build();

        assertThat(coordinates.resolveInternal()).isNotNull();
        assertThat(coordinates.resolveInternal()).isInstanceOf(uk.dioxic.mgenerate.core.operator.type.Coordinates.class);
        uk.dioxic.mgenerate.core.operator.type.Coordinates p = coordinates.resolveInternal();
        assertThat(p.getX()).as("longitude").isBetween(longBounds.get(0).doubleValue(), longBounds.get(1).doubleValue());
        assertThat(p.getY()).as("latitude").isBetween(latBounds.get(0).doubleValue(), latBounds.get(1).doubleValue());
    }
}
