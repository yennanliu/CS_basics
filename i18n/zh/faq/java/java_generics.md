<!-- 01bd1512ad41 -->
# Java 泛型 FAQ

> **範圍** — 型別參數、型別抹除（type erasure）以及由它衍生的一切：有界型別、通配符與 PECS、泛型方法，還有為什麼泛型陣列不合法。
> **另見**：[`java_collection.md`](./java_collection.md) — 你每天在用的泛型 API；
> [`faq_OOP.md`](./faq_OOP.md) — 變異性（variance）與 Liskov 替換原則。

泛型把型別錯誤從執行期（`ClassCastException`）搬到編譯期，也讓同一份實作服務多種型別
而不必到處轉型。

---

<!-- 1ae5e5eda588 -->
## 1) 基礎 ⭐⭐⭐⭐

<!--CODE-->

慣用的命名：`T` 型別、`E` 元素、`K`/`V` key/value、`R` 結果、`N` 數字。

<!-- 5170ba5c89f2 -->
### 泛型方法

型別參數列放在**回傳型別之前**，而且可以和類別本身無關：

<!--CODE-->

<!-- ef8a98be2653 -->
### 有界型別參數

<!--CODE-->

這裡的 `extends` 對類別和介面都一樣，意思是「是……的子型別」。**型別參數上沒有
`super` 界限** —— 只有通配符才有。

---

<!-- a99fa0d7d298 -->
## 2) 型別抹除 ⭐⭐⭐⭐⭐

泛型是**編譯期**的機制。編譯器檢查型別、插入轉型，然後把型別引數**抹除**：
`List<String>` 與 `List<Integer>` 在執行期都只是 `List`。選擇抹除是為了和 Java 5
之前的 bytecode 保持向後相容。

<!--CODE-->

以下每一條都是抹除造成的後果：

| 你不能 | 因為 | 改成 |
|-----------|---------|-----------|
| `new T()` | 型別引數在執行期已經不存在 | 傳一個 `Supplier<T>`，或傳 `Class<T>` 型別權杖再呼叫 `getDeclaredConstructor().newInstance()`（`Class.newInstance()` 已棄用） |
| `new T[10]` | 建立陣列需要可具體化（reifiable）的型別 | 改用 `List<T>`，或用型別權杖建出真正的陣列：`(T[]) Array.newInstance(componentType, 10)`。`(T[]) new Object[10]` 本質上還是 `Object[]`，回傳給期待 `String[]` 的呼叫端會丟 `ClassCastException` |
| `x instanceof List<String>` | 只有 raw type 留了下來 | `x instanceof List<?>` |
| `static T field;` | static 成員屬於類別，不屬於某次型別實例化 | 把方法做成泛型方法 |
| 同時多載 `List<String>` 與 `List<Integer>` | 抹除後簽章一樣 | 把方法改名 |
| `catch (MyException<T> e)` | 例外型別必須可具體化 | 用非泛型的例外型別 |

**橋接方法（bridge method）**：為了讓多型在抹除後仍然成立，編譯器會合成額外的方法 ——
例如一個實作 `Comparable<Person>` 的類別，會同時有 `compareTo(Person)` 和一個合成的
`compareTo(Object)`，後者負責轉型再委派。這就是為什麼一個 unchecked 轉型可以在你
根本沒寫過的方法裡爆掉。

**堆汙染（heap pollution）與 unchecked 警告**發生在抹除讓錯誤型別的值流進泛型變數時 ——
通常是經由 raw type 或 varargs 陣列。`@SafeVarargs` 的意思是「我保證這個泛型
varargs 方法只讀它的陣列」。

---

<!-- a2f82a8dac76 -->
## 3) 通配符與 PECS ⭐⭐⭐⭐⭐

**泛型是不變的（invariant）**：即使 `String` 是 `Object`，`List<String>` 也*不是*
`List<Object>`。如果是的話，你就能透過那個較寬的參考把 `Integer` 塞進字串清單裡。

通配符在安全的前提下把彈性還給你：

