package com.kkb.mvc;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 注解的作用：
 * 被次注解添加的方法，会被用于处理请求
 * 方法返回的内容，会直接重定向指定的页面
 */
public @interface ResponseView {
    String value();
}
