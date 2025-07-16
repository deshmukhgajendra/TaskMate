# 📋 TaskMate – Your Personal Daily Task Scheduler

**TaskMate** is a modern task management Android app built as part of an assessment challenge. It helps users effortlessly schedule and manage their daily routine with a clean UI, offline support, real-time syncing, and overdue task reminders.

---

## ✨ Features

- ✅ Add, update, and delete tasks
- 📌 Prioritize tasks (High, Medium, Low)
- 🔔 Overdue task notifications
- 📶 Offline-first support with Room DB
- 🔄 Auto sync tasks with REST API when online
- 🌙 Toggle between Light and Dark Mode
- 📅 Built-in Date Picker & Priority Dropdown
- 🔕 Push notifications using WorkManager

---

## 🧰 Tech Stack Used

| Tool/Library        | Purpose                                                       |
|---------------------|---------------------------------------------------------------|
| **Android Studio**  | IDE for Android development                                   |
| **Kotlin**          | Primary language for development                              |
| **Jetpack Compose** | Declarative UI framework                                      |
| **Room Database**   | Local database for offline support                            |
| **Retrofit**        | REST API calls (using [MockAPI.io](https://mockapi.io/))      |
| **Dagger-Hilt**     | Dependency Injection framework                                |
| **WorkManager**     | Schedule sync & overdue task notifications                    |
| **Notification API**| Local notifications                                           |
| **MVVM Architecture** | Clean separation of UI, data, and logic                      |

---

## 📱 App UI Preview

| Home Screen | Add Task | Overdue Task |
|-------------|----------|--------------|
| ![img](https://github.com/user-attachments/assets/c8cf997f-0620-4733-a00f-41132d5f2c7c) | ![img](https://github.com/user-attachments/assets/493f1d78-14b2-4b6d-9b41-e60d91661ead) | ![img](https://github.com/user-attachments/assets/28c8b767-e87b-4dfa-8c9f-de2fe28dce6e) |

| Calendar Picker | Priority Menu | Notification |
|-----------------|---------------|--------------|
| ![img](https://github.com/user-attachments/assets/86c431dd-7e96-45fe-a195-35a40899c757) | ![img](https://github.com/user-attachments/assets/f040c978-07ef-48db-bc5c-5fb3945776c1) | ![img](https://github.com/user-attachments/assets/3bc0086c-8a42-4fbd-809f-324ec27a3892) |

| Dark Mode | Light Mode | Sectioned Tasks |
|-----------|-------------|-----------------|
| ![img](https://github.com/user-attachments/assets/2441231b-2184-428d-9f74-f12e83ccd68b) | ![img](https://github.com/user-attachments/assets/b8e390ee-cab3-44d4-8e55-a51267d013ff) | ![img](https://github.com/user-attachments/assets/680b5680-f38b-4846-af01-9ad89b8fbd37) |

---

## 📸 Extra Screenshots

|                           |                           |
|---------------------------|---------------------------|
| ![img](https://github.com/user-attachments/assets/2066c089-b6cb-4bda-b5b0-1d08c4a6be0f) | ![img](https://github.com/user-attachments/assets/6849e374-e838-4d9a-9551-d164d4ed376a) |
| ![img](https://github.com/user-attachments/assets/47f52423-8d55-4a28-8c4a-294273c8f834) | ![img](https://github.com/user-attachments/assets/fea136ec-f7de-4f56-b3c3-444e244cce2b) |

---

## 🛠 How to Run Locally

1. Clone the repository  
   ```bash
   git clone https://github.com/your-username/TaskMate.git
