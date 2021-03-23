package com.want.mono;

import com.want.domain.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple4;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

public class Transformer {

    public static void main(String[] args) {
        // map flatmap filter default
        Mono.just(User.SKYLER)
                .map(User::getLastname)
                .flatMap(str -> Mono.fromSupplier(() -> str.charAt(0)))
                .filter(Character::isDigit)
                .defaultIfEmpty('0')
                .map(String::valueOf)
                .subscribe(System.out::println);

        // cast
        Number number = new Integer(1);
        Mono.just(number)
                .filter(num -> num instanceof Integer)
                .cast(Integer.class)
                .subscribe(System.out::println);
        // will throw Cannot cast java.lang.Double to java.lang.Integer
        /**
        number = new Double(1.0);
        Mono.just(number)
                .cast(Integer.class)
                .subscribe(System.out::println);
         */

        // 1 -> n
        Mono<char[]> mono = Mono.fromSupplier(() -> "ABCDE")
                .flatMap(str -> Mono.fromSupplier(str::toCharArray));

        // 1 -> n handle
        Mono<char[]> mono1 = Mono.fromSupplier(() -> "ABCDE")
                .handle((str, sink) -> {
                    char[] chars = str.toCharArray();
                    sink.next(chars);
                });

        // 忽略数据--》 或者进行替换
        // 如果mono的str为S开头进行替换成Empty
        Mono<String> stringMono = Mono.fromSupplier(() -> "Str")
                .flatMap(str -> {
                    if (str.startsWith("S")) {
                        return Mono.empty();
                    } else {
                        return Mono.just(str);
                    }
                });

        // mono 映射成多个flux
        Flux<String> stringFlux = Mono.fromSupplier(() -> "1,2,3")
                .flatMapMany(str -> Flux.fromArray(str.split(",")));

        // 合并  成Tuple2
        Mono<Tuple2<String, Integer>> tuple2Mono = Mono.fromSupplier(() -> "str")
                .zipWith(Mono.just(1));
        // 合并n个Mono
        Mono<Tuple4<String, Integer, Long, Double>> zip = Mono.zip(
                Mono.fromSupplier(() -> "str"),
                Mono.just(1),
                Mono.just(1L),
                Mono.just(7.0d)
        );

        // 在终止信号出现时“采取行动”,若是普通的 mono 效果等同于mono#when
        Mono.fromSupplier(() -> "str")
                .and(Mono.fromSupplier(() -> 888)
                        .doOnNext(System.out::println))
                .subscribe();
        // 在终止信号出现时“采取行动”
        Mono.when(
                Mono.fromSupplier(() -> "Mono.when")
                        .doOnNext(System.out::println),
                Mono.fromSupplier(() -> 111)
                        .doOnNext(System.out::println),
                Mono.fromSupplier(() -> 222)
                        .doOnNext(System.out::println)
        ).subscribe();

        // 取序列的第一个
        Mono.first(Mono.just("first"),Mono.fromSupplier(() -> "second").flatMap(str -> Mono.delay(Duration.ofMillis(100)).map(i -> str)))
                .subscribe(System.out::println);
        // 取序列的第一个
        Mono.just("first")
                .or(Mono.fromSupplier(() -> "second").flatMap(str -> Mono.delay(Duration.ofMillis(100)).map(i -> str)))
                .or(Mono.delay(Duration.ofMillis(200)).map(i -> "third"))
                .subscribe(System.out::println);

        // 我有一个空值想进行替换
        Mono.empty()
                .defaultIfEmpty("default")
                .subscribe(System.out::println);
        // 我对序列的元素值不感兴趣：并且我希望用 Mono 来表示序列已经结束
        Mono.fromSupplier(() -> "then")
                .doOnNext(System.out::println)
                .ignoreElement()
                .then()
                .subscribe();
        // 我对序列的元素值不感兴趣：并且我想在序列结束后等待另一个任务完成 thenEmpty内的mono必须是Void
        Mono.fromSupplier(() -> "我对序列的元素值不感兴趣：并且我想在序列结束后等待另一个任务完成")
                .doOnNext(System.out::println)
                .ignoreElement()
                .thenEmpty(Mono.delay(Duration.ofMillis(200))
                        .map(i -> "另一个任务")
                        .doOnNext(System.out::println)
                        .then())
                .block();
        // 我对序列的元素值不感兴趣：并且我想在序列结束之后返回一个 Mono
        Mono.fromSupplier(() -> "我对序列的元素值不感兴趣：并且我想在序列结束之后返回一个 Mono")
                .doOnNext(System.out::println)
                .ignoreElement()
                .then(Mono.delay(Duration.ofMillis(200))
                        .map(i -> "结束之后返回一个 的 Mono")
                        .doOnNext(System.out::println))
                .block();
        // 我对序列的元素值不感兴趣：​并且我想在序列结束之后返回一个值：
        Mono.delay(Duration.ofMillis(200))
                .map(i -> "我对序列的元素值不感兴趣：并且我想在序列结束之后返回一个值：")
                .doOnNext(System.out::println)
                .ignoreElement()
                .thenReturn(Boolean.TRUE)
                .doOnNext(System.out::println)
                .block();
        // 我对序列的元素值不感兴趣：​并且我想在序列结束之后返回一个 Flux
        Mono.delay(Duration.ofMillis(200))
                .map(i -> "我对序列的元素值不感兴趣：并且我想在序列结束之后返回一个 Flux")
                .doOnNext(System.out::println)
                .ignoreElement()
                .thenMany(Flux.just(1,2,3))
                .doOnNext(System.out::println)
                .blockLast();

        // 我有一个 Mono 但我想延迟完成 当有1个或N个其他 publishers 都发出（或结束）时才完成
        Mono.fromSupplier(() -> "我有一个 Mono 但我想延迟完成 当有1个或N个其他 publishers 都发出（或结束）时才完成")
                .doOnNext(System.out::println)
                .delaySubscription(Mono.delay(Duration.ofMillis(200)).map(i -> "其他 publishers").doOnNext(System.out::println))
                .block();
        // 我有一个 Mono 但我想延迟完成 使用一个函数式来定义如何获取“其他 publisher”
        Mono.fromSupplier(() -> "我有一个 Mono 但我想延迟完成 使用一个函数式来定义如何获取“其他 publisher”")
                .doOnNext(System.out::println)
                .delayUntil(str -> Mono.delay(Duration.ofMillis(200)).map(i -> "function create 其他 publishers").doOnNext(System.out::println))
                .block();

    }
}
