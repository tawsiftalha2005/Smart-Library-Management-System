# 📚 Smart Library Management System

![Java](https://img.shields.io/badge/Java-26-orange?style=for-the-badge&logo=openjdk)
![Platform](https://img.shields.io/badge/Platform-JavaFX%20%2B%20Console-blue?style=for-the-badge)
![OOP](https://img.shields.io/badge/OOP-Java-success?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

A native desktop Smart Library Management System developed with **Java 26**, **Maven**, **JavaFX**, and object-oriented programming. The JavaFX application is the primary interface; the original menu-driven console version remains available.

The project focuses on **clean code**, **modular architecture**, and **object-oriented design**, making it an excellent learning project for Java beginners and a strong addition to a software engineering portfolio.

---

# 📖 Overview

Managing a library manually can be difficult and time-consuming. This project simulates a basic library management system where books, members, and borrowing activities can be managed through a console interface.

The application is organized into multiple packages following good software design practices, making it easy to understand, maintain, and extend.

---

# ✨ Features

## Modern JavaFX workspace

- Persistent, collapsible navigation sidebar
- Dashboard with live catalog, member, borrowing, returned, and overdue figures
- Books and members management screens with search and service-backed CRUD
- Borrow/return workflow with availability, member, record, and date validation
- Searchable borrow-record history with Active, Returned, and Overdue filters
- Light and dark themes, persisted locally between launches
- Resizable desktop layout designed for 1440×900 and smaller desktop windows

## 📚 Book Management

- ➕ Add New Book
- 📖 View All Books
- 🔍 Search Book by ID
- ✏ Update Book Quantity
- 🗑 Delete Book

---

## 👤 Member Management

- ➕ Register New Member
- 👥 View All Members
- 🔍 Search Member by ID
- 🗑 Delete Member

---

## 🔄 Borrow Management

- 📕 Borrow Book
- 📗 Return Book
- 📋 View Borrow Records

---

## ✅ Input Validation

- Integer input validation
- Empty string validation
- User-friendly error messages

---

# 🏗 Project Structure

```text
Smart-Library-Management-System/
│
├── src/main/
│   ├── java/
│   │  ├── model/
│   │      Book.java
│   │      Member.java
│   │      BorrowRecord.java
│   │
│   │  ├── service/
│   │      BookService.java
│   │      MemberService.java
│   │      BorrowService.java
│   │
│   │  ├── utils/
│   │      FileManager.java
│   │      InputValidator.java
│   │
│   │  ├── ui/ (MainLayout, DashboardView, BookView, MemberView,
│   │  │         BorrowView, BorrowRecordView, SettingsView)
│   │  ├── Main.java (JavaFX entry point)
│   │  └── ConsoleMain.java (console entry point)
│   └── resources/css/style.css (light and dark design system)
│
├── data/
│
├── README.md
└── .gitignore
```

---

# 🛠 Technologies Used

- Java 26
- Maven
- JavaFX 26.0.2
- Object-Oriented Programming (OOP)
- Java Collections Framework
- IntelliJ IDEA
- Git
- GitHub

---

# 🧩 OOP Concepts Used

This project demonstrates the following Java OOP concepts:

- ✅ Classes & Objects
- ✅ Encapsulation
- ✅ Constructors
- ✅ Packages
- ✅ Method Calling
- ✅ ArrayList Collection

---

# 🚀 Getting Started

## Clone the Repository

```bash
git clone https://github.com/tawsiftalha2005/Smart-Library-Management-System.git
```

---

## Open the Project

Open the project using **IntelliJ IDEA**.

---

## Run the Project

Use JDK 26 and Maven. IntelliJ IDEA can import `pom.xml` as a Maven project; no manually configured JavaFX SDK path is needed.

### Run the GUI (primary interface)

```bash
mvn javafx:run
```

### Run the console version

```bash
mvn exec:java -Dexec.mainClass=ConsoleMain
```

The current service layer keeps catalog and borrowing records in memory for the duration of a run. `FileManager` continues to initialize the existing `data/` files; it is intentionally not bypassed or replaced by the GUI.

---

# 🎨 Appearance

Use the header theme button or **Settings → Appearance** to switch between light and dark mode. The selected mode is saved with Java preferences and is restored next time the GUI starts.

---

# 💻 Console Preview

```text
=========================================
     SMART LIBRARY MANAGEMENT SYSTEM
=========================================
1. Book Management
2. Member Management
3. Borrow Book
4. Return Book
5. View Borrow Records
6. Exit
=========================================
Enter your choice:
```

---

# 📂 Packages Description

## 📦 model

Contains all data model classes.

- Book
- Member
- BorrowRecord

---

## 📦 service

Contains the business logic of the application.

- BookService
- MemberService
- BorrowService

---

## 📦 utils

Contains helper classes.

- InputValidator
- FileManager

---

# 🎯 Learning Outcomes

This project helped strengthen my understanding of:

- Java Programming
- Object-Oriented Programming
- Collections Framework
- Package Organization
- Modular Programming
- Console Application Development
- Software Design
- Git & GitHub Workflow

---

# 📈 Future Improvements

The following features are planned for future versions:

- 🔐 Login System
- 📂 Permanent File Storage
- 🗃 Database Integration (MySQL)
- 🔎 Search by Book Title
- 📊 Library Statistics Dashboard
- 📅 Due Date Management
- 💰 Fine Calculation
- 📚 Book Reservation
- 🔎 Richer multi-field search and report export

---

# 💡 Design Philosophy

The goal of this project is not only to build a working Library Management System but also to write **clean**, **organized**, and **maintainable** Java code.

Each package has a specific responsibility:

- **model** → Data representation
- **service** → Business logic
- **utils** → Utility/helper classes

This separation makes the project easier to understand and extend.

---

# 👨‍💻 Developer

## Md. Wahid Tawsif Talha

**Software Engineering Student**

Green University of Bangladesh

### GitHub

https://github.com/tawsiftalha2005

---

# 🤝 Contributing

Contributions, suggestions, and improvements are always welcome.

Feel free to fork this repository and submit a pull request.

---

# ⭐ Support

If you found this project helpful, please consider giving it a **⭐ Star** on GitHub.

Your support motivates me to build more open-source projects.

---

## 📄 License

This project is licensed under the **MIT License**.
