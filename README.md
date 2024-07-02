# Phase1

java.util.concurrent 학습내용

Runnable vs Callable

<Runnable>

:  no argument, no return values.

public interface Runnable {

public abstract void run();

}

<Callable>

: no argument, has return values, can throws Exceptions.

Public interface Callable<V> {

V call() throws Exception;

}

Runnable의 run()은 Checked Exception을 발생시키지 않도록 설계되어있다.

다만 이것이 아예 Exception을 발생시키지 말아야한다는 뜻은 아니다.

`Runnable`을 `ExecutorService`에 제출하면, `ExecutorService`는 `Runnable`의 런타임 예외를 포착하고, `Future` 객체를 통해 `ExecutionException`으로 전달합니다.