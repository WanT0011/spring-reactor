package com.want.mono;

import reactor.core.publisher.Mono;

/**
 * 这一些方法更加类似于在发布者发布过程中的一些事件监听，可以在相应的生命周期做一些事情，
 * 官方文档说：这些操作相当于是 查看 流/管道 中的数据
 *
 * @author want
 * @createTime 2021.03.23.20:33
 */
public class Event {

    public static void main(String[] args) {



        Mono.just("hello")
                // 发出元素时
                .doOnNext(str -> System.out.println("发出元素"))
                // 序列完成
                .doOnSuccess(str -> System.out.println("序列完成"))
                // 发生订阅时
                .doOnSubscribe(subscription -> System.out.println("发生订阅时"))
                // 日志
                .log()
                // 所有结束的情况（完成complete、错误error、取消cancel）
                .doFinally(signalType -> System.out.println(signalType.toString()))
                .subscribe();



        Mono.just("hello")
                // 取消
                .doOnCancel(() -> System.out.println("取消"))
                .subscribe()
                .dispose();

        Mono.error(IllegalArgumentException::new)
                // 发出元素时
                .doOnNext(str -> System.out.println("发出元素"))
                // 因错误终止
                .doOnError(ex -> System.out.println("抛出异常："+ex.getClass().getName()))
                .block();


    }
}
