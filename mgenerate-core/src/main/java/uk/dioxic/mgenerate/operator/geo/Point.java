package uk.dioxic.mgenerate.operator.geo;

import org.bson.Document;
import uk.dioxic.faker.resolvable.Resolvable;
import uk.dioxic.mgenerate.Initializable;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.mgenerate.operator.location.Coordinates;
import uk.dioxic.mgenerate.operator.location.CoordinatesBuilder;

import java.util.List;

import static java.util.Arrays.asList;

@Operator
public class Point implements Resolvable<Document>, Initializable {

    @OperatorProperty
    List<Number> long_lim = asList(-180d, 180d);

    @OperatorProperty
    List<Number> lat_lim = asList(-90d, 90d);

    private Coordinates coordinates;

    @Override
    public Document resolve() {
        Document doc = new Document();
        doc.put("type", "Point");
        doc.put("coordinates", coordinates.resolve());

        return doc;
    }

    @Override
    public void initialize() {
        coordinates = new CoordinatesBuilder()
                .long_lim(long_lim)
                .lat_lim(lat_lim)
                .build();
    }
}