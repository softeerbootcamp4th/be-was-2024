<h1><strong>쓰레드, 동기, 쓰레드풀, Runnable, Callable, Future</strong></h1>

---
<h1><strong>쓰레드란?</strong></h1>

![이미지 설명](https://velog.velcdn.com/images/yun8565/post/b791bf35-aee1-4f10-9fef-49c382578015/image.png)


프로세스 내에서 실제로 작업을 수행하는 주체이다.

프로세스의 코드에 정의된 절차에 따라 실행되는 특정한 수행 경로이다. 

Code, Data, Heap는 다른 쓰레드와 공유하며 pc, stack는 각각의 쓰레드가 독립적으로 가지고 있다.

따라서 프로세스 변경 시에 일어나는 컨텍스트 스위칭 비용보다 낮은 비용으로 사용이 가능하다.

**장점**

짧은 시간동안 여러 작업을 번갈하 수행함으로써 동시에 여러 작업이 수행되는 것처럼 보이게 되는 장점이 있다. 공유 자원을 이용하기 때문에 컨텍스트 스위칭 비용이 저렴하다는 장점이 있다.

다만, 프로세스의 성능이 단순하게 쓰레드의 개수에 비례하여 올라가는 것은 아니다.

**단점**

하나의 쓰레드에 문제가 발생해 공유된 자원을 잘못 건드리면 전체 프로세스가 영향을 받게된다.

Context Switching에서의 오버헤드가 발생하게 된다.

---

<h1><strong>Java에서의 쓰레드</strong></h1>

일반 쓰레드와 거의 차이가 없으며, JVM이 운영체제의 역할을 수행한다.

자바에는 프로세스가 존재하지 않고, 스레드만 존재한다. 자바 스레드는 JVM에 의해 스케줄되는 실행 단위 코드 블록이다.

자바에서 쓰레드 스케줄링은 전적으로 JVM이 관리한다.

다음과 같은 정보들도 JVM이 관리한다.

1\. 쓰레드가 몇 개 존재하는지

2\. 쓰레드로 실행되는 프로그램 코드의 메모리 위치는 어디인지

3\. 쓰레드의 상태는 무엇인지

4\. 쓰레드 우선순위는 얼마인지

\--> 개발자는 자바 쓰레드로 작동할 쓰레드 코드를 작성하고, 쓰레드 코드가 생명을 가지고 실행하도록 JVM에 요청하는 일을 하는 것이다.

---

<h1><strong>Thread  클래스</strong></h1>

Thread는 쓰레드 생성을 위해 Java에서 미리 구현해둔 클래스다.

start: 새로운 스레드를 시작하는 메서드이다. JVM은 이 스레드에서 run() 메서드를 호출한다. 쓰레드가 생성되었지만 아직 실행이 되지 않았다면 'NEW' 상태가 된다. 이 때 start() 메서드를 호출하면 'RUNNABLE' 상태로 전이된다.

여러 번 호출하는 것이 불가능하고 1번만 가능하다.

sleep: 현재 쓰레드를 멈추는 메서드이다. 자원을 놓아주지 않고, 제어권을 넘겨주므로 deadlock문제가 발생할 수 있다.

interrupt: 쓰레드가 일시 정지 상태에 있을 때 interruptedException을 발생시킨다. 이것을 이용하여 Thread의 run() 메소드를 정상 종료시킬 수 있다.

```
public class InterruptExample { 
  public static void main(String[] args) {
    Thread thread = new PrintThread();
    thread.start();

    try { Thread.sleep(1000); } catch (InterruptedException e) {}

    thread.interrupt();
  }
}
```

다음과 같이 사용하여 thread의 정지 상태를 유도하고 interruptedException을 발생시키는 것이다.

쓰레드의 인터럽트 메서드가 실행되면 즉시 예외가 발생하는 것이 아니라 이후에 정지 상태가 되면 예외가 발생하게된다. 따라서 정지 상태가 되지 않는다면 아무 의미가 없다.

일시 정지 상태를 만들지 않고도 interrupt() 호출 여부를 알 수 있는 방법이 있다. 스레드의 interrupt() 메소드가 호출되면 스레드의 interrupted() 와 isInterrupted() 메소드는 true를 반환하도록 되어 있다. Interrupted() 는 static 메소드로 현재 스레드가 interrupted 되었는지 확인하고, isInterrupted() 는 인스턴스 메소드로 현재 스레드가 interrupted 되었는지 확인 할 때 사용한다. 메서드 사용을 통해 인터럽트 상태를 확인하고 run의 실행을 마무리 할 수 있다.

_join_: 다른 쓰레드의 작업이 끝날 때까지 기다리게 하여 쓰레드의 순서를 제어하는데 사용할 수 있다.

쓰레드 클래스로 쓰레드를 구현하면 이를 상속받는 내부 클래스를 만들고, 내부에서 run()메서드를 구현해야 한다. 따라서 Thread의 run메서드를 실행하면 메인 쓰레드의 객체를 호출하는 것이다. 쓰레드를 새로 시작하기 위해서는 start메서드를 사용해야 한다.

```
@Test
void threadStart() {
    Thread thread = new MyThread();

    thread.start();
    System.out.println("Hello: " + Thread.currentThread().getName());
}

static class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread: " + Thread.currentThread().getName());
    }
}

// 출력 결과
// Hello: main
// Thread: Thread-2
출처: https://mangkyu.tistory.com/258 [MangKyu's Diary:티스토리]
```

---
<h1><strong>Runnable interface</strong></h1>

runnable 인터페이스는 추상 메서드 하나만을 갖는 인터페이스다. 따라서 람다로 사용이 가능하다.

```
@FunctionalInterface
public interface Runnable {
    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     */
    public abstract void run();
}
```

보다시피 반환타입이 존재하지 않기 때문에 주로 공유객체를 사용할 때 사용된다.

\---> Thread와 Runnable 모두 로우 레벨의 API에 의존하고, 값의 반환이 불가능하다. 또한 매번 쓰레드 생성과 종료하는 오버헤드가 발생한다. 직접 쓰레드를 생성하고 종료해야 하는데, 이는 비용이 많이 드는 작업이며 직접 쓰레드를 만드는 만큼 관리가 어렵다.

---
<h1><strong>동기(Synchronous)</strong></h1>

쓰레드를 이용한 프로그래밍을 하다보면 Race Condition에 대해 주의해야 한다.

Race Condition이란? 공통 자원을 읽거나 쓰는 동안 접근 순서에 따라 안정된 결과가 나오지 못 할 수 있는 문제이다.

```
class Counter {
    int count = 0;

    synchronized public void addOne() {
        count++;
    }

    synchronized public void subOne() {
        count--;
    }
}

class Solution {
    public static Counter counter = new Counter();

    public static void main(String[] args) throws InterruptedException {
        Runnable task1 = () -> {
            for (int i = 0; i < 1000; i++)
                counter.addOne();
        };

        Runnable task2 = () -> {
            for (int i = 0; i < 1000; i++)
                counter.subOne();
        };

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(MessageFormat.format("count: {0}", counter.count));
    }
}
```

위의 Synchronized 를 선언하면 그 객체 안에서 synchronized 키워드가 붙은 메서드 들에 대하여 잠금을 한 상태로 메서드를 실행할 수 있다. 그렇기에 addOne()가 실행될 때 subOne()가 읽힐 일이 없다.

![이미지 설명](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FIEW6n%2FbtrOQpSr76R%2FYnk6Q0lSoYSRIiyltvIVGK%2Fimg.jpg)

Synchronized에 static을 붙이냐와 안 붙이냐에 대한 차이가 존재한다.

```
package tests.synchronized_principal_test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SyncTest {
    private static int THREAD_COUNT = 200;
    public static Counter counter;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(THREAD_COUNT);
        AtomicInteger cnt = new AtomicInteger(0);

        for(int j=0; j<10000; j++) {
            CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

            for (int i = 0; i < THREAD_COUNT; i++) {
                counter = new Counter();
                service.submit(() -> {
                    try {
                        counter.addOne();
                        counter.subOne();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();

            if(counter.count!=0) {
                cnt.compareAndSet(cnt.get(), cnt.get()+1);
            }
        }

        System.out.println("1만개 중 200이 아닌 개수 = " + cnt);
    }


    static class Counter {
        static int count = 0;

        public static synchronized void addOne() {
            count++;
        }

        public static synchronized void subOne() {
            count--;
        }
    }
}
```

위와 같이 객체를 매번 만들면 클래스 단위의 락을 걸기 위해 static를 사용해야한다. 반대로 하나의 객체에 대해 동기를 진행하고 싶다면 static을 붙이지 않고 사용해도 된다.

---
<h1><strong>Thread Pool Model</strong></h1>

1\. 쓰레드 풀에 작업 처리 요청

2\. 쓰레드 풀에서 쓰레드를 자동으로 할당하여 작업 처리

3\. 작업 완료 후 쓰레드는 풀에 반환

위의 과정을 통해 이용 가능하다.

**장점**

쓰레드는 생성과 소멸에 소요되는 오버헤드가 크다. 하지만 쓰레드 풀 모델은 이러한 과정이 불필요 하게 된다.

작업 특성 상 여러 개의 쓰레드를 사용하는 경우가 많은데, 이 때 모든 쓰레드를 만들 필요는 없다. 그렇기에 작업 응답시간이 더욱 빨라질 수 있다.

---
<h1><strong>Collable</strong></h1>

Thread와 Runnable의 단점을 해결하기 위해 만들어진 인터페이스다.

쓰레드의 관리가 어려우며 값의 반환이 불가능(void()반환 타입) 을 해결할 수 있다.

Runnable의 경우 반환값을 얻기 위해서는 공용 메모리나 파이프를 사용해야 하는데 이 경우 매우 번거롭다. Java5에서 제네릭을 사용해 결과를 받을 수 있는 Callable이 추가된 것이다.

```
@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

---
<h1><strong>Future</strong></h1>

Callable 인터페이스의 구현체인 작업은 가용 가능한 쓰레드가 없거나, 작업 시간이 오래 걸려서 실행 결과를 바로 받지 못할 수 있다.

즉, 미래의 어느 시점에 실행 결과를 얻을 수 있는데 Future의 경우 미래에 완료된 Callable의 반환값을 구하기 위해 사용되는 것이다.

비동기 작업을 갖고 있어 미래에 실행 결과를 얻도록 도와준다. 그렇기에 다음과 같이 구성되어 있다.

```
public interface Future<V> {

    boolean cancel(boolean mayInterruptIfRunning);

    boolean isCancelled();

    boolean isDone();

    V get() throws InterruptedException, ExecutionException;

    V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}
```

_get_: 블로킹 방식으로 결과를 가져오며 타임아웃 설정이 가능하다. 블로킹을 통해 작업에 대한 결과가 발생했을 때 가져오는 역할을 한다.

_isDone, isCancelled_: 작업 완료 여부, 작업 취소 여부를 반환하며 boolean 반환 타입을 가진다.

_cancle_: 작업을 취소 시키며, 취소 여부를 boolean 반환 타입으로 반환한다. cancle 후에 isDone()는 항상 트루 값을 반환한다.

boolean 값을 전달 받는다. true를 전달 받으면 쓰레드를 interrupt 시켜 interruptException을 발생시키고, false를 전달 받으면 진행중인 작업이 끝날때까지 대기한다. 

작업이 이미 정상적으로 완료되어 취소할 수 없는 경우나 작업이 이미 취소되었거나 취소가 불가능한 경우 false를 반환하고, 그렇지 않다면 true를 반환한다.

---
<h1><strong>Executor</strong></h1>

동시에 여러 요청을 처리해야 하는 경우에 매번 새로운 쓰레드를 생성하는 것은 비효율적이다. 그렇기에 쓰레드를 미리 만들어두고 재사용하는 쓰레드풀이 등장하게 되었다. Executor 인터페이스는 쓰레드 풀의 구현을 위한 인터페이스다.

등록된 작업을 실행하기 위한 인터페이스이며 작업 등록과 작업 실행 중에 실행만을 책임진다.

<h1><strong>Executors</strong></h1>

직접 쓰레드를 다루는 것은 번거로우므로, 이를 도와주는 팩토리 클래스이다.

_newFixedThreadPool_: 고정된 쓰레드 개수를 갖는 쓰레드 풀을 생성한다. ExecutorService 인터페이스를 구현한 ThreadPoolExecutor 객체가 생성된다.

_newCachedThreadPool_: 필요할 때 필요한 만큼의 쓰레드 풀을 생성한다. 이미 생성된 쓰레드가 있다면 이를 재활용 할 수 있다.

_newScheculedThreadPool_: 일정 시간 뒤 혹은 주기적으로 실행되어야 하는 작업을 위한 쓰레드 풀을 생성한다.  ScheduledExecutorService 인터페이스를 구현한 ScheduledThreadPoolExecutor 객체가 생성된다.

_newSingleThreadExecutor, newSingleThreadScheduledExecutor_: 1개의 쓰레드만을 갖는 쓰레드 풀을 생성한다.

쓰레드 생성과 실행 및 관리가 매우 용이해지는 장점이 있지만 작업에 대한 특성을 정확하게 파악한 후에 구현해야 효율적인 프로그램을 작성할 수 있다.

<h1><strong>ExecutorService</strong></h1>

작업 등록을 위한 인터페이스다. Executor를 상속받아서 작업 등록 뿐만 아니라 실행을 위한 책임도 갖는다. 

![이미지 설명](https://blog.kakaocdn.net/dn/bcGfch/btrGEhU3Y4W/owqnKjYucjVNZDu4TwEdZ0/img.gif)


**라이프 사이클 관리를 위한 기능을 갖고 있다.**

_shutdown_: 새로운 작업을 더 이상 받아들이지 않는다.

_shutdownNow_: 셧다운 기능에 더해 이미 제출된 작업들도 인터럽트 시킨다. 실행을 위해 대기중인 작업 목록을 반환한다.

_isShutdown_: 셧다운 여부를 반환한다.

_isTerminated_: 셧다운 실행 후 모든 작업의 종료 여부를 반환한다.

_awaitTermination_: 셧다운 실행 후 지정한 시간 동안 모든 작업이 종료될 때 까지 대기한다. 지정한 시간 내에 모든 작업이 종료되었는지 여부를 반환한다.

ExecutorService를 만들어 작업을 실행하면, shutdown이 호출되기 전까지 계속해서 다음 작업을 대기하게 된다. 따라서 작업이 완료되면 반드시 shutdown을 호출해주어야 한다!!

**비동기 작업을 위한 기능을 갖고 있다.**

러너블과 컬러블 작업을 위한 메소드를 제공한다. 동시에 여러 작업들을 실행시키는 메소드를 갖고 있는데 비동기 작업의 진행을 추적할 수 있도록 Future를 반환한다.

_submit_: 실행할 작업들을 추가하고, 작업의 상태와 결과를 포함하는 Future를 반환한다. Future get를 호출하면 성공적으로 작업이 완료된 후에 결과를 얻을 수 있다.

_invokeAll_: 모든 결과가 나올 때까지 대기하는 블로킹 방식의 요청이다. 동시에 주어진 작업들을 모두 실행하고, 전부 끝나면 각각의 상태와 결과를 갖는 List<Future>를 반환한다.

_invokeAny_: 가장 빨리 실행된 결과가 나올 때까지 대기하는 블로킹 방식의 요청이다. 동시에 주어진 작업들을 모두 실행하고, 가장 빨리 완료된 하나의 결과를 Future로 반환 받는다.

---
<h1><strong>ScheduledExecutorService</strong></h1>

ExecutorService를 상속받는 인터페이스로 특정 시간 이후에 또는 주기적으로 작업을 실행시키기 위해 사용된다.

_schedule_: 특정 시간 이후에 작업을 실행시킨다.

_scheduleAtFixedRate_: 특정 시간 이후 처음 작업을 실행시킨다. 작업이 실행되고 특정 시간마다 작업을 실행시킨다.

_scheduleWithFixedDelay_: 특정 시간 이후 처음 작업을 실행시킨다. 작업이 완료되고 특정 시간이 지나면 작업을 실행시킨다.

예를 들면, 3초가 걸리는 작업이 있고 실행 주기가 5초면(초기 지연은 없다고 가정하자.) scheduleAtFixedRate는 0-3초에 실행을 하고 5초에 다시 작업을 실행한다. 하지만 scheduleWithFixedDelay의 경우에는 0-3초에 실행을 하고 5초를 대기하였다가 8초에 작업이 실행된다.
