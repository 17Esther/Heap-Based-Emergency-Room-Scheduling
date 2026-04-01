# Heap-Based Emergency Room Scheduling (COMP47500 Assignment 3)

This project implements a heap-based priority queue for emergency room patient scheduling in Java.

## Priority Rule

1. Higher severity is treated first.
2. If severity is equal, earlier arrival is treated first.

## Implemented ADT Operations

- `insertPatient(...)` - `O(log n)`
- `peekNextPatient()` - `O(1)`
- `treatNextPatient()` - `O(log n)`
- `isEmpty()` - `O(1)`
- `size()` - `O(1)`

## Files

- `src/Patient.java` - patient model with severity and arrival order
- `src/EmergencyRoomHeap.java` - max-heap implementation
- `src/EmergencyRoomHeapTest.java` - correctness tests
- `src/EmergencyRoomSimulation.java` - runnable experiments (5 experiments)

## Compile

```bash
javac src/*.java
```

## Run

```bash
java -cp src EmergencyRoomSimulation
```

## Included Experiments

1. Insertion performance
2. Peek performance
3. Removal performance
4. Mixed emergency room workload simulation
5. Tie-case correctness (same severity, FIFO by arrival)
