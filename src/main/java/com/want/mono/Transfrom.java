package com.want.mono;

import com.want.domain.User;
import reactor.core.publisher.Mono;

public class Transfrom {

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

        //
    }
}
