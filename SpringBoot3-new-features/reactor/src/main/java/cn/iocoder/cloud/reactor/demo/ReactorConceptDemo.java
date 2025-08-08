package cn.iocoder.cloud.reactor.demo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Reactor Concept Demo (without external dependencies)
 * 
 * This example demonstrates reactive programming concepts using Java built-in features:
 * 1. Publisher-Subscriber pattern
 * 2. Asynchronous processing
 * 3. Stream-like operations
 * 4. Error handling
 * 
 * @author iocoder
 */
public class ReactorConceptDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Reactor Concept Demo ===");
        
        // 1. Demonstrate reactive concepts with CompletableFuture
        demonstrateAsyncProcessing();
        
        // 2. Demonstrate stream-like operations
        demonstrateStreamOperations();
        
        // 3. Demonstrate error handling
        demonstrateErrorHandling();
        
        // 4. Demonstrate backpressure concept
        demonstrateBackpressureConcept();
        
        Thread.sleep(2000);
        System.out.println("=== Demo Complete ===");
    }

    /**
     * Demonstrate asynchronous processing (similar to Reactor's async nature)
     */
    private static void demonstrateAsyncProcessing() {
        System.out.println("\n--- Async Processing Demo ---");
        
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Simulate Mono-like behavior with CompletableFuture
        CompletableFuture<String> future1 = CompletableFuture
            .supplyAsync(() -> {
                System.out.println("Processing task 1 on thread: " + Thread.currentThread().getName());
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return "Result 1";
            }, executor);
        
        // Simulate Flux-like behavior with multiple CompletableFutures
        List<CompletableFuture<String>> futures = Arrays.asList(
            CompletableFuture.supplyAsync(() -> {
                System.out.println("Processing task A on thread: " + Thread.currentThread().getName());
                return "Result A";
            }, executor),
            CompletableFuture.supplyAsync(() -> {
                System.out.println("Processing task B on thread: " + Thread.currentThread().getName());
                return "Result B";
            }, executor),
            CompletableFuture.supplyAsync(() -> {
                System.out.println("Processing task C on thread: " + Thread.currentThread().getName());
                return "Result C";
            }, executor)
        );
        
        // Subscribe to single result (Mono-like)
        future1.thenAccept(result -> System.out.println("Single result: " + result));
        
        // Subscribe to multiple results (Flux-like)
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenRun(() -> {
                futures.forEach(future -> {
                    try {
                        System.out.println("Multiple result: " + future.get());
                    } catch (Exception e) {
                        System.err.println("Error getting result: " + e.getMessage());
                    }
                });
            });
        
        executor.shutdown();
    }

    /**
     * Demonstrate stream-like operations (similar to Reactor operators)
     */
    private static void demonstrateStreamOperations() {
        System.out.println("\n--- Stream Operations Demo ---");
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Simulate Flux operations with Stream
        System.out.println("Original numbers: " + numbers);
        
        // Map operation (transform)
        List<Integer> doubled = numbers.stream()
            .map(n -> n * 2)
            .collect(java.util.stream.Collectors.toList());
        System.out.println("Doubled: " + doubled);
        
        // Filter operation
        List<Integer> evenNumbers = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(java.util.stream.Collectors.toList());
        System.out.println("Even numbers: " + evenNumbers);
        
        // Chain operations (like Reactor operator chaining)
        List<String> processed = numbers.stream()
            .filter(n -> n % 2 == 0)        // Filter even numbers
            .map(n -> n * 2)                // Double them
            .map(n -> "Number: " + n)       // Convert to string
            .limit(3)                       // Take first 3 (like take operator)
            .collect(java.util.stream.Collectors.toList());
        System.out.println("Chained operations result: " + processed);
        
        // Reduce operation (like Reactor's reduce)
        int sum = numbers.stream()
            .reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);
    }

    /**
     * Demonstrate error handling (similar to Reactor's error operators)
     */
    private static void demonstrateErrorHandling() {
        System.out.println("\n--- Error Handling Demo ---");
        
        List<String> data = Arrays.asList("1", "2", "invalid", "4", "5");
        
        // Simulate onErrorReturn behavior
        System.out.println("With error handling (return default on error):");
        data.stream()
            .map(s -> {
                try {
                    return Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing: " + s + ", returning default value -1");
                    return -1; // Default value like onErrorReturn
                }
            })
            .forEach(result -> System.out.println("Parsed result: " + result));
        
        // Simulate onErrorContinue behavior
        System.out.println("\nWith error handling (skip errors and continue):");
        data.stream()
            .mapToInt(s -> {
                try {
                    return Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid value: " + s);
                    return -999; // Marker for invalid values
                }
            })
            .filter(n -> n != -999) // Skip invalid values
            .forEach(result -> System.out.println("Valid result: " + result));
    }

    /**
     * Demonstrate backpressure concept
     */
    private static void demonstrateBackpressureConcept() {
        System.out.println("\n--- Backpressure Concept Demo ---");
        
        // Simulate fast producer, slow consumer scenario
        System.out.println("Fast producer, slow consumer scenario:");
        
        // Producer generates data quickly
        Stream<Integer> fastProducer = Stream.iterate(1, n -> n + 1).limit(10);
        
        // Consumer processes slowly
        Consumer<Integer> slowConsumer = item -> {
            try {
                Thread.sleep(100); // Simulate slow processing
                System.out.println("Consumed: " + item + " at " + System.currentTimeMillis() % 10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        
        // Without backpressure control, all items are produced immediately
        // but consumed slowly (this would cause memory issues in real scenarios)
        System.out.println("Processing without backpressure control:");
        long startTime = System.currentTimeMillis();
        fastProducer.forEach(item -> {
            System.out.println("Produced: " + item + " at " + (System.currentTimeMillis() - startTime));
            slowConsumer.accept(item);
        });
        
        System.out.println("In real Reactor, backpressure would control this flow automatically!");
    }

    /**
     * Simple Publisher-Subscriber pattern demonstration
     */
    static class SimplePublisher<T> {
        private Consumer<T> subscriber;
        
        public void subscribe(Consumer<T> subscriber) {
            this.subscriber = subscriber;
        }
        
        public void publish(T item) {
            if (subscriber != null) {
                subscriber.accept(item);
            }
        }
    }
    
    /**
     * Demonstrate simple Publisher-Subscriber pattern
     */
    private static void demonstratePublisherSubscriber() {
        System.out.println("\n--- Publisher-Subscriber Demo ---");
        
        SimplePublisher<String> publisher = new SimplePublisher<>();
        
        // Subscribe to the publisher
        publisher.subscribe(message -> 
            System.out.println("Received message: " + message));
        
        // Publish some messages
        publisher.publish("Hello");
        publisher.publish("Reactive");
        publisher.publish("World");
    }
}
