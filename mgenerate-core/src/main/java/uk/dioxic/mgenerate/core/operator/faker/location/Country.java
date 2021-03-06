package uk.dioxic.mgenerate.core.operator.faker.location;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Country extends AbstractOperator<String> {

    @OperatorProperty
    Resolvable<Boolean> code = Wrapper.wrap(Boolean.FALSE);

	@Override
	public String resolveInternal() {
        return code.resolve() ? FakerUtil.getValue("address.country_code"): FakerUtil.getValue("address.country");
	}

}
