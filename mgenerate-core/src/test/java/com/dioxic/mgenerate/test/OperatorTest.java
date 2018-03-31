package com.dioxic.mgenerate.test;

import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.operator.*;
import com.dioxic.mgenerate.operator.Character;
import com.dioxic.mgenerate.operator.Number;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class OperatorTest {

    @Test
    public void array() {
        String of = "turnip";
        int number = 4;

        Array array = new ArrayBuilder()
                .of(of)
                .number(number)
                .build();

        assertThat(array).as("null check").isNotNull();
        assertThat(array.resolve()).as("has correct size").hasSize(number);
        assertThat(array.resolve()).as("is subset of expected values").containsOnly(of);
    }

    @Test
    public void choose() {
        List<String> from = Lists.newArrayList("fish", "bread", "turnip");
        List<Integer> weights = Lists.newArrayList(1, 2, 3);

        Choose choose = new ChooseBuilder()
                .from(from)
                .weights(weights)
                .build();

        assertThat(choose).as("null check").isNotNull();
        assertThat(choose.resolve()).as("an expected value").isIn(from);
    }

    @Test
    public void arrayChoose() {
        List<String> from = Lists.newArrayList("fish", "bread", "turnip");

        Number number = new NumberBuilder()
                .min(0)
                .max(5)
                .build();

        Choose choose = new ChooseBuilder()
                .from(from)
                .build();

        Array array = new ArrayBuilder()
                .of(choose)
                .number(number)
                .build();

        List<Object> resolved = array.resolve();

        assertThat(resolved).as("null check").isNotNull();
        assertThat(resolved).as("instance of array").isInstanceOf(List.class);
        assertThat(resolved).as("is subset of expected values").isSubsetOf(from);
        assertThat(resolved.size()).as("has correct size").isBetween(0, 5);
    }

    @Test
    public void objectId() {
        assertThat(OperatorFactory.create("objectid").resolve()).isInstanceOf(ObjectId.class);
    }

    @Test
    public void inc() {
        int start = 3;
        int step = 2;

        Inc inc = new IncBuilder()
                .start(start)
                .step(step)
                .build();

        assertThat(inc.resolve()).as("check starting value").isEqualTo(start);
        assertThat(inc.resolve()).as("check starting value").isEqualTo(start + step);
        assertThat(inc.resolve()).as("check starting value").isEqualTo(start + step + step);
    }


    @Test
    public void join() {
        String sep = "|";
        List<String> array = Lists.newArrayList("fish", "gofer", "beaver");

        Join join = new JoinBuilder().array(array).sep(sep).build();

        assertThat(join.resolve()).as("check concaternation").isEqualTo("fish|gofer|beaver");
    }

    @Test
    public void timestamp() {
        int i = 333;
        Timestamp timestamp = new TimestampBuilder().i(i).build();

        assertThat(timestamp.resolve().getInc()).as("check increment value").isEqualTo(i);
    }

    @Test
    public void numberDecimal() {
        long min = -20L;
        long max = 25L;
        int fixed = 2;

        NumberDecimal decimal = new NumberDecimalBuilder().fixed(fixed).min(min).max(max).build();

        assertThat(decimal.resolve()).as("check value").isBetween(BigDecimal.valueOf(min), BigDecimal.valueOf(max));
        assertThat(decimal.resolve().scale()).as("check scale").isEqualTo(fixed);
    }

    @Test
    public void pick() {
        List<String> array = Lists.newArrayList("fish", "turtle", "badger");
        int element = 2;

        Pick pick = new PickBuilder().array(array).element(element).build();

        assertThat(pick.resolve()).as("correct element").isEqualTo(array.get(element));
    }

    @Test
    public void character() {
        String pool = "ABCDE04[]";

        Character character = new CharacterBuilder().pool(pool).build();
        assertThat(character.resolve()).as("check pool").isIn(asCharacterList(pool));

        character = new CharacterBuilder().numeric(true).build();
        assertThat(character.resolve()).as("check numeric").matches(java.lang.Character::isDigit);

        character = new CharacterBuilder().casing("upper").build();
        assertThat(character.resolve()).as("check upper case").matches(java.lang.Character::isUpperCase);

        character = new CharacterBuilder().casing("lower").build();
        assertThat(character.resolve()).as("check lower case").matches(java.lang.Character::isLowerCase);

        character = new CharacterBuilder().alpha(true).build();
        assertThat(character.resolve()).as("check alpha").matches(java.lang.Character::isLetter);
    }

    @Test
    public void string() {
        String pool = "ABCDE04[]";
        int length = 7;

        StringOp stringOp = new StringOpBuilder().pool(pool).length(length).build();

        assertThat(stringOp.resolve()).hasSize(length);

        for (char c :stringOp.resolve().toCharArray()) {
            assertThat(c).isIn(asCharacterList(pool));
        }

    }

    private static List<java.lang.Character> asCharacterList(String s) {
        List<java.lang.Character> chars = new ArrayList<>();
        for (char c : s.toCharArray()) {
            chars.add(c);
        }
        return chars;
    }
}