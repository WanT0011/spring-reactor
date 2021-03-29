package com.want.mono;

import com.sun.istack.internal.logging.Logger;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.concurrent.*;

/**
 * @author WangZhiJian
 * @since 2021/3/28
 */
public class PublishOn {

    static Logger log = Logger.getLogger(PublishOn.class);

    public static void main(String[] args) throws InterruptedException {



        ExecutorService executorService = new ThreadPoolExecutor(4,4,0
                , TimeUnit.MINUTES,new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory());

//        Mono.just("t1")
//                .doOnNext(PublishOn::logThread)
//                .map(str -> {PublishOn.logThread(str);return str;})
//                .zipWith(Mono.just("t2").doOnNext(PublishOn::logThread))
//                .zipWith(Mono.just("t3").doOnNext(PublishOn::logThread).zipWith(Mono.just("t4").doOnNext(PublishOn::logThread)))
//                .publishOn(Schedulers.fromExecutor(executorService,true))
//                .subscribe(it -> PublishOn.logThread(""));

        /**
         * 三月 29, 2021 5:04:08 下午 [com.want.mono.PublishOn]  lambda$main$3
         * 信息: ForkJoinPool.commonPool-worker-9--get1
         * 三月 29, 2021 5:04:08 下午 [com.want.mono.PublishOn]  lambda$main$5
         * 信息: ForkJoinPool.commonPool-worker-2--get2
         * 三月 29, 2021 5:04:08 下午 [com.want.mono.PublishOn]  lambda$main$6
         * 信息: ForkJoinPool.commonPool-worker-2--[hello1,hello2]
         */
//        Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
//            String block = Mono.delay(Duration.ofMillis(900)).map(l -> "hello1").block();
//            PublishOn.logThread("get1");
//            return block;
//        })).zipWith(Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
//            String block = Mono.delay(Duration.ofSeconds(1)).map(l -> "hello2").block();
//            PublishOn.logThread("get2");
//            return block;
//        }))).subscribe(tuple2 -> PublishOn.logThread(tuple2.toString()));

        /**
         * 三月 29, 2021 5:05:11 下午 [com.want.mono.PublishOn]  lambda$main$10
         * 信息: ForkJoinPool.commonPool-worker-4--get2
         * 三月 29, 2021 5:05:11 下午 [com.want.mono.PublishOn]  lambda$main$8
         * 信息: ForkJoinPool.commonPool-worker-11--get1
         * 三月 29, 2021 5:05:11 下午 [com.want.mono.PublishOn]  lambda$main$11
         * 信息: ForkJoinPool.commonPool-worker-11--[hello1,hello2]
         */
//        Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
//            String block = Mono.delay(Duration.ofMillis(1100)).map(l -> "hello1").block();
//            PublishOn.logThread("get1");
//            return block;
//        })).zipWith(Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
//            String block = Mono.delay(Duration.ofSeconds(1)).map(l -> "hello2").block();
//            PublishOn.logThread("get2");
//            return block;
//        }))).subscribe(tuple2 -> PublishOn.logThread(tuple2.toString()));

        /**
         * 三月 29, 2021 5:23:00 下午 [com.want.mono.PublishOn]  lambda$main$3
         * 信息: ForkJoinPool.commonPool-worker-2--get2
         * 三月 29, 2021 5:23:00 下午 [com.want.mono.PublishOn]  onNext
         * 信息: ForkJoinPool.commonPool-worker-2--flatMap
         * 三月 29, 2021 5:23:00 下午 [com.want.mono.PublishOn]  lambda$main$1
         * 信息: ForkJoinPool.commonPool-worker-9--get1
         * 三月 29, 2021 5:23:00 下午 [com.want.mono.PublishOn]  lambda$main$5
         * 信息: ForkJoinPool.commonPool-worker-9--[hello1,flatMap]
         */
        Mono.zip(Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
            String block = Mono.delay(Duration.ofMillis(1100)).map(l -> "hello1").block();
            PublishOn.logThread("get1");
            return block;
        })),Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
            String block = Mono.delay(Duration.ofSeconds(1)).map(l -> "hello2").block();
            PublishOn.logThread("get2");
            return block;
        })).flatMap(str -> Mono.just("flatMap").zipWith(Mono.just("mono2"))).map(Tuple2::getT1).doOnNext(PublishOn::logThread))
                .subscribe(tuple2 -> PublishOn.logThread(tuple2.toString()));


//        CompletableFuture.completedFuture("t1")
//                .thenCompose()
        log.info(" main over!");
        Thread.sleep(2500);
        executorService.shutdown();
    }

    public static final void logThread(String str){
        Thread thread = Thread.currentThread();

        log.info(thread.getName() + "--" + str);
    }

}
