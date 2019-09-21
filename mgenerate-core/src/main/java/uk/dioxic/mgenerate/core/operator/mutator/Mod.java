package uk.dioxic.mgenerate.core.operator.mutator;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;

@Operator
public class Mod implements Resolvable<Integer> {

    @OperatorProperty(required = true)
    Resolvable<Number> input;

    @OperatorProperty
    Integer mod = 720;

    @Override
    public Integer resolve() {
        return Math.floorMod(input.resolve().intValue(), mod);
    }
}