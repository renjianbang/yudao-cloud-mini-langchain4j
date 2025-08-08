package cn.iocoder.cloud.reactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * Reactor测试示例
 * 
 * 演示如何使用StepVerifier测试Reactor代码
 * 
 * @author iocoder
 */
public class ReactorTest {

    @Test
    public void testMono0() {
        Mono<String> mono = Mono.just("Hello Reactor");

        // 订阅（触发执行）
        mono.subscribe(System.out::println);

    }

    @Test
    public void flatMapTest() {
        Flux.just("user1", "user2")
                .flatMap(id -> Mono.just("detail of " + id))
                .subscribe(System.out::println);

    }

    @Test
    public void testMono() {
        Mono<String> mono = Mono.just("Hello Reactor");
        
        StepVerifier.create(mono)
            .expectNext("Hello Reactor")
            .expectComplete()
            .verify();
    }

    @Test
    public void testFlux() {
        Flux<Integer> flux = Flux.range(1, 5);
        
        StepVerifier.create(flux)
            .expectNext(1, 2, 3, 4, 5)
            .expectComplete()
            .verify();
    }

    @Test
    public void testFluxWithOperators() {
        Flux<Integer> flux = Flux.range(1, 10)
            .filter(i -> i % 2 == 0)
            .map(i -> i * 2);
        
        StepVerifier.create(flux)
            .expectNext(4, 8, 12, 16, 20)
            .expectComplete()
            .verify();
    }

    @Test
    public void testError() {
        Flux<Integer> flux = Flux.range(1, 5)
            .map(i -> {
                if (i == 3) throw new RuntimeException("测试错误");
                return i;
            });
        
        StepVerifier.create(flux)
            .expectNext(1, 2)
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    public void testErrorHandling() {
        Flux<Integer> flux = Flux.range(1, 5)
            .map(i -> {
                if (i == 3) throw new RuntimeException("测试错误");
                return i;
            })
            .onErrorReturn(-1);
        
        StepVerifier.create(flux)
            .expectNext(1, 2, -1)
            .expectComplete()
            .verify();
    }

    @Test
    public void testDelay() {
        Mono<String> mono = Mono.just("延迟消息")
            .delayElement(Duration.ofMillis(100));
        
        StepVerifier.create(mono)
            .expectNext("延迟消息")
            .expectComplete()
            .verify(Duration.ofSeconds(1));
    }

    @Test
    public void testZip() {
        Flux<String> names = Flux.just("Alice", "Bob");
        Flux<Integer> ages = Flux.just(25, 30);
        
        Flux<String> result = Flux.zip(names, ages, 
            (name, age) -> name + ":" + age);
        
        StepVerifier.create(result)
            .expectNext("Alice:25", "Bob:30")
            .expectComplete()
            .verify();
    }
}
