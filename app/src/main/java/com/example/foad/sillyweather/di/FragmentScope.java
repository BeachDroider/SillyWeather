package com.example.foad.sillyweather.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Scope
@Documented
@Retention(CLASS)
public @interface FragmentScope {}

