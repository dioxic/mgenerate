package uk.dioxic.mgenerate.core.operator;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator("number")
public class NumberOp implements Resolvable<Integer> {

    @OperatorProperty
    Integer min = Integer.MIN_VALUE;

    @OperatorProperty
    Integer max = Integer.MAX_VALUE;

    @Override
    public Integer resolve() {
        return FakerUtil.numberBetween(min, max);
    }
}