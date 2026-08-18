# Object-Oriented Design (OOD / Low-Level Design)

> **Scope** — The low-level-design round — class modelling, SOLID, and the design patterns interviewers actually ask for (strategy, observer, factory, state), with worked designs.
> **See also**: [design.md](./design.md) — LC design problems judged on operation complexity rather than class structure; [concurrency_patterns.md](./concurrency_patterns.md) — thread-safety in those designs.

## LeetCode Problem Lists

- [Design](https://leetcode.com/problem-list/design/)

## Overview

**OOD (Object-Oriented Design)**, also called **LLD (Low-Level Design)**, tests your ability to translate a real-world problem into a clean set of **classes, interfaces, and relationships**. You are graded less on a working end-to-end program and more on **modeling, responsibility separation, extensibility, and use of design patterns**.

### Key Properties
- **What it tests**: class modeling, encapsulation, SOLID principles, design pattern usage, and how gracefully your design absorbs new requirements.
- **Core Idea**: turn requirements (nouns → objects, verbs → methods) into a maintainable class graph.
- **When to Use**: interview prompts like "Design a Parking Lot / Elevator / Vending Machine / Card game", or any "design the classes for X" question.

### OOD (Low-Level) vs System Design (High-Level)

| Aspect | System Design (HLD) | OOD / LLD |
|--------|---------------------|-----------|
| Altitude | Services, databases, queues, caches, network | Classes, interfaces, methods, fields |
| Concerns | Scalability, availability, latency, sharding | Responsibilities, coupling, cohesion, patterns |
| Output | Architecture diagram, API contracts, data flow | Class diagram + key method signatures + code |
| Scale | Millions of users, distributed nodes | Single process, in-memory objects |
| Typical prompt | "Design a URL shortener" | "Design the classes for a parking lot" |

> Cross-reference: for **data-structure design** (LRU, LFU, All-O(1), Trie, iterators) and **system-level coding patterns** (consistent hashing, rate limiter, load balancer), see [`design.md`](design.md). This sheet focuses on **class modeling for OOD interviews** and does NOT duplicate the LRU/LFU content there.

### References
- SOLID principles (Robert C. Martin)
- Gang of Four (GoF) design patterns
- [`design.md`](design.md) — data structure & system coding patterns
- [`../faq/java/faq_OOP.md`](../faq/java/faq_OOP.md) — OOP fundamentals FAQ

---

## 0) Concept

### 0-1) The 5-Step Repeatable Approach ⭐⭐⭐⭐⭐

Apply these **five steps to ANY OOD prompt**. Narrate each step out loud in an interview.

```text
STEP 1: Clarify requirements & scope
   - What features are in / out of scope? (Say "I'll assume ... — is that OK?")
   - Functional: what must the system DO?
   - Non-functional: concurrency? persistence? scale? (usually keep in-memory, single process)
   - Nail down 3-5 concrete use cases before writing any class.

STEP 2: Identify core objects / entities (the NOUNS)
   - Scan requirements for nouns -> candidate classes.
   - "A parking lot HAS spots, a spot HOLDS a vehicle, a ticket TRACKS an entry."
   - Drop nouns that are just attributes (e.g. "color" is a field, not a class).

STEP 3: Define relationships (has-a / is-a, cardinality)
   - is-a  -> inheritance / interface implementation (Car IS-A Vehicle)
   - has-a -> composition / aggregation (ParkingLot HAS-A List<Level>)
   - cardinality: 1-to-1, 1-to-many, many-to-many
   - Prefer COMPOSITION over inheritance when unsure.

STEP 4: Design classes with fields, key methods & interfaces
   - Give each class ONE clear responsibility (SRP).
   - Program to interfaces, not implementations.
   - Enums for fixed sets (VehicleType, SpotType, Direction).
   - Sketch method signatures; don't implement everything.

STEP 5: Discuss design patterns, extensibility & edge cases
   - Which pattern fits? (Strategy for pricing, Factory for creation, State for lifecycle...)
   - "If we add feature X later, only class Y changes" (Open/Closed).
   - Edge cases: full capacity, invalid input, concurrency, null states.
```

### 0-2) Nouns → Classes, Verbs → Methods (mental model)

```text
Requirement sentence:
  "A customer inserts coins into a vending machine to buy a product."
                ^nouns: Customer, Coin, VendingMachine, Product
                ^verbs: insert, buy  -> methods on VendingMachine

  -> class VendingMachine { void insertCoin(Coin c); Product dispense(); }
  -> enum Coin { PENNY, NICKEL, DIME, QUARTER }
  -> class Product { String name; int price; }
```

---

## 1) General form

### 1-1) SOLID Principles ⭐⭐⭐⭐⭐

The single most-cited framework in OOD interviews. Memorize the one-liners.

| Principle | Meaning | Code smell it fixes |
|-----------|---------|---------------------|
| **S** — Single Responsibility | A class should have ONE reason to change | God class doing parsing + validation + DB + printing |
| **O** — Open/Closed | Open for extension, closed for modification | Editing a giant `if/switch` every time a new type is added |
| **L** — Liskov Substitution | Subtypes must be usable anywhere their base type is | `Square extends Rectangle` breaking `setWidth` behavior |
| **I** — Interface Segregation | Many small interfaces > one fat interface | Forcing a class to implement methods it throws `UnsupportedOperation` for |
| **D** — Dependency Inversion | Depend on abstractions, not concretions | High-level module hard-wired to a concrete `MySQLDatabase` |

```java
// java
// D — Dependency Inversion: depend on the INTERFACE, inject the concretion.
interface PaymentProcessor { boolean pay(double amount); }

class CardProcessor implements PaymentProcessor {
    public boolean pay(double amount) { /* ... */ return true; }
}

class Checkout {
    private final PaymentProcessor processor;         // abstraction, not concrete
    Checkout(PaymentProcessor processor) {            // injected
        this.processor = processor;
    }
    boolean buy(double amount) { return processor.pay(amount); }
}
// Swap CardProcessor -> WalletProcessor WITHOUT touching Checkout (Open/Closed too).
```

### 1-2) Relationship Cheat Table

| Relationship | UML | Meaning | Java expression |
|--------------|-----|---------|-----------------|
| **is-a** | ▷ (hollow arrow) | inheritance / subtype | `class Car extends Vehicle` / `implements Drivable` |
| **has-a (composition)** | ◆ (filled diamond) | part cannot outlive whole | `Engine` created & owned by `Car` |
| **has-a (aggregation)** | ◇ (hollow diamond) | part can exist independently | `Team` holds `List<Player>` but players outlive team |
| **uses-a (dependency)** | ┄> (dashed) | transient use (param/local) | method takes `Logger` as an argument |

**Rule of thumb: favor composition over inheritance.** Inheritance is rigid (one parent, tight coupling); composition lets you swap behavior at runtime and keeps classes small.

---

## 1-3) Key Design Patterns for OOD Interviews ⭐⭐⭐⭐

Know **when** to reach for each and be able to sketch the skeleton.

### **Strategy** — swap an algorithm at runtime
**When**: multiple interchangeable behaviors (pricing rules, sorting, routing, payment methods). Replaces branchy `if/switch`.

```java
// java
interface PricingStrategy { double price(long minutes); }

class FlatRate  implements PricingStrategy { public double price(long m){ return 5.0; } }
class PerMinute implements PricingStrategy { public double price(long m){ return 0.1 * m; } }

class ParkingBill {
    private PricingStrategy strategy;                 // holds a strategy
    void setStrategy(PricingStrategy s){ this.strategy = s; }
    double compute(long minutes){ return strategy.price(minutes); }
}
```

```python
# python — Strategy is often just a function/callable
class ParkingBill:
    def __init__(self, strategy):        # strategy: Callable[[int], float]
        self.strategy = strategy
    def compute(self, minutes):
        return self.strategy(minutes)

flat = lambda m: 5.0
per_minute = lambda m: 0.1 * m
ParkingBill(per_minute).compute(30)      # 3.0
```

#### **Factory** — centralize object creation
**When**: creation logic is complex or type is decided at runtime. Callers ask the factory instead of `new`-ing concretes (supports Open/Closed).

```java
// java
enum VehicleType { CAR, BIKE, TRUCK }

class VehicleFactory {
    static Vehicle create(VehicleType type) {
        switch (type) {
            case CAR:   return new Car();
            case BIKE:  return new Bike();
            case TRUCK: return new Truck();
            default: throw new IllegalArgumentException("unknown type");
        }
    }
}
```

#### **Singleton** — exactly one instance
**When**: shared coordinator/config/registry (a parking lot, a logger). Be ready to discuss thread safety.

```java
// java — thread-safe lazy singleton (holder idiom)
class ParkingLot {
    private ParkingLot() {}
    private static class Holder { static final ParkingLot INSTANCE = new ParkingLot(); }
    public static ParkingLot getInstance() { return Holder.INSTANCE; }
}
```

```python
# python — module-level object is the idiomatic singleton
class _ParkingLot:
    def __init__(self): self.levels = []
parking_lot = _ParkingLot()   # import this shared instance everywhere
```

#### **Observer** — publish/subscribe on state change
**When**: many objects must react to one object's changes (elevator display updates, event notifications, UI listeners).

```java
// java
interface Observer { void update(String event); }

class Subject {
    private final List<Observer> observers = new ArrayList<>();
    void subscribe(Observer o){ observers.add(o); }
    void notifyAll(String event){ for (Observer o : observers) o.update(event); }
}
```

#### **State** — behavior changes with internal state
**When**: an object has a lifecycle where the SAME method behaves differently per state (vending machine: NoCoin → HasCoin → Dispensing; elevator: Idle → Moving → DoorOpen). Replaces sprawling state flags.

```java
// java
interface MachineState { void insertCoin(VendingMachine m); void dispense(VendingMachine m); }

class NoCoinState implements MachineState {
    public void insertCoin(VendingMachine m){ m.setState(m.hasCoin); }   // transition
    public void dispense(VendingMachine m){ System.out.println("insert coin first"); }
}
```

#### **Decorator** — add behavior without subclassing
**When**: optional, stackable features (coffee + milk + sugar; a spot with EV-charging). Avoids class explosion from combinations.

```java
// java
interface Coffee { double cost(); }
class Espresso implements Coffee { public double cost(){ return 2.0; } }

abstract class CoffeeDecorator implements Coffee {
    protected final Coffee inner;
    CoffeeDecorator(Coffee inner){ this.inner = inner; }
}
class Milk extends CoffeeDecorator {
    Milk(Coffee c){ super(c); }
    public double cost(){ return inner.cost() + 0.5; }   // wraps + extends
}
// new Milk(new Espresso()).cost() == 2.5
```

#### **Adapter** — make incompatible interfaces work together
**When**: integrating a third-party / legacy class whose interface doesn't match what your code expects.

```java
// java
interface JsonLogger { void logJson(String json); }

class LegacyTextLogger { void writeLine(String text){ /* ... */ } }  // incompatible

class LoggerAdapter implements JsonLogger {
    private final LegacyTextLogger legacy;
    LoggerAdapter(LegacyTextLogger legacy){ this.legacy = legacy; }
    public void logJson(String json){ legacy.writeLine(json); }      // translate call
}
```

**Quick pattern-selection table:**

| Symptom in the prompt | Reach for |
|-----------------------|-----------|
| "It should support multiple pricing / payment / ranking rules" | **Strategy** |
| "Create different kinds of X depending on input" | **Factory** |
| "There is exactly one shared controller / registry" | **Singleton** |
| "When X changes, notify all the Ys" | **Observer** |
| "The object behaves differently in each phase of its lifecycle" | **State** |
| "Add optional features that can be combined" | **Decorator** |
| "Bridge an existing/legacy/3rd-party interface" | **Adapter** |

---

## 2) Classic OOD Problems (worked designs)

### 2-1) Parking Lot ⭐⭐⭐⭐⭐

**Requirements**
- Multiple levels; each level has spots of types (COMPACT, LARGE, MOTORCYCLE, EV).
- Vehicles (Car, Bike, Truck) fit certain spot types.
- Park a vehicle → issue a **Ticket**; unpark → compute fee.
- Report availability per level.

**Core classes & relationships**
```text
ParkingLot (Singleton)  ──has-a──▶ List<Level>
Level                   ──has-a──▶ List<ParkingSpot>
ParkingSpot             ──holds──▶ Vehicle (0..1)
Vehicle (abstract)      ◁── Car, Bike, Truck            (is-a)
Ticket                  ──refs──▶ Vehicle, ParkingSpot, entryTime
PricingStrategy         (Strategy)  used by ParkingLot to compute fee
VehicleType, SpotType   (enums)
```

**Patterns used**: Singleton (`ParkingLot`), Strategy (pricing), Factory (vehicle/spot creation), enums for fixed sets.

```java
// java — illustrative skeleton
enum VehicleType { CAR, BIKE, TRUCK }
enum SpotType    { COMPACT, LARGE, MOTORCYCLE, EV }

abstract class Vehicle {
    private final String plate;
    private final VehicleType type;
    Vehicle(String plate, VehicleType type){ this.plate = plate; this.type = type; }
    VehicleType getType(){ return type; }
    abstract boolean canFitIn(SpotType spot);
}

class Car extends Vehicle {
    Car(String plate){ super(plate, VehicleType.CAR); }
    boolean canFitIn(SpotType s){ return s == SpotType.COMPACT || s == SpotType.LARGE; }
}

class ParkingSpot {
    private final String id;
    private final SpotType type;
    private Vehicle vehicle;                 // null == free
    ParkingSpot(String id, SpotType type){ this.id = id; this.type = type; }
    boolean isFree(){ return vehicle == null; }
    boolean assign(Vehicle v){
        if (!isFree() || !v.canFitIn(type)) return false;
        this.vehicle = v; return true;
    }
    void release(){ this.vehicle = null; }
    SpotType getType(){ return type; }
}

class Ticket {
    final String id; final Vehicle vehicle; final ParkingSpot spot; final long entryTime;
    Ticket(String id, Vehicle v, ParkingSpot s){
        this.id = id; this.vehicle = v; this.spot = s; this.entryTime = System.currentTimeMillis();
    }
}

interface PricingStrategy { double fee(Ticket t, long exitTime); }

class Level {
    private final List<ParkingSpot> spots;
    Level(List<ParkingSpot> spots){ this.spots = spots; }
    // find first free spot the vehicle fits into. time = O(n) over spots
    ParkingSpot findSpot(Vehicle v){
        for (ParkingSpot s : spots)
            if (s.isFree() && v.canFitIn(s.getType())) return s;
        return null;
    }
}

class ParkingLot {                                   // Singleton
    private static final ParkingLot INSTANCE = new ParkingLot();
    private ParkingLot(){}
    public static ParkingLot getInstance(){ return INSTANCE; }

    private final List<Level> levels = new ArrayList<>();
    private PricingStrategy pricing;
    private final Map<String, Ticket> active = new HashMap<>();

    Ticket park(Vehicle v){
        for (Level level : levels){
            ParkingSpot spot = level.findSpot(v);
            if (spot != null && spot.assign(v)){
                Ticket t = new Ticket(UUID.randomUUID().toString(), v, spot);
                active.put(t.id, t);
                return t;
            }
        }
        return null;                                 // lot full for this vehicle
    }

    double unpark(String ticketId){
        Ticket t = active.remove(ticketId);
        if (t == null) throw new IllegalArgumentException("invalid ticket");
        t.spot.release();
        return pricing.fee(t, System.currentTimeMillis());
    }
}
```

**Extensibility talking points**: new vehicle type → add a `Vehicle` subclass (no edits to `Level`/`ParkingLot`); new pricing → new `PricingStrategy` (Open/Closed).

> **LC counterpart**: LC 1603 *Design Parking System* is this prompt stripped to its smallest honest version (counters only, no `Spot`/`Ticket` objects). See [§6-3](#6-3-worked-bridge--lc-1603-design-parking-system-) for the LC answer and the exact requirement that forces you back up to the full model above.

---

### 2-2) Elevator System ⭐⭐⭐⭐

**Requirements**
- Multiple elevators, N floors.
- Handle external requests (up/down at a floor) and internal requests (go to floor X).
- Scheduler decides which elevator serves a request.
- Each elevator has direction + state (IDLE, MOVING, DOOR_OPEN).

**Core classes & relationships**
```text
ElevatorSystem  ──has-a──▶ List<Elevator>, Scheduler
Scheduler       (Strategy)  picks best elevator for a Request
Elevator        ──has-a──▶ Direction, ElevatorState, TreeSet<Integer> stops
Request         { floor, Direction }     (external)  or { targetFloor } (internal)
Direction       enum { UP, DOWN, IDLE }
ElevatorState   (State)  IDLE / MOVING / DOOR_OPEN
Observer        floor displays subscribe to elevator position changes
```

**Patterns used**: Strategy (scheduling algorithm), State (elevator lifecycle), Observer (displays), enums.

```java
// java — illustrative skeleton
enum Direction { UP, DOWN, IDLE }

class Elevator {
    private final int id;
    private int currentFloor = 0;
    private Direction direction = Direction.IDLE;
    // sorted set of pending stops -> naturally serve floors in order
    private final TreeSet<Integer> stops = new TreeSet<>();

    Elevator(int id){ this.id = id; }

    void addStop(int floor){ stops.add(floor); }

    // move one step toward the next stop in the current direction
    void step(){
        if (stops.isEmpty()){ direction = Direction.IDLE; return; }
        Integer next = (direction == Direction.DOWN)
                ? stops.floor(currentFloor)   // nearest stop <= current
                : stops.ceiling(currentFloor);// nearest stop >= current
        if (next == null){ next = stops.first(); }
        if      (next > currentFloor){ currentFloor++; direction = Direction.UP; }
        else if (next < currentFloor){ currentFloor--; direction = Direction.DOWN; }
        else { stops.remove(currentFloor); /* open doors */ }
    }

    int distanceTo(int floor){ return Math.abs(currentFloor - floor); }
    int getCurrentFloor(){ return currentFloor; }
    Direction getDirection(){ return direction; }
}

interface Scheduler { Elevator pick(List<Elevator> elevators, int floor, Direction dir); }

// Nearest-car scheduling: choose the closest idle/compatible elevator.
class NearestCarScheduler implements Scheduler {
    public Elevator pick(List<Elevator> elevators, int floor, Direction dir){
        Elevator best = null; int bestDist = Integer.MAX_VALUE;
        for (Elevator e : elevators){
            int d = e.distanceTo(floor);
            if (d < bestDist){ bestDist = d; best = e; }
        }
        return best;
    }
}

class ElevatorSystem {
    private final List<Elevator> elevators;
    private final Scheduler scheduler;
    ElevatorSystem(List<Elevator> elevators, Scheduler scheduler){
        this.elevators = elevators; this.scheduler = scheduler;
    }
    void requestExternal(int floor, Direction dir){
        Elevator e = scheduler.pick(elevators, floor, dir);
        if (e != null) e.addStop(floor);
    }
}
```

**Talking points**: swap `NearestCarScheduler` for `LookScheduler`/`ScanScheduler` without touching `ElevatorSystem` (Strategy). Displays react via Observer.

---

### 2-3) Deck of Cards / Card Game ⭐⭐⭐⭐

**Requirements**
- Standard 52-card deck: 4 suits × 13 ranks.
- Shuffle, deal N cards, track remaining.
- Reusable base for games (Blackjack, Poker) — game rules layered on top.

**Core classes & relationships**
```text
Card    { Suit, Rank }   (immutable value object)
Deck    ──has-a──▶ List<Card>          (composition, 52 cards)
Hand    ──has-a──▶ List<Card>          (a player's cards)
Player  ──has-a──▶ Hand
Game (abstract) ◁── BlackjackGame, PokerGame   (Template Method for game flow)
Suit, Rank  (enums)
```

**Patterns used**: enums for fixed domains, composition (`Deck` owns `Card`s), optional Template Method for game flow, Factory for building a standard deck.

```java
// java
enum Suit { HEARTS, DIAMONDS, CLUBS, SPADES }
enum Rank {
    TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8),
    NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10), ACE(11);
    final int value;
    Rank(int value){ this.value = value; }
}

// immutable value object — good OOD habit for domain values
final class Card {
    final Suit suit; final Rank rank;
    Card(Suit suit, Rank rank){ this.suit = suit; this.rank = rank; }
    int value(){ return rank.value; }
    @Override public String toString(){ return rank + " of " + suit; }
}

class Deck {
    private final List<Card> cards = new ArrayList<>();
    private int dealt = 0;

    Deck(){                                          // Factory: build standard 52
        for (Suit s : Suit.values())
            for (Rank r : Rank.values())
                cards.add(new Card(s, r));
    }

    // Fisher-Yates shuffle. time = O(n)
    void shuffle(){
        Random rng = new Random();
        for (int i = cards.size() - 1; i > 0; i--){
            int j = rng.nextInt(i + 1);
            Collections.swap(cards, i, j);
        }
        dealt = 0;
    }

    Card dealCard(){                                 // time = O(1)
        if (dealt >= cards.size()) throw new IllegalStateException("deck empty");
        return cards.get(dealt++);
    }
    int remaining(){ return cards.size() - dealt; }
}

class Hand {
    private final List<Card> cards = new ArrayList<>();
    void add(Card c){ cards.add(c); }
    int score(){ return cards.stream().mapToInt(Card::value).sum(); }  // Blackjack-style
}
```

```python
# python — same model, more compact
from enum import Enum
import random

class Suit(Enum):
    HEARTS = "H"; DIAMONDS = "D"; CLUBS = "C"; SPADES = "S"

class Deck:
    def __init__(self):
        ranks = list(range(2, 15))                    # 11=J,12=Q,13=K,14=A
        self.cards = [(s, r) for s in Suit for r in ranks]
        self.dealt = 0
    def shuffle(self):
        random.shuffle(self.cards); self.dealt = 0
    def deal(self):
        if self.dealt >= len(self.cards): raise IndexError("deck empty")
        card = self.cards[self.dealt]; self.dealt += 1
        return card
```

**Talking points**: `Card` is immutable (thread-safe, safe as map key). New games subclass `Game` and override rule hooks (Template Method) — the deck/hand model is reused untouched.

---

### 2-4) Vending Machine ⭐⭐⭐⭐⭐

**Requirements**
- Holds products in slots, each with a price and stock count.
- Accept coins/notes; track balance.
- Select product → dispense if enough money + in stock → return change.
- Handle: insufficient funds, out of stock, cancel/refund.

**Core classes & relationships**
```text
VendingMachine ──has-a──▶ Inventory, MachineState (current), balance
MachineState (State) ◁── NoMoneyState, HasMoneyState, DispensingState
Inventory      ──has-a──▶ Map<String, Slot>
Slot           { Product, count }
Product        { name, price }
Coin           enum { PENNY, NICKEL, DIME, QUARTER }
```

**Patterns used**: **State** (machine lifecycle — the standout pattern here), composition (Inventory), enums (Coin).

```java
// java — State pattern drives the lifecycle
interface MachineState {
    void insertMoney(VendingMachine m, int cents);
    void selectProduct(VendingMachine m, String code);
    void dispense(VendingMachine m);
}

class Product { final String name; final int priceCents;
    Product(String n, int p){ name = n; priceCents = p; } }

class Slot { Product product; int count;
    Slot(Product p, int c){ product = p; count = c; } }

class VendingMachine {
    final MachineState noMoney    = new NoMoneyState();
    final MachineState hasMoney   = new HasMoneyState();
    private MachineState state = noMoney;

    private final Map<String, Slot> inventory = new HashMap<>();
    private int balanceCents = 0;
    private String selected;

    void setState(MachineState s){ this.state = s; }
    void addBalance(int c){ balanceCents += c; }
    int  getBalance(){ return balanceCents; }
    Slot slot(String code){ return inventory.get(code); }
    void select(String code){ this.selected = code; }
    String getSelected(){ return selected; }

    // delegate to current state -> no giant if/switch
    void insertMoney(int cents){ state.insertMoney(this, cents); }
    void selectProduct(String code){ state.selectProduct(this, code); }
    void dispense(){ state.dispense(this); }

    int refund(){ int r = balanceCents; balanceCents = 0; setState(noMoney); return r; }
}

class NoMoneyState implements MachineState {
    public void insertMoney(VendingMachine m, int cents){
        m.addBalance(cents); m.setState(m.hasMoney);
    }
    public void selectProduct(VendingMachine m, String code){
        System.out.println("insert money first");
    }
    public void dispense(VendingMachine m){ System.out.println("no money"); }
}

class HasMoneyState implements MachineState {
    public void insertMoney(VendingMachine m, int cents){ m.addBalance(cents); }
    public void selectProduct(VendingMachine m, String code){
        Slot slot = m.slot(code);
        if (slot == null || slot.count == 0){ System.out.println("out of stock"); return; }
        if (m.getBalance() < slot.product.priceCents){ System.out.println("insufficient funds"); return; }
        m.select(code); m.dispense();
    }
    public void dispense(VendingMachine m){
        Slot slot = m.slot(m.getSelected());
        slot.count--;
        int change = m.getBalance() - slot.product.priceCents;
        System.out.println("dispensed " + slot.product.name + ", change=" + change);
        m.setState(m.noMoney);
    }
}
```

**Talking points**: adding a new phase (e.g. `MaintenanceState`) means a new class implementing `MachineState` — no edits to existing states. Contrast with a flag-based `if (state == ...)` approach that grows unmaintainable.

---

## 3) Common Pitfalls

- **God class**: one class doing everything. Split by responsibility (SRP).
- **Over-inheritance**: deep class trees / inheriting just to reuse code. Prefer composition.
- **Leaking `if/switch` on type everywhere**: sign you need polymorphism, Strategy, or State.
- **Anemic model**: classes with only getters/setters and no behavior. Put behavior with its data.
- **Mutable value objects**: make domain values (`Card`, `Money`) immutable when possible.
- **Ignoring edge cases**: full capacity, empty inventory, invalid ticket, concurrent access.
- **Premature patterns**: don't force a pattern where a plain class is clearer. Name the pattern only when it earns its keep.
- **Skipping requirement clarification**: jumping to classes before scoping loses easy points.
- **Not stating cardinality**: "a lot has levels" — one? many? Be explicit.

---

## 4) OOD Interview Checklist

```text
[ ] Clarified functional + non-functional requirements, stated assumptions
[ ] Listed 3-5 concrete use cases
[ ] Extracted nouns -> core classes; verbs -> methods
[ ] Marked each relationship: is-a vs has-a, and cardinality
[ ] Each class has a single, clear responsibility (SRP)
[ ] Programmed to interfaces / abstractions (DIP)
[ ] Used enums for fixed sets (types, states, directions)
[ ] Chose patterns deliberately (Strategy/Factory/State/Observer...) and justified them
[ ] Design is Open/Closed: new feature -> new class, not edits to old ones
[ ] Handled edge cases (full/empty, invalid input, concurrency if asked)
[ ] Sketched a class diagram + key method signatures
[ ] Called out extension points ("to add X later, only Y changes")
```

---

## 5) Quick Decision Table

| Prompt keyword | Likely core objects | Likely patterns |
|----------------|---------------------|-----------------|
| Parking lot | Lot, Level, Spot, Vehicle, Ticket | Singleton, Strategy, Factory |
| Elevator | System, Elevator, Scheduler, Request | Strategy, State, Observer |
| Deck / card game | Card, Deck, Hand, Player, Game | Enum, Composition, Template Method |
| Vending machine | Machine, Slot, Product, State, Coin | **State**, Composition |
| Library management | Library, Book, Member, Loan, Catalog | Strategy (fines), Observer (holds) |
| Chess / board game | Board, Piece, Move, Player | Strategy (per-piece moves), Factory |
| ATM | ATM, Account, Card, Transaction, State | State, Chain of Responsibility (auth) |

> For **data-structure-heavy** design prompts (LRU/LFU cache, iterators, Trie search, rate limiters, consistent hashing) see [`design.md`](design.md). For **OOP fundamentals** (encapsulation, polymorphism, SOLID deep-dive, interface vs abstract class) see [`../faq/java/faq_OOP.md`](../faq/java/faq_OOP.md).

---

## 6) LC Design Problems Through an OOD Lens ⭐⭐⭐⭐⭐

A LeetCode `Design X` problem is an **OOD interview with the class diagram already given to you**: LC hands you the class name and the public method signatures, then grades only the part an OOD interviewer would grade last — the internals. The three decisions you still own are exactly the OOD ones:

1. **State** — which fields (and which helper classes) are the minimum that can answer every method?
2. **Structure** — *one* operation is always tighter than the rest; that operation, not the "main" one, picks the data structure.
3. **Invariant** — the one-line rule that is true between calls, which every method must restore before it returns.

> This section is the **bridge only**. Full data-structure implementations (LRU/LFU, Trie internals, heaps, segment trees) live in [`design.md`](design.md); iterator-shaped designs (LC 173 / 284 / 341 / 900) live in [`iterator.md`](iterator.md); stream-shaped ones in [`streaming_algorithms.md`](streaming_algorithms.md).

### 6-1) Mapping table — what each LC design problem actually tests ⭐⭐⭐⭐⭐

| LC | What you declare (state / helper classes) | Operation that dictates the structure | Invariant every method restores |
|----|-------------------------------------------|---------------------------------------|---------------------------------|
| **380** Insert Delete GetRandom O(1) | `List<Integer> vals` + `Map<Integer,Integer> pos` | `getRandom()` O(1) → needs array backing; `remove()` O(1) → needs the index map | `pos[v]` is v's real index in `vals`, and `vals` has no holes |
| **381** Insert Delete GetRandom - Duplicates allowed | same, but `Map<Integer, Set<Integer>>` | duplicates break the 1-to-1 index map | each value maps to the exact set of its positions |
| **297** Serialize and Deserialize Binary Tree | a `Codec` class; the wire **format** is the design decision | `deserialize` must invert `serialize` | `deserialize(serialize(t))` ≡ `t` — null markers preserve shape |
| **449** Serialize and Deserialize BST | same `Codec`, but BST ordering lets you drop null markers | the BST property is extra information you may exploit | encoded stream is a valid preorder of a BST |
| **295** Find Median from Data Stream | two heaps: max-heap `low`, min-heap `high` | `findMedian()` O(1) → the median must sit at a heap top | the two sizes differ by at most 1 **and** `max(low) ≤ min(high)` |
| **211** Design Add and Search Words | `TrieNode { children, isWord }` | `'.'` wildcard → search is DFS/recursion, not a loop | root→node path spells the prefix; `isWord` marks exactly inserted words |
| **208** Implement Trie (Prefix Tree) | `TrieNode` helper class (a genuine has-a tree) | `startsWith` → prefix must be walkable char by char | same as above |
| **146** LRU Cache | `Map<K,Node>` + `Node` doubly-linked list class | `get`/`put` both O(1) → eviction target must be reachable in O(1) | list order == recency order; map keys == the live nodes exactly |
| **432** All O`one Data Structure | `Bucket` doubly-linked list of equal-count keys + `Map<key,Bucket>` | `getMaxKey`/`getMinKey` O(1) → counts must be kept in sorted buckets | bucket counts strictly increase along the list; each key sits in the bucket of its count |
| **706** Design HashMap / **705** Design HashSet | `Node` chain per bucket + `Node[] buckets` | collision handling is the whole problem | at most one node per key, in the bucket `hash(key)` |
| **981** Time Based Key-Value Store | `Map<String, List<Pair<time,value>>>` | `get(key, t)` = "largest time ≤ t" → binary search | per-key list is append-only and sorted by timestamp |
| **355** Design Twitter | entity classes: `User { id, followees, tweets }`, `Tweet { id, time }` + global clock | `getNewsFeed` = top-10 by recency across followees → k-way merge | timestamps strictly increase; a user follows themselves so the feed rule stays uniform |
| **155** Min Stack | one stack of `(val, minSoFar)` pairs (or a second min-stack) | `getMin()` O(1) → the min must be carried, not recomputed | top pair's `minSoFar` == min of every element currently in the stack |
| **895** Maximum Frequency Stack | `Map<val,freq>` + `Map<freq, Stack<val>>` + `maxFreq` | `pop()` breaks freq ties by **recency** → needs a stack per freq | `group[f]` holds, in push order, every value that has reached frequency `f` |
| **729** My Calendar I | `TreeMap<start,end>` of booked intervals | `book()` needs the neighbours of a start → ordered map, not a list | no two stored intervals overlap |
| **1146** Snapshot Array | per-index `List<(snapId, value)>` + a snap counter | `get(i, snapId)` → binary search on that index's history | each index records only its *changes*, sorted by snapId |
| **703** Kth Largest Element in a Stream | min-heap capped at size `k` | `add()` must return the kth largest immediately | the heap holds exactly the k largest seen; its root is the answer |
| **384** Shuffle an Array | the pristine `original` array **plus** a working copy | `reset()` must be exact → the original may never be mutated | `original` is never written after construction; `shuffle` is Fisher-Yates (uniform) |
| **622** Design Circular Queue / **641** Design Circular Deque | fixed `int[]` + `head` + `size` | O(1) at **both** ends under a fixed capacity → index arithmetic mod capacity | `0 ≤ size ≤ capacity`; element `i` lives at `(head + i) % capacity` |
| **232** Implement Queue using Stacks / **225** Implement Stack using Queues | two stacks: `in`, `out` | amortized O(1) `pop` → move elements only when forced | `out` holds the oldest elements in pop order; refill `out` **only** when it is empty |
| **1603** Design Parking System | three counters (see §6-3) | `addCar` only asks "is one left?" → identity is not needed | `remaining[t] == capacity[t] - parked(t)`, never negative |

**How to read this table in an interview**: the third column is the sentence you should say out loud *before* writing any field ("`getRandom` has to be O(1), so the storage has to be an array — everything else follows"). The fourth column is the sentence you write as a comment above your fields; it is what turns a pile of maps into a design.

### 6-2) The 4-step LC-design procedure ⭐⭐⭐⭐⭐

The §0-1 five-step OOD approach, compressed for a problem where the API is already fixed:

```text
STEP 1: Read the API, not the story
   - List every public method + its required complexity (LC states it, or the
     constraints imply it: 1e5 calls => O(1)/O(log n) per call).
   - Note which methods are QUERIES (read) and which are COMMANDS (write).

STEP 2: Find the tightest operation -> it picks the structure
   - Rank the methods by how hard they are at the required complexity.
   - Design for the HARDEST one; the easy ones will fall out.
   - "O(1) random access"   -> array          (LC 380, 384)
   - "O(1) min/max/median"  -> carried value / heap / bucket list (155, 295, 432)
   - "largest key <= x"     -> sorted list + binary search / TreeMap (981, 729, 1146)
   - "prefix / wildcard"    -> Trie of nodes  (208, 211)
   - "O(1) evict oldest"    -> linked list + map (146)

STEP 3: Write the invariant as a comment ABOVE the fields
   - One sentence, true between every pair of calls.
   - If you cannot state it, your state is wrong (usually redundant or missing).

STEP 4: Implement each method as "restore the invariant"
   - Every command ends by re-establishing it; every query may assume it.
   - Constructor establishes it on empty state.
   - Then and only then: edge cases (empty, full, duplicate, unknown key).
```

**Only introduce a helper class when it carries identity or behavior.** `Node`, `TrieNode`, `Bucket`, `Tweet` earn their place (they have state that outlives a single call). A "class" that is just a tuple returned once is noise — LC design answers are graded on the state model, not the class count.

### 6-3) Worked bridge — LC 1603 Design Parking System ⭐⭐⭐⭐

The single cleanest illustration of "requirements decide how much OOD you need": it is §2-1's parking lot with every requirement that justified objects removed.

```java
// java
// LC 1603 - Design Parking System
// IDEA: the only query is addCar -> "is a spot of this type left?".
//       No car is ever identified and none ever leaves, so the state collapses
//       to ONE counter per spot type. No Spot / Vehicle / Ticket class needed.
class ParkingSystem {
    // time = O(1) per addCar, space = O(1)
    private final int[] remaining = new int[4];      // index 1=big, 2=medium, 3=small

    public ParkingSystem(int big, int medium, int small) {
        remaining[1] = big; remaining[2] = medium; remaining[3] = small;
    }

    // INVARIANT: remaining[t] == capacity[t] - parked(t), and never negative
    public boolean addCar(int carType) {
        if (remaining[carType] == 0) return false;   // full for this type
        remaining[carType]--;
        return true;
    }
}
```

```python
# python
# LC 1603 - Design Parking System
# IDEA: one counter per spot type; identity of a car is never asked for.
class ParkingSystem:
    # time = O(1) per addCar, space = O(1)
    def __init__(self, big: int, medium: int, small: int):
        self.remaining = {1: big, 2: medium, 3: small}

    # INVARIANT: remaining[t] == capacity[t] - parked(t), never negative
    def addCar(self, carType: int) -> bool:
        if self.remaining[carType] == 0:
            return False
        self.remaining[carType] -= 1
        return True
```

**Now add ONE requirement — "cars leave, and a small car may take a bigger spot"** — and the counter stops being able to hold the invariant (you cannot free "a spot" you never identified, and the fit rule is now a policy). That is the exact moment the §2-1 object model earns its keep:

```java
// java — the same prompt, one requirement heavier
// IDEA: leave() forces spot IDENTITY (free/occupied pools); a variable fit rule
//       forces a POLICY OBJECT (Strategy) instead of an if/else in park().
enum SpotType { BIG, MEDIUM, SMALL }

interface SpotPolicy { List<SpotType> fitsFor(SpotType wanted); }   // Strategy

class ExactFitPolicy implements SpotPolicy {
    public List<SpotType> fitsFor(SpotType wanted) { return List.of(wanted); }
}

class UpgradePolicy implements SpotPolicy {          // a car may take a bigger spot
    public List<SpotType> fitsFor(SpotType wanted) {
        switch (wanted) {
            case SMALL:  return List.of(SpotType.SMALL, SpotType.MEDIUM, SpotType.BIG);
            case MEDIUM: return List.of(SpotType.MEDIUM, SpotType.BIG);
            default:     return List.of(SpotType.BIG);
        }
    }
}

class ParkingLotV2 {
    // time = O(#types) park / O(1) leave, space = O(total spots)
    private final Map<SpotType, Deque<Integer>> free = new EnumMap<>(SpotType.class);
    private final Map<Integer, SpotType> occupied = new HashMap<>();   // spotId -> type
    private final SpotPolicy policy;                                   // injected (DIP)
    private int nextId = 0;

    ParkingLotV2(Map<SpotType, Integer> capacity, SpotPolicy policy) {
        this.policy = policy;
        for (SpotType t : SpotType.values()) {
            Deque<Integer> ids = new ArrayDeque<>();
            for (int i = 0; i < capacity.getOrDefault(t, 0); i++) ids.push(nextId++);
            free.put(t, ids);
        }
    }

    // INVARIANT: every spot id is in exactly one of `free` / `occupied`
    Integer park(SpotType wanted) {
        for (SpotType t : policy.fitsFor(wanted)) {
            Deque<Integer> pool = free.get(t);
            if (!pool.isEmpty()) { int id = pool.pop(); occupied.put(id, t); return id; }
        }
        return null;                                  // no acceptable spot
    }

    boolean leave(int spotId) {
        SpotType t = occupied.remove(spotId);
        if (t == null) return false;                  // unknown / already free
        free.get(t).push(spotId);
        return true;
    }
}
```

```python
# python — same model; the Strategy is just a function
# IDEA: leave() needs spot identity -> free/occupied pools; fit rule -> injected policy.
from enum import Enum

class SpotType(Enum):
    BIG = 1; MEDIUM = 2; SMALL = 3

def exact_fit(wanted):                       # Strategy A
    return [wanted]

def upgrade_fit(wanted):                     # Strategy B: may take a bigger spot
    order = [SpotType.SMALL, SpotType.MEDIUM, SpotType.BIG]
    return order[order.index(wanted):]

class ParkingLotV2:
    # time = O(#types) park / O(1) leave, space = O(total spots)
    def __init__(self, capacity, policy=exact_fit):
        self.policy = policy
        self.free = {t: [] for t in SpotType}
        self.occupied = {}                   # spot_id -> SpotType
        next_id = 0
        for t in SpotType:
            for _ in range(capacity.get(t, 0)):
                self.free[t].append(next_id)
                next_id += 1

    # INVARIANT: every spot id is in exactly one of free / occupied
    def park(self, wanted):
        for t in self.policy(wanted):
            if self.free[t]:
                spot_id = self.free[t].pop()
                self.occupied[spot_id] = t
                return spot_id
        return None

    def leave(self, spot_id):
        t = self.occupied.pop(spot_id, None)
        if t is None:
            return False
        self.free[t].append(spot_id)
        return True
```

**Talking point that scores**: "LC 1603 needs no objects because nothing has identity and nothing has a lifecycle. Add `leave()` and identity appears; add a fit rule and a Strategy appears; add billing and §2-1's `Ticket` + `PricingStrategy` appear." Naming the *requirement* that creates each class is the whole skill.

### 6-4) Entity modelling inside an LC problem — LC 355 Design Twitter ⭐⭐⭐⭐

The one common LC design problem where real **entity modelling** (not just a data structure) is the expected answer:

```text
Twitter  ──has-a──▶ Map<Integer, User>   (registry of users)
Twitter  ──has-a──▶ int clock            (global monotonic timestamp)
User     ──has-a──▶ Set<Integer> followees, List<Tweet> tweets
Tweet    { int id, int time }            (immutable value object)

getNewsFeed(u) = top-10 by time over { tweets of u } ∪ { tweets of each followee }
```

- **Why `Tweet` is a class**: recency ordering needs a timestamp, so a bare tweet id cannot carry the state — the value object is forced by the query.
- **Why the global clock lives on `Twitter`, not on `User`**: the invariant "timestamps are comparable across users" cannot be maintained by any single user.
- **The self-follow trick**: `follow(u, u)` at registration makes `getNewsFeed` a single uniform merge over followees instead of a special case — an invariant chosen to delete a branch.
- **Which operation dictates the structure**: `getNewsFeed` (top-10 across k sorted lists) → per-user tweet lists kept newest-last plus a k-way merge (heap). `postTweet`/`follow`/`unfollow` are O(1) either way.

> The k-way merge implementation itself is a data-structure exercise — see [`design.md`](design.md) and [`heap.md`](heap.md). What an OOD interviewer wants here is the class graph above plus the two invariants.

### 6-5) Reference-only LC design problems worth a look

Same skill, no new template — good drilling material once §6-2 feels automatic:

- LC 1797 Design Authentication Manager — `Map<token, expiryTime>`; invariant: a token is valid iff its stored expiry > now (expire lazily, never sweep).
- LC 2013 Detect Squares — `Map<point, count>`; the `count()` query (pick the diagonal, derive the other two corners) dictates the point-multiset state.
- LC 2034 Stock Price Fluctuation — `Map<timestamp, price>` + ordered multiset of prices; invariant: the multiset holds exactly the *current* price of every timestamp.
- LC 1352 Product of the Last K Numbers — prefix-product list; invariant: reset the list on a `0` so every stored prefix is non-zero.
- LC 707 Design Linked List / LC 1206 Design Skiplist — pure node-class modelling drills.
- LC 715 Range Module / LC 731 My Calendar II / LC 732 My Calendar III — the §6-1 LC 729 invariant ("no two stored intervals overlap") relaxed step by step.
