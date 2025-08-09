# WhatsApp Bot

This is a **WhatsApp Bot** built using **Java** and **Spring Boot**, with two separate implementations:
- **MySQL** (Relational Database)
- **Firebase** (NoSQL Database)

Both implementations offer the same functionalities but differ in the database management system used.

---

## 📌 Features
- Doctor appointment booking bot.
- Medicine ordering bot.
- Built using the **Meta for Developers** WhatsApp Cloud API.
- Two backend options:
  - **MySQL** for relational data storage.
  - **Firebase** for real-time NoSQL data storage.
- Tested locally using **ngrok** for webhook tunneling.

---

## ⚙️ Tech Stack
- **Java** (Spring Boot)
- **MySQL**
- **Firebase**
- **Meta WhatsApp Cloud API**
- **ngrok**

---

## 🔒 Sensitive Information
All personal credentials (phone number ID, access token, service account JSON for Firebase, etc.) have been **removed** from this repository.  
To run this project, you will need to provide:
- Your own **phone number ID** and **access token** from the Meta developer portal.
- Your own **Firebase service account JSON** file (for Firebase version).

---

## 🚀 Running the Project
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/whatsapp-bot.git
