# 物件導向設計（OOD／Low-Level Design）

> **範圍** — 低階設計那一關 — 類別建模、SOLID，以及面試官真的會問的設計模式（strategy、observer、factory、state），附完整的設計範例。
> **另見**：[design.md](./design.md) — 那些以操作複雜度而非類別結構評分的 LC 設計題；[concurrency_patterns.md](./concurrency_patterns.md) — 這些設計裡的執行緒安全問題。

## LeetCode 題目清單

- [Design](https://leetcode.com/problem-list/design/)

## 總覽

**OOD（物件導向設計）**，又叫 **LLD（低階設計）**，考的是你能不能把一個真實世界的問題翻譯成一組乾淨的**類別、介面與關係**。評分的重點不在於你寫出一個能從頭跑到尾的程式，而在於**建模、責任切分、可擴充性，以及設計模式的運用**。

### 關鍵性質
- **考什麼**：類別建模、封裝、SOLID 原則、設計模式的使用，以及你的設計吸收新需求時有多優雅。
- **核心想法**：把需求（名詞 → 物件，動詞 → 方法）變成一張可維護的類別關係圖。
- **什麼時候用**：像「設計一個停車場／電梯／販賣機／紙牌遊戲」這類面試題，或任何「幫 X 設計類別」的問題。

### OOD（低階）vs 系統設計（高階）

| 面向 | 系統設計（HLD） | OOD／LLD |
|--------|---------------------|-----------|
| 視角高度 | 服務、資料庫、佇列、快取、網路 | 類別、介面、方法、欄位 |
| 關注點 | 可擴展性、可用性、延遲、分片 | 責任歸屬、耦合、內聚、模式 |
| 產出 | 架構圖、API 合約、資料流 | 類別圖 + 關鍵方法簽名 + 程式碼 |
| 規模 | 數百萬使用者、分散式節點 | 單一行程、記憶體中的物件 |
| 典型題目 | 「設計一個短網址服務」 | 「設計停車場的類別」 |

> 交叉參考：**資料結構設計**（LRU、LFU、All-O(1)、Trie、iterator）與**系統層級的程式碼模式**（一致性雜湊、限流器、負載平衡器）請看 [`design.md`](design.md)。這份文件專注在 **OOD 面試的類別建模**，不重複那邊的 LRU/LFU 內容。

### 參考資料
- SOLID 原則（Robert C. Martin）
- Gang of Four（GoF）設計模式
- [`design.md`](design.md) — 資料結構與系統程式碼模式
- [`../faq/java/faq_OOP.md`](../faq/java/faq_OOP.md) — OOP 基礎 FAQ

---

## 0) 概念

### 0-1) 可重複套用的五步法 ⭐⭐⭐⭐⭐

**任何 OOD 題目**都套這五個步驟。面試時每一步都要講出來。

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

### 0-2) 名詞 → 類別，動詞 → 方法（心智模型）

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

## 1) 通用形式

### 1-1) SOLID 原則 ⭐⭐⭐⭐⭐

OOD 面試中被引用最多的框架。這些一句話的定義要背起來。

| 原則 | 意義 | 它修掉的壞味道 |
|-----------|---------|---------------------|
| **S** — 單一職責 | 一個類別只該有一個改動的理由 | 什麼都做的上帝類別：解析 + 驗證 + 資料庫 + 列印 |
| **O** — 開放封閉 | 對擴充開放，對修改封閉 | 每加一個新型別就得去改那個巨大的 `if/switch` |
| **L** — 里氏替換 | 子型別必須能用在任何用得到其基底型別的地方 | `Square extends Rectangle` 把 `setWidth` 的行為搞壞 |
| **I** — 介面隔離 | 多個小介面 > 一個肥介面 | 逼一個類別去實作那些它只能丟 `UnsupportedOperation` 的方法 |
| **D** — 依賴反轉 | 依賴抽象，不要依賴具體 | 高階模組被寫死綁在具體的 `MySQLDatabase` 上 |

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

### 1-2) 關係速查表

| 關係 | UML | 意義 | Java 寫法 |
|--------------|-----|---------|-----------------|
| **is-a** | ▷（空心箭頭） | 繼承／子型別 | `class Car extends Vehicle` / `implements Drivable` |
| **has-a（組合）** | ◆（實心菱形） | 部分不能活得比整體久 | `Engine` 由 `Car` 建立並擁有 |
| **has-a（聚合）** | ◇（空心菱形） | 部分可以獨立存在 | `Team` 持有 `List<Player>`，但球員活得比球隊久 |
| **uses-a（依賴）** | ┄>（虛線） | 短暫使用（參數／區域變數） | 方法把 `Logger` 當參數收進來 |

**經驗法則：組合優先於繼承。** 繼承很僵硬（只能有一個父類、耦合很緊）；組合讓你能在執行期抽換行為，也讓類別保持小。

---

## 1-3) OOD 面試的關鍵設計模式 ⭐⭐⭐⭐

要知道**什麼時候**該拿出哪一個，而且要能把骨架畫出來。

### **Strategy** — 在執行期抽換演算法
**時機**：有多種可互換的行為（計價規則、排序、路由、付款方式）。用來取代一堆分支的 `if/switch`。

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

#### **Factory** — 把物件建立集中起來
**時機**：建立邏輯很複雜，或型別要到執行期才決定。呼叫端去問工廠，而不是自己 `new` 出具體類別（支援開放封閉）。

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

#### **Singleton** — 只有一個實例
**時機**：共用的協調者／設定／登錄表（一個停車場、一個 logger）。要準備好談執行緒安全。

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

#### **Observer** — 狀態改變時的發布／訂閱
**時機**：很多物件必須對某個物件的變化做出反應（電梯顯示更新、事件通知、UI 監聽器）。

```java
// java
interface Observer { void update(String event); }

class Subject {
    private final List<Observer> observers = new ArrayList<>();
    void subscribe(Observer o){ observers.add(o); }
    void notifyAll(String event){ for (Observer o : observers) o.update(event); }
}
```

#### **State** — 行為隨內部狀態而變
**時機**：物件有生命週期，**同一個**方法在不同狀態下行為不同（販賣機：NoCoin → HasCoin → Dispensing；電梯：Idle → Moving → DoorOpen）。用來取代到處蔓延的狀態旗標。

```java
// java
interface MachineState { void insertCoin(VendingMachine m); void dispense(VendingMachine m); }

class NoCoinState implements MachineState {
    public void insertCoin(VendingMachine m){ m.setState(m.hasCoin); }   // transition
    public void dispense(VendingMachine m){ System.out.println("insert coin first"); }
}
```

#### **Decorator** — 不靠繼承來加行為
**時機**：可選、可疊加的功能（咖啡 + 牛奶 + 糖；一個附電動車充電的車位）。避免組合爆炸出一堆類別。

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

#### **Adapter** — 讓不相容的介面能一起運作
**時機**：要整合第三方／舊有的類別，但它的介面跟你的程式碼期待的對不上。

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

**模式選擇速查表：**

| 題目裡的徵兆 | 該拿出 |
|-----------------------|-----------|
| 「它要支援多種計價／付款／排名規則」 | **Strategy** |
| 「依輸入建立不同種類的 X」 | **Factory** |
| 「只有一個共用的控制器／登錄表」 | **Singleton** |
| 「X 改變時，通知所有的 Y」 | **Observer** |
| 「物件在生命週期的每個階段行為都不同」 | **State** |
| 「加上可以互相組合的選配功能」 | **Decorator** |
| 「橋接既有／舊有／第三方介面」 | **Adapter** |

---

## 2) 經典 OOD 題目（完整設計）

### 2-1) 停車場 ⭐⭐⭐⭐⭐

**需求**
- 多層樓；每層有不同型別的車位（COMPACT、LARGE、MOTORCYCLE、EV）。
- 車輛（Car、Bike、Truck）只能停某些型別的車位。
- 停車 → 發一張**票**；取車 → 算費用。
- 回報每層樓的可用車位。

**核心類別與關係**
```text
ParkingLot (Singleton)  ──has-a──▶ List<Level>
Level                   ──has-a──▶ List<ParkingSpot>
ParkingSpot             ──holds──▶ Vehicle (0..1)
Vehicle (abstract)      ◁── Car, Bike, Truck            (is-a)
Ticket                  ──refs──▶ Vehicle, ParkingSpot, entryTime
PricingStrategy         (Strategy)  used by ParkingLot to compute fee
VehicleType, SpotType   (enums)
```

**用到的模式**：Singleton（`ParkingLot`）、Strategy（計價）、Factory（建立車輛／車位）、用 enum 表示固定集合。

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

**可擴充性的討論點**：新的車種 → 加一個 `Vehicle` 子類別（不用動 `Level`／`ParkingLot`）；新的計價方式 → 新的 `PricingStrategy`（開放封閉）。

> **對應的 LC 題**：LC 1603 *Design Parking System* 就是這題被剝到最精簡而誠實的版本（只剩計數器，沒有 `Spot`／`Ticket` 物件）。LC 的解答，以及是哪一條需求逼你回到上面那個完整模型，見 [§6-3](#6-3-worked-bridge--lc-1603-design-parking-system-)。

---

### 2-2) 電梯系統 ⭐⭐⭐⭐

**需求**
- 多部電梯、N 層樓。
- 處理外部請求（在某層按上／下）與內部請求（去第 X 層）。
- 由排程器決定哪部電梯去處理某個請求。
- 每部電梯有方向 + 狀態（IDLE、MOVING、DOOR_OPEN）。

**核心類別與關係**
```text
ElevatorSystem  ──has-a──▶ List<Elevator>, Scheduler
Scheduler       (Strategy)  picks best elevator for a Request
Elevator        ──has-a──▶ Direction, ElevatorState, TreeSet<Integer> stops
Request         { floor, Direction }     (external)  or { targetFloor } (internal)
Direction       enum { UP, DOWN, IDLE }
ElevatorState   (State)  IDLE / MOVING / DOOR_OPEN
Observer        floor displays subscribe to elevator position changes
```

**用到的模式**：Strategy（排程演算法）、State（電梯生命週期）、Observer（顯示器）、enum。

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

**討論點**：不用動 `ElevatorSystem` 就能把 `NearestCarScheduler` 換成 `LookScheduler`／`ScanScheduler`（Strategy）。顯示器透過 Observer 做出反應。

---

### 2-3) 一副撲克牌／紙牌遊戲 ⭐⭐⭐⭐

**需求**
- 標準 52 張牌：4 種花色 × 13 個點數。
- 洗牌、發 N 張牌、追蹤剩餘張數。
- 可重複利用的基底，給各種遊戲用（Blackjack、Poker）— 遊戲規則疊在上面。

**核心類別與關係**
```text
Card    { Suit, Rank }   (immutable value object)
Deck    ──has-a──▶ List<Card>          (composition, 52 cards)
Hand    ──has-a──▶ List<Card>          (a player's cards)
Player  ──has-a──▶ Hand
Game (abstract) ◁── BlackjackGame, PokerGame   (Template Method for game flow)
Suit, Rank  (enums)
```

**用到的模式**：用 enum 表示固定的值域、組合（`Deck` 擁有一堆 `Card`）、遊戲流程可選用 Template Method、用 Factory 組出一副標準牌。

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

**討論點**：`Card` 是不可變的（執行緒安全，可以安全地當 map 的 key）。新遊戲繼承 `Game` 並覆寫規則掛勾（Template Method）— 牌堆／手牌的模型原封不動地被重複使用。

---

### 2-4) 販賣機 ⭐⭐⭐⭐⭐

**需求**
- 商品放在一格格的貨道裡，每格有價格與庫存數量。
- 接受硬幣／紙鈔；追蹤餘額。
- 選商品 → 錢夠且有庫存就出貨 → 找零。
- 要處理：錢不夠、缺貨、取消／退款。

**核心類別與關係**
```text
VendingMachine ──has-a──▶ Inventory, MachineState (current), balance
MachineState (State) ◁── NoMoneyState, HasMoneyState, DispensingState
Inventory      ──has-a──▶ Map<String, Slot>
Slot           { Product, count }
Product        { name, price }
Coin           enum { PENNY, NICKEL, DIME, QUARTER }
```

**用到的模式**：**State**（機器的生命週期 — 這題最突出的模式）、組合（Inventory）、enum（Coin）。

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

**討論點**：加一個新階段（例如 `MaintenanceState`）就是新增一個實作 `MachineState` 的類別 — 完全不用動既有的狀態。對比之下，用旗標寫成 `if (state == ...)` 的做法會長到無法維護。

---

## 3) 常見陷阱

- **上帝類別**：一個類別包山包海。依責任切開（SRP）。
- **繼承過頭**：類別樹太深／只為了重用程式碼而繼承。優先用組合。
- **到處對型別做 `if/switch`**：這代表你需要多型、Strategy 或 State。
- **貧血模型**：類別只有 getter/setter，沒有行為。行為要跟它的資料放在一起。
- **可變的值物件**：領域值（`Card`、`Money`）能做成不可變就做成不可變。
- **忽略邊界情況**：滿場、庫存空、無效票券、並行存取。
- **過早套模式**：不要在單純類別更清楚的地方硬塞模式。只有在模式真的划算時才把它的名字講出來。
- **跳過需求釐清**：還沒界定範圍就開始寫類別，白白丟掉好拿的分數。
- **沒有講清楚基數**：「一個停車場有樓層」— 一層？多層？要講明白。

---

## 4) OOD 面試檢查清單

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

## 5) 快速決策表

| 題目關鍵字 | 可能的核心物件 | 可能的模式 |
|----------------|---------------------|-----------------|
| 停車場 | Lot、Level、Spot、Vehicle、Ticket | Singleton、Strategy、Factory |
| 電梯 | System、Elevator、Scheduler、Request | Strategy、State、Observer |
| 撲克牌／紙牌遊戲 | Card、Deck、Hand、Player、Game | Enum、組合、Template Method |
| 販賣機 | Machine、Slot、Product、State、Coin | **State**、組合 |
| 圖書館管理 | Library、Book、Member、Loan、Catalog | Strategy（罰款）、Observer（預約） |
| 西洋棋／棋盤遊戲 | Board、Piece、Move、Player | Strategy（各棋子的走法）、Factory |
| ATM | ATM、Account、Card、Transaction、State | State、Chain of Responsibility（驗證） |

> 偏重**資料結構**的設計題（LRU/LFU 快取、iterator、Trie 搜尋、限流器、一致性雜湊）請看 [`design.md`](design.md)。**OOP 基礎**（封裝、多型、SOLID 深入、interface vs abstract class）請看 [`../faq/java/faq_OOP.md`](../faq/java/faq_OOP.md)。

---

## 6) 用 OOD 的眼光看 LC 設計題 ⭐⭐⭐⭐⭐

一道 LeetCode 的 `Design X` 題，其實就是**類別圖已經直接給你的 OOD 面試**：LC 把類別名稱和公開方法簽名都交到你手上，然後只評分 OOD 面試官會最後才評的那部分 — 內部實作。還握在你手上的三個決定，剛好就是 OOD 的那三個：

1. **狀態** — 哪些欄位（以及哪些輔助類別）是能回答每個方法的最小集合？
2. **結構** — 一定有*某一個*操作比其他都吃緊；挑資料結構的是那個操作，不是「主要」的那個。
3. **不變式** — 一句話寫得完、在兩次呼叫之間永遠成立的規則，每個方法回傳前都必須把它恢復。

> 這一節**只是橋梁**。完整的資料結構實作（LRU/LFU、Trie 內部、堆積、線段樹）在 [`design.md`](design.md)；iterator 型的設計（LC 173／284／341／900）在 [`iterator.md`](iterator.md)；串流型的在 [`streaming_algorithms.md`](streaming_algorithms.md)。

### 6-1) 對照表 — 每道 LC 設計題實際上在考什麼 ⭐⭐⭐⭐⭐

| LC | 你要宣告的東西（狀態／輔助類別） | 決定結構的那個操作 | 每個方法都要恢復的不變式 |
|----|-------------------------------------------|---------------------------------------|---------------------------------|
| **380** Insert Delete GetRandom O(1) | `List<Integer> vals` + `Map<Integer,Integer> pos` | `getRandom()` 要 O(1) → 底層得是陣列；`remove()` 要 O(1) → 得有索引表 | `pos[v]` 是 v 在 `vals` 中的真實索引，而且 `vals` 沒有空洞 |
| **381** Insert Delete GetRandom - Duplicates allowed | 同上，但改成 `Map<Integer, Set<Integer>>` | 重複值破壞了一對一的索引表 | 每個值對應到它所有位置構成的那個集合 |
| **297** Serialize and Deserialize Binary Tree | 一個 `Codec` 類別；設計決策在於傳輸**格式** | `deserialize` 必須是 `serialize` 的反函數 | `deserialize(serialize(t))` ≡ `t` — null 標記保住了樹的形狀 |
| **449** Serialize and Deserialize BST | 一樣是 `Codec`，但 BST 的順序讓你可以省掉 null 標記 | BST 性質是你可以拿來用的額外資訊 | 編碼後的串流是某棵 BST 的合法前序 |
| **295** Find Median from Data Stream | 兩個堆積：最大堆 `low`、最小堆 `high` | `findMedian()` 要 O(1) → 中位數必須就坐在某個堆頂 | 兩邊大小最多差 1，**而且** `max(low) ≤ min(high)` |
| **211** Design Add and Search Words | `TrieNode { children, isWord }` | `'.'` 萬用字元 → 搜尋是 DFS／遞迴，不是迴圈 | root→node 這條路徑拼出該前綴；`isWord` 精確標出被插入過的字 |
| **208** Implement Trie (Prefix Tree) | `TrieNode` 輔助類別（貨真價實的 has-a 樹） | `startsWith` → 前綴必須能一個字元一個字元走下去 | 同上 |
| **146** LRU Cache | `Map<K,Node>` + `Node` 雙向鏈結串列類別 | `get`／`put` 都要 O(1) → 要淘汰的對象必須能 O(1) 拿到 | 串列順序 == 使用時間順序；map 的 key 精確對應到現存的節點 |
| **432** All O`one Data Structure | 由相同計數的 key 組成的 `Bucket` 雙向鏈結串列 + `Map<key,Bucket>` | `getMaxKey`／`getMinKey` 要 O(1) → 計數必須維持在排序好的桶裡 | 沿著串列，桶的計數嚴格遞增；每個 key 坐在它自己計數的那個桶裡 |
| **706** Design HashMap / **705** Design HashSet | 每個桶一條 `Node` 鏈 + `Node[] buckets` | 碰撞處理就是這題的全部 | 每個 key 至多一個節點，且在 `hash(key)` 那個桶裡 |
| **981** Time Based Key-Value Store | `Map<String, List<Pair<time,value>>>` | `get(key, t)` = 「不超過 t 的最大時間」→ 二分搜尋 | 每個 key 的串列只會往後追加，且依 timestamp 排序 |
| **355** Design Twitter | 實體類別：`User { id, followees, tweets }`、`Tweet { id, time }` + 全域時鐘 | `getNewsFeed` = 跨所有追蹤對象取最新的前 10 則 → k 路合併 | timestamp 嚴格遞增；使用者也追蹤自己，讓 feed 規則保持一致 |
| **155** Min Stack | 一個放 `(val, minSoFar)` 配對的堆疊（或第二個 min 堆疊） | `getMin()` 要 O(1) → 最小值必須被帶著走，不能現算 | 頂端配對的 `minSoFar` == 堆疊中所有現存元素的最小值 |
| **895** Maximum Frequency Stack | `Map<val,freq>` + `Map<freq, Stack<val>>` + `maxFreq` | `pop()` 在頻率相同時要看**誰比較新** → 每個頻率各一個堆疊 | `group[f]` 依 push 順序放著所有曾經達到頻率 `f` 的值 |
| **729** My Calendar I | 已預訂區間的 `TreeMap<start,end>` | `book()` 需要某個 start 的左右鄰居 → 要有序 map，不是 list | 任兩個已存區間都不重疊 |
| **1146** Snapshot Array | 每個索引一份 `List<(snapId, value)>` + 一個快照計數器 | `get(i, snapId)` → 在該索引的歷史上做二分搜尋 | 每個索引只記錄自己的*變動*，依 snapId 排序 |
| **703** Kth Largest Element in a Stream | 大小上限為 `k` 的最小堆 | `add()` 必須立刻回傳第 k 大 | 堆裡精確地放著看過的最大 k 個；堆頂就是答案 |
| **384** Shuffle an Array | 原封不動的 `original` 陣列**加上**一份工作副本 | `reset()` 必須完全還原 → 原陣列永遠不能被改 | `original` 建構後不再被寫入；`shuffle` 用 Fisher-Yates（均勻） |
| **622** Design Circular Queue / **641** Design Circular Deque | 固定的 `int[]` + `head` + `size` | 容量固定下**兩端**都要 O(1) → 索引對容量取模的算術 | `0 ≤ size ≤ capacity`；元素 `i` 住在 `(head + i) % capacity` |
| **232** Implement Queue using Stacks / **225** Implement Stack using Queues | 兩個堆疊：`in`、`out` | 攤還 O(1) 的 `pop` → 非搬不可時才搬元素 | `out` 依 pop 順序放著最舊的元素；**只在** `out` 空掉時才補滿它 |
| **1603** Design Parking System | 三個計數器（見 §6-3） | `addCar` 只問「還有沒有剩？」→ 不需要身分 | `remaining[t] == capacity[t] - parked(t)`，且永不為負 |

**面試時怎麼讀這張表**：第三欄是你在寫下任何欄位*之前*就該講出來的那句話（「`getRandom` 必須是 O(1)，所以儲存必須是陣列 — 其他都是跟著推出來的」）。第四欄是你寫在欄位上方當註解的那句話；它就是把一堆 map 變成一個設計的東西。

### 6-2) LC 設計題的四步流程 ⭐⭐⭐⭐⭐

把 §0-1 的五步 OOD 法，壓縮成適用於「API 已經固定」的題目：

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

**只有當一個輔助類別帶著身分或行為時才引入它。** `Node`、`TrieNode`、`Bucket`、`Tweet` 都對得起它們的位置（它們的狀態活得比單次呼叫久）。一個只是被回傳一次的 tuple 卻硬包成「類別」，那是雜訊 — LC 設計題的評分看的是狀態模型，不是類別數量。

### 6-3) 完整的橋梁範例 — LC 1603 Design Parking System ⭐⭐⭐⭐

這是「需求決定你需要多少 OOD」最乾淨的例證：它就是 §2-1 的停車場，把所有能正當化那些物件的需求全部拿掉之後的樣子。

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

**現在只加一條需求 — 「車會離場，而且小車可以停大車位」** — 計數器就撐不住那個不變式了（你沒辦法釋放一個從來沒被識別過的「車位」，而且能不能停的規則現在變成一種政策）。這正是 §2-1 那套物件模型開始划算的那一刻：

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

**能拿分的那句話**：「LC 1603 不需要物件，因為沒有東西有身分，也沒有東西有生命週期。加上 `leave()`，身分就出現了；加上停車位相容規則，Strategy 就出現了；加上計費，§2-1 的 `Ticket` + `PricingStrategy` 就出現了。」能指名是哪一條*需求*創造出每一個類別，整個功力就在這裡。

### 6-4) 在 LC 題目內部做實體建模 — LC 355 Design Twitter ⭐⭐⭐⭐

這是唯一一道常見的 LC 設計題，標準答案要的是真正的**實體建模**（而不只是一個資料結構）：

```text
Twitter  ──has-a──▶ Map<Integer, User>   (registry of users)
Twitter  ──has-a──▶ int clock            (global monotonic timestamp)
User     ──has-a──▶ Set<Integer> followees, List<Tweet> tweets
Tweet    { int id, int time }            (immutable value object)

getNewsFeed(u) = top-10 by time over { tweets of u } ∪ { tweets of each followee }
```

- **為什麼 `Tweet` 要是類別**：依新舊排序需要時間戳，所以光一個 tweet id 承載不了這個狀態 — 是查詢逼出了這個值物件。
- **為什麼全域時鐘放在 `Twitter` 而不是 `User` 上**：「時間戳可以跨使用者比較」這個不變式，任何單一使用者都維護不了。
- **追蹤自己這個技巧**：註冊時做 `follow(u, u)`，`getNewsFeed` 就變成對追蹤對象的一次統一合併，不用特例處理 — 這是為了刪掉一個分支而刻意選的不變式。
- **哪個操作決定結構**：`getNewsFeed`（跨 k 個排序串列取前 10）→ 每個使用者的 tweet 串列保持最新的在最後，再加上 k 路合併（堆積）。`postTweet`／`follow`／`unfollow` 怎麼做都是 O(1)。

> k 路合併本身的實作是資料結構練習 — 見 [`design.md`](design.md) 與 [`heap.md`](heap.md)。OOD 面試官在這題想看的，是上面那張類別圖加上那兩個不變式。

### 6-5) 只當參考、值得看一眼的 LC 設計題

同一套功力，沒有新模板 — §6-2 練到變成反射之後，這些是很好的操練材料：

- LC 1797 Design Authentication Manager — `Map<token, expiryTime>`；不變式：一個 token 有效，若且唯若它存的到期時間 > 現在（惰性過期，不要主動掃）。
- LC 2013 Detect Squares — `Map<point, count>`；是 `count()` 查詢（挑一條對角線，推出另外兩個角）決定了「點的多重集」這個狀態。
- LC 2034 Stock Price Fluctuation — `Map<timestamp, price>` + 價格的有序多重集；不變式：多重集裡精確地放著每個時間戳的*當前*價格。
- LC 1352 Product of the Last K Numbers — 前綴乘積串列；不變式：遇到 `0` 就重置串列，讓每個存下來的前綴都不是零。
- LC 707 Design Linked List / LC 1206 Design Skiplist — 純粹的節點類別建模練習。
- LC 715 Range Module / LC 731 My Calendar II / LC 732 My Calendar III — 把 §6-1 中 LC 729 那個不變式（「任兩個已存區間都不重疊」）一步步放寬。
