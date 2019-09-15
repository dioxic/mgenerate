package uk.dioxic.mgenerate.core.operator.core;

import uk.dioxic.mgenerate.common.Initializable;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.common.exception.ResolveException;
import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.core.util.ResolverUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator({"pickset", "pickSet"})
public class PickSet implements Resolvable<Set<Object>>, Initializable {

    @OperatorProperty(required = true)
    List<?> from;

    @OperatorProperty
    List<Integer> weights;

    @OperatorProperty
    Resolvable<Integer> quantity;

    private long minQuantity;

    @Override
    public Set<Object> resolve() {
        Integer quantity = this.quantity.resolve();

        if (quantity > minQuantity) {
            throw new ResolveException("specified quanitity of " + quantity + " is greater than the number of unique values available (" + minQuantity + ")");
        }

        Set<Object> result = new HashSet<>(quantity);
        Stream.generate(() -> FakerUtil.random().nextInt(from.size()))
                .map(from::get)
                .filter(o -> !result.contains(o))
                .limit(quantity)
                .forEach(result::add);

        return result;
    }

    @Override
    public void initialize() {
        if (weights != null) {
            minQuantity = weights.stream()
                    .filter(i -> i > 0)
                    .count();
        }
        else {
            minQuantity = new HashSet<>(from).size();
        }
        from = ResolverUtil.getWeightedArray(from, weights);
    }

}
