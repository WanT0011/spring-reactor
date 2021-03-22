package com.want.mono;

import com.want.domain.User;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

public class Create {

    public static void main(String[] args) {

        Mono<User> mono = Mono.just(User.JESSE);
        Mono<User> mono1 = Mono.justOrEmpty(User.JESSE);

        // lazy
        Mono<User> objectMono = Mono.fromSupplier(() -> User.JESSE);
        Mono<User> defer = Mono.defer(() -> mono);
        Mono<Object> error1 = Mono.error(IllegalAccessError::new);

        // from 参数值给出的源
        Mono<User> fromCallable = Mono.fromCallable(() -> User.JESSE);
        Mono<User> userMono = Mono.fromFuture(CompletableFuture.supplyAsync(() -> User.JESSE));

        // empty
        Mono<Object> empty = Mono.empty();

        // error
        Mono<Object> error = Mono.error(new IllegalAccessError());

        // never
        Mono<Object> never = Mono.never();

        // 使用using结束后释放，但是在下游订阅前就释放
        Mono<Integer> using = Mono.using(ThreadLocal::new, threadLocal -> Mono.justOrEmpty(1), ThreadLocal::remove);
        // 订阅后再进行释放
        Mono<Integer> using1 = Mono.using(ThreadLocal::new, threadLocal -> Mono.justOrEmpty(1), ThreadLocal::remove,false);

        // 编程式创建
//        Mono.create(sink -> {
//            sink.
//        })
    }
}
