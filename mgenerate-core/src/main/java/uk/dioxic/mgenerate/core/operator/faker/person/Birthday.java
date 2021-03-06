package uk.dioxic.mgenerate.core.operator.faker.person;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.operator.type.AgeType;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.time.ZoneOffset;
import java.util.Date;

@Operator
public class Birthday extends AbstractOperator<Object> {

    @OperatorProperty
    Resolvable<AgeType> type = Wrapper.wrap(AgeType.DEFAULT);

    @OperatorProperty
    Resolvable<Boolean> string = Wrapper.wrap(Boolean.FALSE);

    @Override
    public Object resolveInternal() {
        AgeType ageType = type.resolve();
        return Date.from(FakerUtil.randomDate(ageType.getMinBirthday(), ageType.getMaxBirthday()).toInstant(ZoneOffset.UTC));
    }
}
