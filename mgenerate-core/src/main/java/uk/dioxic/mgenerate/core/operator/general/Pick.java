package uk.dioxic.mgenerate.core.operator.general;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;

import java.util.List;

@Operator
public class Pick extends AbstractOperator<Object> {

    @OperatorProperty(required = true)
    Resolvable<List> array;

    @OperatorProperty
    Resolvable<Integer> element = Wrapper.wrap(0);

    @Override
    public Object resolveInternal() {
        return array.resolve().get(element.resolve());
    }

}
