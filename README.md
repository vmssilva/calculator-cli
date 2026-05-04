# Calculator REPL

A simple interactive REPL (Read-Eval-Print Loop) in Java for experimenting with the  
[Calculator Engine](https://github.com/vmssilva/calculator-engine).

A terminal-based interface with real-time evaluation and ANSI-powered live preview.

---

## ✨ Features

- Interactive REPL interface
- Real-time expression evaluation
- Live preview while typing
- Command history and line editing
- Built-in commands (`exit`, `clear`, etc.)
- Pure ANSI terminal rendering (no external UI libs)

---

## 📦 Installation

The CLI depends on the Calculator Engine.

### 1. Install Calculator Engine

```bash
git clone https://github.com/vmssilva/calculator-engine
cd calculator-engine
mvn clean install
````

---

### 2. Build CLI

```bash
git clone https://github.com/vmssilva/calculator-cli
cd calculator-cli
mvn clean package
```

---

### 3. Run

```bash
java -jar target/calc.jar
```

---

## 🧠 Usage

```txt
calc> 2 + 2
4
```

Multiple expressions:

```txt
calc> 2 + 2; 3 + 3
6
```

---

## ⚡ Live Preview

Expressions are evaluated in real time while typing:

```txt
calc> 10 * (2 + 3)
50
```

---

## ⌨️ Navigation

| Key        | Action           |
| ---------- | ---------------- |
| ↑ / ↓      | History          |
| ← / →      | Move cursor      |
| Backspace  | Delete character |
| Home / End | Line navigation  |
| Enter      | Execute          |

---

## 🧩 Commands

Commands start with `\`:

```txt
\exit   → exit REPL
\q      → quit
\clear  → clear screen
```

---

## 🧪 Examples

### Expressions

```txt
calc> (10 + 2) * 3
36

calc> sqrt(144)
12
```

---

### Variables

```txt
calc> a = 10
0

calc> a + 5
15
```

---

### Functions & Lambdas

```txt
calc> add(10, 20)
30

calc> ((x) -> (y) -> x * y)(10)(2)
20
```

---

## 🧠 Behavior

* Last expression is always returned
* `;` separates multiple expressions
* Assignments return `0`
* Functions and lambdas are first-class values
* Built-in functions expose signature when referenced

---

## 📦 Architecture

* **KeyMapper** → maps keyboard input to commands
* **Commands** → mutate REPL state
* **State** → buffer, cursor, history
* **Renderer** → terminal output layer
* **Engine** → expression evaluation core

---

## 📌 Notes

* Uses raw ANSI escape sequences
* No external UI libraries
* Behavior may vary depending on terminal emulator

---

## 🪪 License

MIT

