# Producer-Consumer Pattern Implementation

A thread-safe implementation of the classic producer-consumer pattern in Java, demonstrating concurrent programming concepts including thread synchronization, blocking queues, and wait/notify mechanisms.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Setup Instructions](#setup-instructions)
- [Usage](#usage)
- [Sample Output](#sample-output)
- [Testing](#testing)
- [Implementation Details](#implementation-details)
- [License](#license)

## 🎯 Overview

This project implements a multi-threaded producer-consumer pattern where:
- A **Producer** thread reads integers from a source list and places them into a shared bounded blocking queue
- A **Consumer** thread retrieves items from the queue and stores them in a destination list
- Thread synchronization ensures safe concurrent access to shared resources
- A "poison pill" pattern signals graceful shutdown

## ✨ Features

- **Thread-Safe Bounded Blocking Queue**: Custom implementation using `wait()` and `notifyAll()`
- **Configurable Capacity**: Set queue size to control buffer limits
- **Poison Pill Pattern**: Elegant shutdown mechanism for consumer termination
- **Comprehensive Logging**: Console output tracking production and consumption
- **Concurrent Data Transfer**: Simulates realistic concurrent processing scenarios

## 🏗️ Architecture

### Core Components

```
com.janhavi.challenge.producerconsumer/
├── BoundedBlockingQueue.java    # Thread-safe queue with capacity limits
├── Producer.java                # Produces items from source to queue
├── Consumer.java                # Consumes items from queue to destination
└── ProducerConsumerApp.java     # Main application orchestrator
```

### Class Diagram

```
┌─────────────────────────┐
│ BoundedBlockingQueue<T> │
├─────────────────────────┤
│ - queue: Queue<T>       │
│ - capacity: int         │
├─────────────────────────┤
│ + put(T): void          │
│ + take(): T             │
│ + size(): int           │
└─────────────────────────┘
           ▲
           │ uses
    ┌──────┴──────┐
    │             │
┌───────────┐ ┌──────────┐
│ Producer  │ │ Consumer │
├───────────┤ ├──────────┤
│ Runnable  │ │ Runnable │
└───────────┘ └──────────┘
```

## 🚀 Setup Instructions

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven or Gradle (optional, for dependency management)
- IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/producer-consumer-pattern.git
   cd producer-consumer-pattern
   ```

2. **Compile the project**
   ```bash
   javac -d bin src/com/janhavi/challenge/producerconsumer/*.java
   ```

3. **Run the application**
   ```bash
   java -cp bin com.janhavi.challenge.producerconsumer.ProducerConsumerApp
   ```

### Using an IDE

1. Import project as a Java project
2. Ensure source folder is set to `src/`
3. Run `ProducerConsumerApp.java`

## 💻 Usage

### Basic Example

```java
// Create source data
List<Integer> source = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
List<Integer> destination = new ArrayList<>();

// Configure queue and poison pill
int capacity = 3;
int poisonPill = -1;

// Initialize shared queue
BoundedBlockingQueue<Integer> queue = new BoundedBlockingQueue<>(capacity);

// Create and start threads
Thread producerThread = new Thread(
    new Producer(source, queue, poisonPill), 
    "ProducerThread"
);
Thread consumerThread = new Thread(
    new Consumer(queue, destination, poisonPill), 
    "ConsumerThread"
);

producerThread.start();
consumerThread.start();

// Wait for completion
producerThread.join();
consumerThread.join();

// View results
System.out.println("Final Destination: " + destination);
```

### Configuration Options

| Parameter | Description | Default |
|-----------|-------------|---------|
| `capacity` | Maximum queue size | 3 |
| `poisonPill` | Termination signal value | -1 |
| `source` | Input data list | [1..10] |

## 📊 Sample Output

```
ProducerThread produced 1
ProducerThread produced 2
[Consumer] Consumed: 1
ProducerThread produced 3
[Consumer] Consumed: 2
ProducerThread produced 4
[Consumer] Consumed: 3
ProducerThread produced 5
[Consumer] Consumed: 4
ProducerThread produced 6
[Consumer] Consumed: 5
ProducerThread produced 7
[Consumer] Consumed: 6
ProducerThread produced 8
[Consumer] Consumed: 7
ProducerThread produced 9
[Consumer] Consumed: 8
ProducerThread produced 10
[Consumer] Consumed: 9
ProducerThread produced -1
[Consumer] Consumed: 10
[Consumer] Received poison pill. Stopping.
=== Final Destination Content ===
[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
```

## 🧪 Testing

### Unit Test Structure

Create comprehensive tests covering:

**BoundedBlockingQueueTest.java**
```java
@Test
void testPutAndTake() throws InterruptedException
void testBlockingWhenFull() throws InterruptedException
void testBlockingWhenEmpty() throws InterruptedException
void testCapacityEnforcement()
void testConcurrentAccess() throws InterruptedException
```

**ProducerConsumerIntegrationTest.java**
```java
@Test
void testCompleteWorkflow()
void testMultipleProducersAndConsumers()
void testPoisonPillTermination()
void testEmptySource()
```

### Running Tests

```bash
# Using JUnit 5
mvn test

# Or with Gradle
gradle test
```

## 🔧 Implementation Details

### Thread Synchronization

The `BoundedBlockingQueue` uses intrinsic locks (synchronized blocks) and wait/notify mechanisms:

- **put()**: Blocks when queue is full, notifies waiting consumers after adding
- **take()**: Blocks when queue is empty, notifies waiting producers after removing

### Wait/Notify Pattern

```java
synchronized (this) {
    while (condition) {
        wait(); // Release lock and wait
    }
    // Perform operation
    notifyAll(); // Wake up waiting threads
}
```

### Poison Pill Pattern

A sentinel value (-1) signals the consumer to stop processing:
- Producer sends poison pill after all data
- Consumer exits upon receiving poison pill
- Ensures graceful shutdown without external interruption

### Thread Safety Guarantees

- All queue operations are atomic
- No race conditions on shared data structures
- Proper happens-before relationships established

## 📈 Performance Considerations

- **Queue Capacity**: Smaller capacity increases blocking frequency
- **Processing Time**: Consumer sleep (80ms) is slower than producer (50ms)
- **Scalability**: Pattern supports multiple producers/consumers with minor modifications

## 🎓 Learning Objectives

This implementation demonstrates:
- ✅ Thread synchronization using synchronized blocks
- ✅ Inter-thread communication via wait/notify
- ✅ Bounded blocking queue design pattern
- ✅ Producer-consumer coordination
- ✅ Graceful thread termination strategies
- ✅ Concurrent data structure implementation

## 📝 Future Enhancements

- Add support for multiple producers and consumers
- Implement timeout mechanisms for put/take operations
- Create generic batch processing capabilities
- Add metrics collection (throughput, latency)
- Implement priority queue variant


## 🙏 Acknowledgments

- Classic producer-consumer pattern from concurrent programming literature
- Java concurrency utilities and best practices
- Thread synchronization patterns from "Java Concurrency in Practice"

---

**Author**: Janhavi  
**Repository**: [https://github.com/yourusername/producer-consumer-pattern](https://github.com/yourusername/producer-consumer-pattern)  
**Last Updated**: December 2025