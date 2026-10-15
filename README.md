# Oasis Infobyte Android Development Internship (OIBSIP)

Welcome to my repository for the **Oasis Infobyte Android Development Internship**. This repository houses production-ready, native Android applications built from scratch using **Android Studio, Java, and XML**. Each project follows modern Android architecture patterns, utilizing multi-activity navigation, local database persistence, custom UI adapters, and state management.

---

## 📱 Projects Overview

| Project | Description | Core Tech Stack |
| :--- | :--- | :--- |
| **1. To-Do App** | A fully-functional task management app with user authentication, local SQLite storage, and interactive card-based lists. | Java, SQLite, RecyclerView, CardView, XML |
| **2. Quiz App** | An interactive trivia application featuring randomized question shuffling, radio group options, and real-time color-coded feedback. | Java, Intent/Bundles, RadioGroup, XML |
| **3. Unit Converter** | A practical utility tool for seamless measurement conversions across multiple categories (length, weight, temperature, etc.). | Java, Adapters, XML Layouts |

---

## 🚀 Detailed Project Breakdown

### 1. To-Do Application (`TodoApp`)
A robust task tracker designed for secure personal productivity.
* **User Authentication:** Secure registration and login flow backed by local data validation.
* **SQLite Persistence:** Dynamic storage for user accounts and associated tasks using custom SQLite queries (`DatabaseHelper`).
* **Enhanced UI/UX:** Built with a custom `RecyclerView` adapter, displaying tasks inside styled `CardView` items with custom margins and rounded borders.
* **Full CRUD Operations:** Support for adding, editing, deleting, and toggling task completion states in real time.
* **Empty State Handling:** Gracefully displays clean placeholder indicators when no active tasks exist.

### 2. Quiz Application (`QuizApp`)
An engaging multiple-choice trivia game built to academic and professional standards.
* **Welcome Screen:** Clean splash interface welcoming the user and initiating app navigation via explicit `Intent` routing.
* **Randomized Question Repository:** Hardcoded question bank utilizing `Collections.shuffle()` to randomize question sequences on every fresh playthrough.
* **Single-Choice UI:** Implements `RadioGroup` and `RadioButton` elements to ensure precise multi-choice selection.
* **Immediate Feedback Loop:** Real-time color validation on answer submission—highlighting correct answers in **green** and incorrect selections in **red**, while locking states to prevent mid-question changes.
* **Telemetry & Results Screen:** Passes score metrics via `Bundle` extras to a summary activity displaying total scores, correct/incorrect counts, and a clean restart loop.

### 3. Unit Converter (`UnitConverter`)
A handy conversion utility designed for quick everyday calculations.
* **Multi-Category Support:** Clean spinners/selectors for transitioning between units of measurement.
* **Instant Calculation:** Responsive input fields providing accurate conversion metrics on the fly.

---

## 🛠️ Tech Stack & Prerequisites

* **IDE:** Android Studio (latest stable version recommended)
* **Language:** Java (JDK 8+)
* **UI Framework:** Android XML Layouts, Material Design Components
* **Database:** SQLite OpenHelper

---

## 📦 Getting Started & Installation

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/blackbox24/OIBSIP.git](https://github.com/blackbox24/OIBSIP.git)
