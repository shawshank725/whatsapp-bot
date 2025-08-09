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

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/whatsapp-bot.git
   cd whatsapp-bot
   ```

2. **Choose your implementation**
   - **MySQL version** → Located in the MySQL project folder.
   - **Firebase version** → Located in the Firebase project folder.

3. **Set up the database**
   - **MySQL**:  
     Run the SQL scripts included in the MySQL project to create the required database and tables.
   - **Firebase**:  
     Create a new Firebase project in the [Firebase Console](https://console.firebase.google.com/), obtain your **Service Account JSON**, and set up the required collections.

4. **Obtain WhatsApp Cloud API credentials**
   - Go to the [Meta for Developers](https://developers.facebook.com/) dashboard.
   - Create or open your **WhatsApp Business App**.
   - Retrieve:
     - **Phone Number ID**
     - **Permanent Access Token**
     - **Verify Token** (you choose this; must match in code and Meta dashboard)
   - Note the **API endpoint** for sending messages.

5. **Configure your application**
   - Add your credentials (Phone Number ID, Access Token, Verify Token, etc.) to the `application.properties` or environment variables for your chosen project.
   - For Firebase: also add the path to your **Service Account JSON**.

6. **Set up ngrok for local testing**
   - [Download ngrok](https://ngrok.com/download) and install it.
   - Run ngrok to expose your Spring Boot port (default is 8080):
     ```bash
     ngrok http 8080
     ```
   - Copy the generated HTTPS URL.

7. **Configure webhook in Meta dashboard**
   - In your WhatsApp app settings, set the **Callback URL** to the ngrok URL + your webhook path (e.g., `https://<ngrok-id>.ngrok.io/webhook`).
   - Enter your **Verify Token** and click **Verify and Save**.

8. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```
   Or build and run the JAR:
   ```bash
   ./mvnw clean package
   java -jar target/whatsapp-bot.jar
   ```

9. **Test the bot**
   - Send a WhatsApp message to the phone number provided in your Meta developer dashboard.
   - The bot should respond according to the appointment booking or medicine ordering flow.
