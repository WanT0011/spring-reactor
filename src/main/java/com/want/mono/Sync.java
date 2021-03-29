package com.want.mono;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

/**
 * @author want
 * @createTime 2021.03.24.22:22
 */
public class Sync {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 回到同步
        Mono.delay(Duration.ofSeconds(1))
                .then(Mono.just("1s after"))
                .doOnNext(System.out::println)
                .block();
        Mono.delay(Duration.ofSeconds(3))
                .then(Mono.just("3s after"))
                .doOnNext(System.out::println)
                .toFuture()
                .get();
        Mono.delay(Duration.ofSeconds(3))
                .then(Mono.just("3s after"))
                .doOnNext(System.out::println)
                .block(Duration.ofSeconds(2));


    }
}
