package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;

@OperatorClass
public class Number implements Resolvable<Integer> {

    @OperatorProperty
    Integer min = Integer.MIN_VALUE;

    @OperatorProperty
    Integer max = Integer.MAX_VALUE;

    @Override
    public Integer resolve() {
        return FakerUtil.numberBetween(min, max);
    }
}