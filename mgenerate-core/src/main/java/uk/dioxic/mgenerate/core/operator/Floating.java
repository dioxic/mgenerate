package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator({"floating", "float", "double"})
public class Floating implements Resolvable<Double> {

    @OperatorProperty
    Double min = Double.MIN_VALUE;

    @OperatorProperty
    Double max = Double.MAX_VALUE;

    @Override
    public Double resolve() {
        return FakerUtil.randomDouble(min, max);
    }
}