package com.app.yf.myapplication.view.activity.diy;

import android.renderscript.Sampler;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME) //保留到什么时候（注解的生命同期）
@Target(TYPE) //目标，用在哪里
public @interface ContentView {

}
