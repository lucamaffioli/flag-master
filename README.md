# World Flags Quiz System

A Full-Stack Java application designed to provide an interactive educational experience through world flag quizzes. This project was developed as the final assignment for the **Programmazione Avanzata** (Advanced Programming) course.

🎓 **Institution:** University of Pisa (UniPi)  
🏛 **Department:** BSc in Computer Engineering (*Ingegneria Informatica*)  

---

## 🌟 Project Overview

The system allows users to test their knowledge of world flags and create custom lists to track their learning progress. It features a robust **Client-Server architecture**, ensuring a clean separation between core logic, data management, and the user interface.

- **Backend:** Manages game logic and data persistence, exposing endpoints via a REST API.
- **Frontend:** A responsive desktop graphical interface for seamless gameplay.

---

## 🛠 Tech Stack

- **Language:** Java 21
- **Backend Framework:** Spring Boot (REST API)
- **Data Access:** Spring Data JPA / Hibernate
- **Database:** MySQL
- **Frontend Framework:** JavaFX
- **Build Tool:** Maven
- **Environment:** Developed using NetBeans IDE on Linux

---

## 📂 Repository Structure

- `server/`: The Spring Boot backend infrastructure and API logic.
- `app/`: The JavaFX desktop application and user interface.
- `docs/`: Project documentation with screenshots and the original university assignment.

---

## 🚀 How to Run

### 1. Prerequisites
- **Java JDK 21**
- **Maven** installed on your system (`sudo dnf install maven` on Fedora).
- A running **MySQL** server.

### 2. Database Setup
The application is configured to automatically manage the database lifecycle.
1. Ensure your MySQL server is running.
2. Configure your MySQL credentials (username and password) in:  
   `server/src/main/resources/application.properties`
3. **Note:** Upon the first execution, Spring Boot will automatically create the database schema and all required tables. No manual database creation is required.

### 3. Start the Backend
Open a terminal in the `server` directory and run:
```bash
mvn spring-boot:run
```
The server will start at `http://localhost:8080`.

### 4. Start the Frontend
Open a new terminal in the `app` directory and run:
```bash
mvn javafx:run
```

---

## 📄 License
This project is licensed under the **MIT License** - see the [LICENSE](./LICENSE) file for details.

---
*Developed by Luca Maffioli - BSc in Computer Engineering @ UniPi*
