🏦 Java Bank Application System
📌 Description

A console-based Bank Application System built using Core Java, implementing Object-Oriented Programming (OOP) principles and layered architecture.
The application performs essential banking operations using in-memory data storage without any database.

🚀 Features

   Open a new bank account

   money into an account

   Withdraw money from an account

   Transfer money between accounts

   List all bank accounts

Search accounts by customer name

Interactive console-based menu

🧱 Project Architecture

   The application follows a layered design, similar to real-world enterprise applications.

src/
 ├── app         → Application entry point (Main class)
 ├── domain      → Business entities (Account, Customer, Transaction)
 ├── service     → Business logic layer
 ├── repository  → In-memory data storage
 ├── exceptions  → Custom exception handling
 └── util        → Utility/helper classes

🛠 Technologies Used

     .Java (Core Java)

     .Object-Oriented Programming (OOP)

    .Exception Handling

    .Java Collections Framework

▶️ How to Run the Application

Clone the repository:

    git clone https://github.com/your-username/java-bank-application-system.git


    Open the project in IntelliJ IDEA or Eclipse

    Navigate to Main.java

    Run the main() method

📸 Sample Console Menu
1) Open Account
2) Deposit
3) Withdraw
4) Transfer
5) Account Statement
6) List Accounts
7) Search Accounts by Customer Name
0) Exit

🎯 OOP Concepts Used

      .Abstraction – Using BankService interface

      .Polymorphism – Interface reference with implementation

      .Encapsulation – Private fields with controlled access

      .Loose Coupling – Interface-driven design

🔮 Future Enhancements

      .Add file-based persistence

      .Integrate database (MySQL)

      .Convert to Spring Boot REST API

      .Add logging framework

👨‍💻 Author

     .Rajan Gupta

⭐ Support

    .If you find this project useful, please give it a ⭐ on GitHub!

📌 Notes

    .This project does not use any database

    .All data is stored in memory

    .IDE-specific files (.idea, *.iml, out/) are ignored
