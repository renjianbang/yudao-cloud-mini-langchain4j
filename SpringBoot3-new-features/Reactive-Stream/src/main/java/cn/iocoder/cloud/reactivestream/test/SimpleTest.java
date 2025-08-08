package cn.iocoder.cloud.reactivestream.test;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple Reactive Stream Test
 * 
 * @author iocoder
 */
public class SimpleTest {
    
    public static class SimplePublisher implements Publisher<Integer> {
        private final int maxValue;
        
        public SimplePublisher(int maxValue) {
            this.maxValue = maxValue;
        }
        
        @Override
        public void subscribe(Subscriber<? super Integer> subscriber) {
            subscriber.onSubscribe(new SimpleSubscription(subscriber, maxValue));
        }
    }
    
    public static class SimpleSubscription implements Subscription {
        private final Subscriber<? super Integer> subscriber;
        private final int maxValue;
        private final AtomicLong requested = new AtomicLong(0);
        private int currentValue = 1;
        
        public SimpleSubscription(Subscriber<? super Integer> subscriber, int maxValue) {
            this.subscriber = subscriber;
            this.maxValue = maxValue;
        }
        
        @Override
        public void request(long n) {
            if (n <= 0) {
                subscriber.onError(new IllegalArgumentException("Request must be positive"));
                return;
            }
            
            long previousRequested = requested.getAndAdd(n);
            if (previousRequested == 0) {
                sendData();
            }
        }
        
        @Override
        public void cancel() {
            // Cancel subscription
        }
        
        private void sendData() {
            while (requested.get() > 0 && currentValue <= maxValue) {
                subscriber.onNext(currentValue);
                currentValue++;
                requested.decrementAndGet();
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    subscriber.onError(e);
                    return;
                }
            }
            
            if (currentValue > maxValue) {
                subscriber.onComplete();
            }
        }
    }
    
    public static class SimpleSubscriber implements Subscriber<Integer> {
        private Subscription subscription;
        
        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscription = subscription;
            System.out.println("Subscribed successfully");
            subscription.request(3);
        }
        
        @Override
        public void onNext(Integer item) {
            System.out.println("Received: " + item);
            if (item < 5) {
                subscription.request(1);
            }
        }
        
        @Override
        public void onError(Throwable throwable) {
            System.err.println("Error: " + throwable.getMessage());
        }
        
        @Override
        public void onComplete() {
            System.out.println("Stream completed!");
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Reactive Stream Simple Test ===");
        
        SimplePublisher publisher = new SimplePublisher(5);
        SimpleSubscriber subscriber = new SimpleSubscriber();
        
        publisher.subscribe(subscriber);
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Test completed!");
    }
}
