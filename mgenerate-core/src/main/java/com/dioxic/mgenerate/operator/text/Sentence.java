package com.dioxic.mgenerate.operator.text;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import com.dioxic.mgenerate.Resolvable;

@Operator
public class Sentence implements Resolvable<String> {

    @OperatorProperty
    Resolvable<Integer> words = OperatorFactory.wrap(1);

    @Override
    public String resolve() {
        return FakerUtil.instance().lorem().sentence(words.resolve());
    }

}
