# E-commerce Platform for Mobile Phones

A full-featured e-commerce web application built with Spring Boot and Thymeleaf. This platform allows a single admin to manage a product catalog and enables users to register, manage their accounts, and purchase products using a virtual wallet system.

---

## ✨ Features

### Admin Role
- **Secure Login**: Separate login for the administrator.
- **Dashboard**: A dedicated admin account page showing profile information, total products managed, and a visual grid of all products.
- **Product Management (CRUD)**: Full control over the product catalog.
  - **Create**: Add new mobile phone products with details like name, description, price, stock, color, and image URL.
  - **Read**: View all products in a clean, organized table.
  - **Update**: Edit the details of any existing product.
  - **Delete**: Remove products from the store.
- **Revenue Tracking**: The admin's account balance automatically increases with every user purchase.

### User/Customer Role
- **User Registration**: Customers can create a new account with their name, email, and password.
- **Secure Login**: A robust login system for returning customers.
- **Account Management**: A personal account page where users can:
  - View their profile information.
  - See their current account **balance**.
  - **Deposit** funds into their virtual wallet.
  - View a detailed **order history** with product images.
- **Product Shopping**:
  - Browse all available products on a visually appealing shop page.
  - Add products to a persistent **shopping cart**.
- **Shopping Cart Management**:
  - View all items in the cart.
  - Update the quantity of items.
  - Remove items from the cart.
- **Secure Checkout**:
  - A confirmation page to review the order before purchase.
  - The system checks for sufficient balance before processing the payment.
  - Upon purchase, the order is saved, the user's balance is debited, and the admin's balance is credited.

---

## 🛠️ Technologies Used

- **Backend**: Spring Boot 3
- **Frontend**: Thymeleaf, Tailwind CSS
- **Database**: MySQL
- **Data Access**: Spring Data JPA / Hibernate
- **Utilities**: Lombok

---

## 🚀 Setup and Installation

Follow these steps to get the project running on your local machine.

### Prerequisites
- Java Development Kit (JDK) 17 or later
- Apache Maven
- MySQL Server

### 1. Database Setup
1.  Open your MySQL client (e.g., phpMyAdmin, MySQL Workbench).
2.  Create a new database named `pmsdb`:
    ```sql
    CREATE DATABASE pmsdb;
    ```
3.  No need to create tables manually. Hibernate will generate them for you on the first run.

### 2. Configure the Application
1.  Open the `src/main/resources/application.properties` file.
2.  Update the database credentials to match your MySQL setup:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/pmsdb
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    ```
3.  Set the `ddl-auto` property. For the first run, use `create`:
    ```properties
    spring.jpa.hibernate.ddl-auto=create
    ```
    After the first successful run, change this to `update` to preserve your data on subsequent restarts.

### 3. Run the Application
1.  Open a terminal in the root directory of the project.
2.  Run the application using Maven:
    ```bash
    mvn spring-boot:run
    ```
3.  The application will be accessible at **http://localhost:8080**.

### 4. Initial Data (Required)
For the application to function, you must have at least one admin. Run the following SQL script to create the default admin:
```sql
INSERT INTO Admin (username, password, email, balance)
VALUES ('superadmin', 'password', 'superadmin@example.com', 0.00);
```
**Note**: Passwords are not hashed in this version. For a production environment, implement Spring Security.

---

## 🗂️ Database Schema

The application uses the following main tables:
- **Admin**: Stores the single administrator's account and balance.
- **AppUser**: Stores customer accounts and their balances.
- **Product**: Contains all product details and is linked to an `Admin`.
- **CartItem**: A temporary table for items in a user's shopping cart.
- **Orders**: Stores the header information for each completed purchase.
- **OrderItem**: Stores the line-item details for each order.
