# 📚 Smart Library Management System

![Java](https://img.shields.io/badge/Java-26-orange?style=for-the-badge&logo=openjdk)
![Platform](https://img.shields.io/badge/Platform-JavaFX%20%2B%20Console-blue?style=for-the-badge)
![OOP](https://img.shields.io/badge/OOP-Java-success?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)


A modern **Java-based Library Management System** built using **Object-Oriented Programming (OOP)** principles. The system provides both a **native JavaFX graphical interface** and a **console-based interface** for managing books, members, borrowing, returning, and library records.

The project is designed as an academic and portfolio project with a clean service-based architecture, file persistence, validation, reusable JavaFX UI components, and Light/Dark theme support.

---

## ✨ Features

### 📊 Dashboard
- Total Books overview
- Total Members overview
- Currently Borrowed books
- Overdue books
- Recent borrowing activity
- Borrowing activity visualization
- Overdue record overview
- Real-time statistics from persisted data

### 📚 Book Management
- Add books
- Update books
- Delete books
- Search books
- View book information
- Track total copies
- Track available copies
- Automatic availability updates
- Book status indicators
- Validation for book information
- Protection against deleting books with active loans

### 👥 Member Management
- Add members
- Update members
- Delete members
- Search members
- View member information
- Track member borrowing activity
- Member status management
- Protection against deleting members with active loans

### 🔄 Borrow & Return
- Borrow books
- Return books
- Track borrow date
- Track due date
- Track return date
- Prevent duplicate active borrowing
- Automatic book quantity updates
- Automatic availability restoration after return
- Overdue detection
- Validation for invalid borrowing/return operations

### 📋 Borrow Records
- View all borrowing records
- Search records
- Filter by status
- Track:
  - Record ID
  - Member
  - Book
  - Borrow Date
  - Due Date
  - Return Date
  - Status
- Status support:
  - Borrowed
  - Returned
  - Overdue

### 🎨 Modern JavaFX UI
- Professional desktop interface
- Figma-inspired visual design
- Persistent sidebar navigation
- Dashboard layout
- Modern cards and tables
- Reusable UI components
- Modal dialogs for forms
- Search and filtering controls
- Status badges
- Success/error feedback
- Responsive layout behavior
- Clean typography and spacing

### 🌗 Light & Dark Mode
- Light theme
- Dark theme
- Theme switching
- Theme-aware:
  - Text
  - Buttons
  - Forms
  - Tables
  - Dialogs
  - Status badges
  - Navigation
- Improved contrast and readability in both themes

### 💾 File Persistence
The system stores data using local text files.

Persistent data includes:

- Books
- Members
- Borrow Records

Data remains available after restarting the application.

### 🖥️ Console Interface
The project also maintains a console-based interface for library operations.

The console application uses the same service layer and business logic as the JavaFX application.

---

## 🏗️ Project Architecture

```text
Smart-Library-Management-System/
│
├── data/
│   ├── books.txt
│   ├── members.txt
│   └── borrow_records.txt
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── model/
│   │   │   │   ├── Book.java
│   │   │   │   ├── Member.java
│   │   │   │   └── BorrowRecord.java
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── BookService.java
│   │   │   │   ├── MemberService.java
│   │   │   │   └── BorrowService.java
│   │   │   │
│   │   │   ├── utils/
│   │   │   │   ├── FileManager.java
│   │   │   │   └── InputValidator.java
│   │   │   │
│   │   │   ├── ui/
│   │   │   │   ├── MainLayout.java
│   │   │   │   ├── DashboardView.java
│   │   │   │   ├── BorrowRecordView.java
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── Main.java
│   │   │   └── ConsoleMain.java
│   │   │
│   │   └── resources/
│   │       └── css/
│   │           └── style.css
│   │
│   └── test/
│       └── java/
│           └── LibraryServiceTest.java
│
├── pom.xml
├── .gitignore
├── LICENSE
└── README.md
```

---

## 🧩 Architecture Overview

### Model Layer

Contains the core domain objects:

- `Book`
- `Member`
- `BorrowRecord`

These classes represent the main entities of the library system.

### Service Layer

Contains the application's business logic:

- `BookService`
- `MemberService`
- `BorrowService`

The service layer is responsible for:

- CRUD operations
- Borrowing rules
- Return processing
- Validation
- Quantity management
- Overdue calculation
- Business rule enforcement

The JavaFX UI does not duplicate this business logic.

### Utility Layer

Provides shared functionality:

- `FileManager`
- `InputValidator`

Responsibilities include:

- File reading/writing
- Data persistence
- Input validation
- Safe handling of stored data

### UI Layer

The JavaFX interface is organized into reusable views and layouts.

Major UI components include:

- `MainLayout`
- `DashboardView`
- Books View
- Members View
- Borrow & Return View
- Borrow Records View
- Settings View

The UI communicates with the service layer to display and update real application data.

---

## 🔄 System Workflow

### Borrowing Workflow

```text
User
  │
  ▼
JavaFX / Console Interface
  │
  ▼
Service Layer
  │
  ├── Validate Request
  ├── Check Member
  ├── Check Book
  ├── Check Availability
  └── Create Borrow Record
          │
          ▼
     File Persistence
```

### Returning Workflow

```text
Return Request
      │
      ▼
BorrowService
      │
      ├── Validate Record
      ├── Update Return Date
      ├── Update Record Status
      └── Restore Book Availability
              │
              ▼
        File Persistence
```

---

## 🔐 Business Rules

### Book Availability

Each book maintains:

```text
Total Copies
Available Copies
```

The system ensures that available copies do not exceed total copies.

### Borrowing

A book cannot be borrowed when:

```text
Available Copies <= 0
```

A member cannot create an invalid duplicate active borrowing record for the same book.

### Returning

When a book is returned:

```text
Available Copies += 1
```

The corresponding borrow record is updated with:

```text
Return Date
Status
```

### Deletion Protection

Books or members with active borrowing relationships cannot be deleted.

This prevents invalid references in the library records.

### Overdue Detection

A borrowing record can be identified as overdue based on its due date and return status.

The dashboard and borrow records use this information to display overdue records.

---

## 🖥️ User Interface

The JavaFX interface follows a modern desktop application design inspired by the project's Figma reference.

### Main Navigation

```text
Dashboard
Books
Members
Borrow & Return
Borrow Records
Settings
```

### UI Design Includes

- Persistent sidebar
- Page headers
- Cards
- Tables
- Search fields
- Filters
- Modal forms
- Confirmation dialogs
- Status badges
- Notification feedback
- Light/Dark themes

---

## 🎨 Theme Support

The application supports two main visual themes.

### Light Mode

Designed with:

- White cards
- Light application background
- Dark readable text
- Subtle borders
- Purple primary accent

### Dark Mode

Designed with:

- Dark application background
- Dark cards
- Light readable text
- High-contrast inputs
- Consistent accent colors

The UI components are styled through JavaFX CSS.

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java 26 | Programming Language |
| JavaFX | Desktop GUI |
| Maven | Build & Dependency Management |
| JUnit | Testing |
| JavaFX CSS | UI Styling |
| Local Text Files | Data Persistence |
| IntelliJ IDEA | Development Environment |
| Git & GitHub | Version Control |

---

## 📦 Requirements

Before running the project, make sure you have:

- JDK 26
- Maven
- IntelliJ IDEA (recommended)
- JavaFX dependencies configured through Maven

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

---

## 🚀 Running the Project

### 1. Clone the Repository

```bash
git clone https://github.com/tawsiftalha2005/Smart-Library-Management-System.git
```

### 2. Enter the Project Directory

```bash
cd Smart-Library-Management-System
```

### 3. Build the Project

```bash
mvn clean verify
```

### 4. Run the JavaFX Application

```bash
mvn javafx:run
```

---

## 🖥️ Running from IntelliJ IDEA

1. Open the project in IntelliJ IDEA.
2. Make sure JDK 26 is selected.
3. Allow Maven to load the dependencies.
4. Reload the Maven project.
5. Run the JavaFX main application.

The project uses the Maven standard directory structure:

```text
src/main/java
src/main/resources
src/test/java
```

---

## 💻 Running the Console Version

The project also includes a console-based entry point:

```text
ConsoleMain.java
```

The console interface uses the same service layer and persistence system as the JavaFX application.

This allows the project to demonstrate both:

- Console-based OOP application design
- GUI-based desktop application design

---

## 🧪 Testing

Unit tests are included for the main library service workflows.

Run:

```bash
mvn clean test
```

For a complete Maven verification:

```bash
mvn clean verify
```

The tests cover important scenarios such as:

- Book operations
- Member operations
- Borrowing
- Duplicate borrowing prevention
- Returning
- Quantity restoration
- Persistence/reloading

---

## 📁 Data Storage

The application uses local text files for persistence.

```text
data/
├── books.txt
├── members.txt
└── borrow_records.txt
```

This approach keeps the project lightweight and avoids requiring an external database.

---

## 🔮 Future Improvements

Possible future improvements include:

- Database integration using MySQL/PostgreSQL
- User authentication and role management
- Admin and librarian roles
- Advanced reporting
- PDF report generation
- Email notifications
- Fine calculation
- Book cover images
- Advanced filtering
- Pagination for large datasets
- Cloud-based data storage
- Automated backup and restore
- Improved analytics dashboard

---

## 🎯 Learning Objectives

This project demonstrates practical application of:

- Object-Oriented Programming
- Encapsulation
- Abstraction
- Inheritance
- Polymorphism
- Composition
- Separation of concerns
- Service-layer architecture
- File handling
- Data validation
- Exception handling
- JavaFX GUI development
- JavaFX CSS styling
- Maven project management
- Unit testing
- Git & GitHub workflow

---

## 👨‍💻 Developer

**Md. Wahid Tawsif Talha**

B.Sc. in Software Engineering

Green University of Bangladesh

GitHub:

https://github.com/tawsiftalha2005

---

## 📄 License

This project is licensed under the MIT License.

See the [LICENSE](LICENSE) file for details.
