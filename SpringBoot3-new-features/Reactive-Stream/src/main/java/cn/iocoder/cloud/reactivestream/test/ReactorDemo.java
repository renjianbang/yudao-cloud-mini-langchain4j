package cn.iocoder.cloud.reactivestream.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Project Reactor Demo
 * 
 * @author iocoder
 */
public class ReactorDemo {
    
    public static void monoDemo() {
        System.out.println("=== Mono Demo ===");
        
        // Create Mono
        Mono<String> mono = Mono.just("Hello Reactive World");
        
        // Subscribe and process
        mono
            .map(String::toUpperCase)
            .map(s -> s + "!")
            .subscribe(
                data -> System.out.println("Mono result: " + data),
                error -> System.err.println("Mono error: " + error.getMessage()),
                () -> System.out.println("Mono completed")
            );
        
        // Empty Mono with default value
        Mono.empty()
            .defaultIfEmpty("Default Value")
            .subscribe(data -> System.out.println("Empty Mono with default: " + data));
        
        System.out.println();
    }
    
    public static void fluxDemo() {
        System.out.println("=== Flux Demo ===");
        
        // Create Flux
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);
        
        // Process data
        flux
            .filter(i -> i % 2 == 0)  // Filter even numbers
            .map(i -> i * 2)          // Double the values
            .subscribe(
                data -> System.out.println("Flux result: " + data),
                error -> System.err.println("Flux error: " + error.getMessage()),
                () -> System.out.println("Flux completed")
            );
        
        // Range Flux
        Flux.range(1, 5)
            .map(i -> "Item-" + i)
            .subscribe(data -> System.out.println("Range: " + data));
        
        System.out.println();
    }
    
    public static void backpressureDemo() {
        System.out.println("=== Backpressure Demo ===");
        
        // Create a fast producer
        Flux<Integer> fastProducer = Flux.range(1, 100)
            .delayElements(Duration.ofMillis(10));
        
        // Slow consumer with backpressure
        fastProducer
            .onBackpressureBuffer(5)  // Buffer size of 5
            .subscribe(
                data -> {
                    try {
                        Thread.sleep(100); // Slow processing
                        System.out.println("Processed: " + data);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                },
                error -> System.err.println("Backpressure error: " + error.getMessage()),
                () -> System.out.println("Backpressure demo completed")
            );
        
        // Wait for processing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println();
    }
    
    public static void asyncDemo() {
        System.out.println("=== Async Processing Demo ===");
        
        List<String> tasks = Arrays.asList("Task-1", "Task-2", "Task-3", "Task-4");
        
        Flux.fromIterable(tasks)
            .flatMap(task -> 
                Mono.fromCallable(() -> {
                    // Simulate async work
                    Thread.sleep(500);
                    return "Completed: " + task;
                })
                .subscribeOn(Schedulers.boundedElastic())
            )
            .subscribe(
                result -> System.out.println("Async result: " + result),
                error -> System.err.println("Async error: " + error.getMessage()),
                () -> System.out.println("Async processing completed")
            );
        
        // Wait for async processing
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println();
    }
    
    public static void errorHandlingDemo() {
        System.out.println("=== Error Handling Demo ===");
        
        Flux<Integer> errorFlux = Flux.just(1, 2, 0, 4, 5)
            .map(i -> {
                if (i == 0) {
                    throw new RuntimeException("Division by zero");
                }
                return 10 / i;
            });
        
        // Error recovery
        errorFlux
            .onErrorReturn(-1)  // Return -1 on error
            .subscribe(
                data -> System.out.println("Error recovery result: " + data),
                error -> System.err.println("Final error: " + error.getMessage()),
                () -> System.out.println("Error recovery completed")
            );
        
        // Error continue
        errorFlux
            .onErrorContinue((throwable, obj) -> {
                System.out.println("Skipping error data: " + obj + ", error: " + throwable.getMessage());
            })
            .subscribe(
                data -> System.out.println("Error continue result: " + data),
                error -> System.err.println("Final error: " + error.getMessage()),
                () -> System.out.println("Error continue completed")
            );
        
        System.out.println();
    }
    
    public static void compositionDemo() {
        System.out.println("=== Composition Demo ===");
        
        Flux<String> source1 = Flux.just("A", "B", "C");
        Flux<String> source2 = Flux.just("1", "2", "3");
        
        // Merge
        System.out.println("Merge:");
        Flux.merge(source1, source2)
            .subscribe(data -> System.out.println("  Merged: " + data));
        
        // Zip
        System.out.println("Zip:");
        Flux.zip(source1, source2)
            .map(tuple -> tuple.getT1() + tuple.getT2())
            .subscribe(data -> System.out.println("  Zipped: " + data));
        
        // Concat
        System.out.println("Concat:");
        source1.concatWith(source2)
            .subscribe(data -> System.out.println("  Concatenated: " + data));
        
        System.out.println();
    }
    
    public static void realWorldExample() {
        System.out.println("=== Real World Example: User Processing ===");
        
        // Simulate user data
        List<User> users = Arrays.asList(
            new User(1, "Alice", "alice@example.com", 25),
            new User(2, "Bob", "bob@example.com", 30),
            new User(3, "Charlie", "charlie@example.com", 22),
            new User(4, "Diana", "diana@example.com", 35)
        );
        
        // Process users reactively
        Flux.fromIterable(users)
            .filter(user -> user.getAge() >= 25)  // Filter by age
            .map(user -> user.getName() + " (" + user.getEmail() + ")")  // Transform
            .flatMap(userInfo -> 
                // Simulate async email sending
                Mono.fromCallable(() -> {
                    Thread.sleep(300); // Simulate network delay
                    return "Email sent to: " + userInfo;
                })
                .subscribeOn(Schedulers.boundedElastic())
            )
            .subscribe(
                result -> System.out.println(result),
                error -> System.err.println("Processing error: " + error.getMessage()),
                () -> System.out.println("User processing completed")
            );
        
        // Wait for processing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static class User {
        private final int id;
        private final String name;
        private final String email;
        private final int age;
        
        public User(int id, String name, String email, int age) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.age = age;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public int getAge() { return age; }
    }
    
    public static void main(String[] args) {
        System.out.println("Project Reactor Demo");
        System.out.println("===================\n");
        
        monoDemo();
        fluxDemo();
        backpressureDemo();
        asyncDemo();
        errorHandlingDemo();
        compositionDemo();
        realWorldExample();
        
        System.out.println("=== Summary ===");
        System.out.println("1. Mono: 0 or 1 element async sequence");
        System.out.println("2. Flux: 0 to N elements async sequence");
        System.out.println("3. Backpressure: Automatic handling of producer/consumer speed mismatch");
        System.out.println("4. Async Processing: Non-blocking operations with thread scheduling");
        System.out.println("5. Error Handling: Comprehensive error recovery mechanisms");
        System.out.println("6. Composition: Rich set of operators for data transformation");
        System.out.println("\nReactive Stream learning completed successfully!");
    }
}
