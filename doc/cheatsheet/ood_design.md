# Object-Oriented Design (OOD / Low-Level Design)

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

```
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

```
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

#### **Strategy** — swap an algorithm at runtime
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
```
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

---

### 2-2) Elevator System ⭐⭐⭐⭐

**Requirements**
- Multiple elevators, N floors.
- Handle external requests (up/down at a floor) and internal requests (go to floor X).
- Scheduler decides which elevator serves a request.
- Each elevator has direction + state (IDLE, MOVING, DOOR_OPEN).

**Core classes & relationships**
```
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
```
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
```
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

```
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
