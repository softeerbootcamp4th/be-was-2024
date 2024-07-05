# 학습 정리

## [ExecutorService]
- 병렬 작업시에 여러 개의 작업을 효율적으로 처리하기 위해 제공되는 JAVA 라이브러리
- ThreadPool을 이용해서 Task를 실행하고 관리한다.
- Task는 Queue를 통해 관리되고 ThreadPool에 있는 Thread 수보다 Task가 많으면 미 실행된 Task는 Queue에 저장되고, 실행을 마친 Thread로 할당되어 순차적으로 수행된다.


### 1. 객체 생성 방법
1. ThreadPoolExecutor로 초기화
````
ExecutorService executorService = new ThreadPoolExecutor(int corePoolSize, 
                                int maximumPoolSize, 
                                long keepAliveTime, 
                                TimeUnit unit, 
                                BlockingQueue<Runnable> workQueue);
````
2. Executors 클래스에서 제공하는 Static Factory Method(정적 팩토리 메소드)로 초기화
````
ExecutorService executorService = Executors.newCachedThreadPool();

ExecutorService executorService = Executors.newFixedThreadPool(int nThreads);

ExecutorService executorService = Executors.newSingleThreadExecutor();
````
- CachedThreadPool
    - 쓰레드를 캐싱하는 쓰레드풀 (일정시간동안 쓰레드를 검사하여 60초동안 작업이 없으면 Pool에서 제거한다.)
- FixedThreadPool
    - 고정된 개수의 쓰레드를 가진 쓰레드풀
- SingleThreadExecutor
    - 한 개의 쓰레드로 작업을 처리하는 쓰레드풀


### 2. 작업 실행 메서드
1. execute()
    - 리턴 타입이 void로 Task의 실행 결과나 Task의 상태를 알 수 없다.
2. submit()
    - Task를 할당하고 Future 타입의 결과값을 받는다. 
    - 결과값을 반환할 수 있도록 Callable을 구현한 Task를 인자로 준다.
3. invokeAny()
    - Task를 Collection에 넣어서 인자로 넘겨준다. 
    - 실행에 성공한 Task 중 하나의 리턴값을 반환한다.
4. invokeAll()
    - Task를 Collection에 넣어서 인자로 넘겨줄 수 있다. 
    - 모든 Task의 리턴값을 List<Future<>>로 반환한다.

### 3. 작업 종료 메서드
1. shutdown()
    - 실행 중인 모든 작업이 수행되면 종료한다.
2. shutdownNow()
    - 실행중인 Thread들을 즉시 종료시키려고 하지만 모든 Thread가 동시에 종료되는 것을 보장하지는 않고 실행되지 않은 Task를 반환한다.


## [Request Header 파싱]



### 출처
- https://simyeju.tistory.com/119