| 寫法 | 意思 | 你可以 |
|------|-------|---------|
| `List<?>` | 元素型別未知 | 以 `Object` 讀取；`remove`/`clear` 還能用，但不能**加入**任何非 null 的值（元素型別未知） |
| `List<? extends Number>` | `Number` 的某個子型別（**生產者**） | **讀**出 `Number`；不能加入（誰知道是哪個子型別？） |
| `List<? super Integer>` | `Integer` 的某個父型別（**消費者**） | **加入** `Integer`；讀出來只能當 `Object` |

**PECS —— Producer 用 `extends`，Consumer 用 `super`：**

<!--CODE-->

JDK 裡到處都是：`Collections.max(Collection<? extends T>)`、
`Stream.map(Function<? super T, ? extends R>)`、`forEach(Consumer<? super T>)`。
經驗法則：**通配符放參數，不要放回傳型別** —— 回傳型別帶通配符會逼得每個呼叫端
也跟著用通配符。

<!-- eaaea18a1c48 -->
### 通配符還是型別參數？

同一個型別出現超過一次（也就是它們之間的關係有意義）時用型別參數 `<T>`；方法根本
不在意型別是什麼時就用 `?`：

<!--CODE-->

---

<!-- 8c90d7e35606 -->
## 4) Raw type 與舊程式碼的互通 ⭐⭐⭐

**raw type** 是不帶型別引數使用的泛型類別（`List list = ...`）。它的存在只為了和
Java 5 以前相容：它會關掉所有泛型檢查、產生 unchecked 警告，並讓型別參數失去意義。

<!--CODE-->

真的不知道元素型別時，請用 `List<?>` —— 它保留了型別安全。

---

<!-- 97f6a333b818 -->
## 5) 泛型與陣列 ⭐⭐⭐

陣列是**共變（covariant）且可具體化（reified）**的；泛型是**不變且被抹除**的。
兩套模型合不起來。

<!--CODE-->

因為陣列在執行期檢查元素型別而泛型不檢查，所以 `new T[n]` 與 `new List<String>[10]`
都不合法。優先用集合；真的必須從泛型程式碼回傳陣列時，照 JDK 的
`T[] toArray(T[] a)` 那個形狀寫。

---

<!-- 78e5817a1bf1 -->
## 6) 面試常見問答

**Q：泛型解決了什麼問題？**
編譯期的型別安全，以及不用再轉型 —— 原本的 `ClassCastException` 變成編譯錯誤，
而型別也寫在簽章裡當文件。

**Q：什麼是型別抹除，Java 為什麼要這樣做？**
型別引數檢查完就丟掉，所以泛型程式碼編出來的 bytecode 和沒有泛型的時代一樣。
這是 Java 5 為了二進位向後相容付的代價。

**Q：`List<Object>`、`List<?>` 和 raw `List` 差在哪？**
`List<Object>` 什麼元素都收，但只能對應宣告為 `List<Object>` 的清單。
`List<?>` 對應「某個未知型別」的清單；你可以從它讀、也可以從它移除，但除了 `null`
什麼都不能加。raw `List` 則是把檢查整個關掉 —— 新程式碼裡絕對不要寫。

**Q：解釋 PECS。**
見 §3 —— 從 `? extends` 讀，往 `? super` 寫。

**Q：`void f(List<String>)` 和 `void f(List<Integer>)` 可以多載嗎？**
不行 —— 兩者都抹除成 `f(List)`，等於重複定義同一個方法。

**Q：執行期要怎麼拿到 `Class<T>`？**
明確傳進來（`Class<T> type` —— 也就是「型別權杖」模式，`EnumMap`／Jackson 都這樣做），
或是從子類別的泛型父型別把它抓出來（`TypeReference`、`ParameterizedType`），
因為那份資訊*確實*保留在 class 檔裡。

**Q：為什麼泛型類別不能繼承 `Throwable`？**
`catch` 的比對需要精確的執行期型別，而抹除正好把它銷毀了。

---

<!-- ef160d7245e4 -->
## 7) 重點檢查表

<!--CODE-->

---

<!-- 8a9fc577aaea -->
## 參考資料

- [Java Tutorials — Generics](https://docs.oracle.com/javase/tutorial/java/generics/)
- [`java_collection.md`](./java_collection.md) — 泛型集合 API
- [`faq_OOP.md`](./faq_OOP.md) — 從 OOP 角度看替換與變異性
