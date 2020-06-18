package com.app.yf.myapplication.view.activity.diy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //保留到什么时候（注解的生命同期）
@Target(ElementType.TYPE) //目标，用在哪里
public @interface ContentView {
    String value();
}
