package cn.iocoder.cloud.reactivestream.basic;

/**
 * Reactive Stream 规范介绍
 * 
 * 1. 为什么有Reactive Stream规范？
 * 
 * 传统的同步编程模型在处理大量数据或高并发场景时存在以下问题：
 * - 阻塞式I/O操作会导致线程等待，资源利用率低
 * - 背压(Backpressure)问题：生产者产生数据的速度超过消费者处理的速度
 * - 内存溢出：无法控制数据流的速度，可能导致内存耗尽
 * - 线程池耗尽：大量阻塞操作占用线程资源
 * 
 * Reactive Stream规范解决了这些问题：
 * - 异步非阻塞处理
 * - 背压控制机制
 * - 流式数据处理
 * - 更好的资源利用率
 * 
 * 2. Reactive Stream规范的核心原则：
 * - 异步处理：所有操作都是异步的，不会阻塞调用线程
 * - 非阻塞背压：消费者可以控制从生产者接收数据的速度
 * - 最小资源使用：高效利用系统资源
 * - 错误处理：提供统一的错误处理机制
 * 
 * 3. 适用场景：
 * - 高并发系统
 * - 大数据流处理
 * - 实时数据处理
 * - 微服务架构中的异步通信
 * - IoT数据处理
 * 
 * @author iocoder
 */
public class ReactiveStreamIntroduction {
    
    /**
     * 传统同步处理的问题示例
     */
    public static void traditionalSyncProblem() {
        System.out.println("=== 传统同步处理的问题 ===");
        
        // 模拟传统的同步数据处理
        System.out.println("开始处理大量数据...");
        long startTime = System.currentTimeMillis();
        
        // 模拟处理1000万条数据
        for (int i = 0; i < 10_000_000; i++) {
            // 模拟数据处理耗时
            if (i % 1_000_000 == 0) {
                System.out.println("已处理: " + i + " 条数据");
            }
            // 这里会阻塞当前线程
            processDataSync(i);
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("传统同步处理完成，耗时: " + (endTime - startTime) + "ms");
        System.out.println("问题：阻塞主线程，无法处理其他任务\n");
    }
    
    private static void processDataSync(int data) {
        // 模拟同步处理耗时
        try {
            Thread.sleep(0, 1000); // 1微秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * 背压问题示例
     */
    public static void backpressureProblem() {
        System.out.println("=== 背压问题示例 ===");
        
        // 模拟快速生产者和慢速消费者
        System.out.println("快速生产者 vs 慢速消费者");
        
        // 生产者每秒产生1000个数据
        int producerRate = 1000;
        // 消费者每秒只能处理100个数据
        int consumerRate = 100;
        
        System.out.println("生产者速率: " + producerRate + "/秒");
        System.out.println("消费者速率: " + consumerRate + "/秒");
        System.out.println("结果: 数据积压，可能导致内存溢出");
        System.out.println("解决方案: Reactive Stream的背压控制机制\n");
    }
    
    public static void main(String[] args) {
        System.out.println("Reactive Stream 规范学习");
        System.out.println("========================\n");
        
        traditionalSyncProblem();
        backpressureProblem();
        
        System.out.println("=== Reactive Stream的优势 ===");
        System.out.println("1. 异步非阻塞：不会阻塞调用线程");
        System.out.println("2. 背压控制：消费者可以控制数据接收速度");
        System.out.println("3. 流式处理：数据以流的形式处理，内存占用可控");
        System.out.println("4. 组合性：可以轻松组合多个异步操作");
        System.out.println("5. 错误处理：统一的错误处理机制");
    }
}
