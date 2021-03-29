package com.want.mono;

import reactor.core.publisher.Mono;

/**
 * 进行过滤元素
 *
 * @author want
 * @createTime 2021.03.23.20:52
 */
public class Filter {


    public static void main(String[] args) {
        // 我想过滤一个序列 基于给定的判断条件：
        Mono.fromSupplier(() -> "str")
                .filter(str -> str.equals("str"));
        // 我想过滤一个序列 ​异步地进行判断：
        Mono.fromSupplier(() -> "str")
                .filterWhen(str -> Mono.just(str.endsWith("str")));

        // 我想过滤一个序列 仅限于指定类型的对象：
        Mono.fromSupplier(() -> "str")
                .ofType(Boolean.class)
                .subscribe(System.out::println);
        // 我想过滤一个序列 忽略所有元素
        Mono.fromSupplier(() -> "str")
                .ignoreElement();

    }
}
