package cn.iocoder.cloud.reactor.demo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Simple Reactor Demo
 * 
 * This example demonstrates:
 * 1. Basic Mono and Flux usage
 * 2. Simple operators
 * 3. Subscription
 * 
 * @author iocoder
 */
public class SimpleReactorDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Simple Reactor Demo ===");
        
        // 1. Mono example
        demonstrateMono();
        
        // 2. Flux example
        demonstrateFlux();
        
        // 3. Operators example
        demonstrateOperators();
        
        Thread.sleep(1000);
        System.out.println("=== Demo Complete ===");
    }

    /**
     * Demonstrate Mono usage
     */
    private static void demonstrateMono() {
        System.out.println("\n--- Mono Demo ---");
        
        // Create Mono with single value
        Mono<String> mono = Mono.just("Hello Reactor");
        mono.subscribe(value -> System.out.println("Mono value: " + value));
        
        // Empty Mono
        Mono<String> emptyMono = Mono.empty();
        emptyMono.subscribe(
            value -> System.out.println("Empty mono value: " + value),
            error -> System.err.println("Empty mono error: " + error.getMessage()),
            () -> System.out.println("Empty mono completed")
        );
    }

    /**
     * Demonstrate Flux usage
     */
    private static void demonstrateFlux() {
        System.out.println("\n--- Flux Demo ---");
        
        // Create Flux with multiple values
        Flux<String> flux = Flux.just("A", "B", "C", "D");
        flux.subscribe(value -> System.out.println("Flux value: " + value));
        
        // Create Flux from range
        Flux<Integer> rangeFlux = Flux.range(1, 5);
        rangeFlux.subscribe(value -> System.out.println("Range flux value: " + value));
    }

    /**
     * Demonstrate operators
     */
    private static void demonstrateOperators() {
        System.out.println("\n--- Operators Demo ---");
        
        // Map operator
        Flux.range(1, 5)
            .map(i -> i * 2)
            .subscribe(value -> System.out.println("Map result: " + value));
        
        // Filter operator
        Flux.range(1, 10)
            .filter(i -> i % 2 == 0)
            .subscribe(value -> System.out.println("Filter result: " + value));
        
        // Chain operators
        Flux.range(1, 10)
            .filter(i -> i % 2 == 0)  // Filter even numbers
            .map(i -> "Number: " + i)  // Convert to string
            .take(2)                   // Take first 2
            .subscribe(value -> System.out.println("Chain result: " + value));
    }
}
