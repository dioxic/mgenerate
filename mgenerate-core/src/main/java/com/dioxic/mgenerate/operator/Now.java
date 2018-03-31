package com.dioxic.mgenerate.operator;

import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.OperatorClass;

import java.util.Date;

@OperatorClass
public class Now implements Resolvable<Date> {

	@Override
	public Date resolve() {
		return new Date();
	}

}