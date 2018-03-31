package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.OperatorClass;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import org.bson.BsonRegularExpression;

@OperatorClass
public class Regex implements Resolvable<BsonRegularExpression> {

    @OperatorProperty
    Resolvable<String> string = OperatorFactory.wrap(".*");

    @OperatorProperty
    Resolvable<String> flags = OperatorFactory.wrap("");

    @Override
    public BsonRegularExpression resolve() {
        return new BsonRegularExpression(string.resolve(), flags.resolve());
    }

}