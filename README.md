# 🧠 FocusMind StudyFlow

[![GitHub stars](https://img.shields.io/github/stars/Musk-co/StudyFlow?style=social)](https://github.com/Musk-co/StudyFlow/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/Musk-co/StudyFlow?style=social)](https://github.com/Musk-co/StudyFlow/network/members)
[![MIT License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

A cognitive load-aware study planner web application that helps users balance mental fatigue and task difficulty using a rule-based decision engine and AI-powered chat assistant.

---

## ✨ Features

- 📅 **Dynamic Study Schedule**: Adapts session durations based on cognitive load and user-defined rules.
- 😴 **Fatigue Tracking**: Emoji-based slider for intuitive fatigue input.
- 📈 **Visual Analytics**: Fatigue heatmaps and study distribution charts (Chart.js).
- ⚖️ **Rule-Based Adaptation**: Custom rules to adjust session durations.
- 📝 **Session Logging**: Log study sessions with task type, difficulty, fatigue, and notes.
- ⚡ **Quick Stats**: Real-time stats for today's sessions, average fatigue, and active rules.
- 🕒 **Recent Sessions**: Preview and interact with your latest study sessions.
- 🤖 **AI Chat Assistant**: Gemini-powered chat for study advice, session analysis, and Q&A. Start a chat about any recent session with a single click.
- 📱 **Responsive UI**: Modern, mobile-friendly design using Bootstrap 5.
- ❗ **Error Handling**: Friendly error pages and robust validation.

---

## 🛠️ Technologies Used

- ☕ Java (JDK 17+)
- 🧰 Maven
- 🖥️ Servlets & JSP
- 🛢️ MySQL
- 📊 Chart.js
- 🎨 Bootstrap 5
- 🤖 Google Gemini API (AI chat)
- 🔄 Gson (JSON processing)

---

## 📁 Project Structure

```
StudyFlow/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── model/         # Data models (StudySession, AdaptRule)
│   │   │   ├── dao/           # DAO interfaces
│   │   │   ├── dao/impl/      # DAO implementations
│   │   │   ├── servlet/       # Servlets (Logger, Dashboard, Rule, Chat)
│   │   │   └── util/          # Utilities (DBUtil, RuleEngine)
│   │   ├── resources/         # Config files
│   │   └── webapp/            # Web resources
│   │       ├── WEB-INF/
│   │       ├── static/        # CSS, JS, images
│   │       ├── error/         # Error pages
│   │       └── *.jsp          # JSP views
├── pom.xml                    # Maven config
├── README.md                  # Project documentation
└── sql/
    └── create_tables.sql      # Database schema
```

---

## 🚦 Setup Instructions

### Prerequisites

- ☕ JDK 17 or higher
- 🧰 Maven 3.6+
- 🛢️ MySQL 8.0+
- 🖥️ Tomcat 9.0+

---

### 🗄️ Database Setup

1. Create the database and tables:
   ```sh
   mysql -u root -p < sql/create_tables.sql
   ```
2. Update DB credentials in `src/main/java/util/DBUtil.java` if needed.

---

### ⚙️ Build & Deploy

1. Build the project:
   ```sh
   mvn clean package
   ```
2. Deploy the generated WAR file to your Tomcat server.

---

### 🤖 Gemini API Setup

- The Gemini API key is already integrated in the code for demo purposes. For production, use environment variables or a secure config.

---

## 🚀 Usage

- **Home Page**: Log new study sessions, view quick stats, recent sessions, and chat with the AI assistant.
- **Dashboard**: Visualize fatigue trends and study distribution.
- **Rules**: Create, edit, and toggle adaptation rules.
- **Chat**: Use the chat widget (bottom right) for study help, or click a recent session to start a session-specific chat.

---

## 💬 About the AI Chat

- Powered by Google Gemini API.
- You can ask for study tips, session analysis, or discuss any recent session.
- Click a session in "Recent Sessions" to start a context-aware chat about it.

---

## 📸 Screenshots

**Home**  
![Home](https://github.com/user-attachments/assets/a000e467-1b50-4932-9fe6-58d129ee4c9d)

**Dashboard** 

![Rules](https://github.com/user-attachments/assets/111e768b-aae2-44b2-8f37-c9194291ba0a)

**Rules**  
 
![Dashboard](https://github.com/user-attachments/assets/13005b2f-d0b6-40dc-be0f-5da9207138f4)

---

## 🤝 Contributing

1. Fork the repository  
2. Create a feature branch  
3. Commit your changes  
4. Push to your branch  
5. Create a Pull Request

---

## 📄 License

This project is licensed under the MIT License.

---

⭐️ If you like this project, consider [starring it!](https://github.com/Musk-co/StudyFlow)  
🔗 [Fork it to build your own study planner!](https://github.com/Musk-co/StudyFlow/fork)
