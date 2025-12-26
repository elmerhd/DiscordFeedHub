# 🚀 DiscordFeedHub

**DiscordFeedHub** is a lightweight Java desktop application that monitors RSS feeds from websites and automatically pushes new content to Discord channels using webhooks.

It’s designed to be **simple, local, and self-hosted** — no external hosting required.

---

## ✨ Features

- 📡 Monitor multiple RSS feeds (TechCrunch, Hacker News, etc.)
- 🔔 Send new feed items directly to Discord via webhooks
- 🗃️ SQLite database for persistent feed storage
- 🖥️ Swing-based desktop UI
- 📊 JTable view of RSS sources
- 🔄 Add, update, and delete RSS sources
- 🧠 Duplicate check (RSS URL + Discord Webhook URL)
- 🎨 Smooth UI animations using **Universal Tween Engine**

---

## 🧩 Tech Stack

- **Java (Swing)**
- **SQLite** (local database)
- **ROME** – RSS/Atom feed parsing
- **Universal Tween Engine** – UI animations
- **JDBC** – database access
- **Discord Webhooks** – message delivery

---

## 📝 TODO

### ⏳ Background Monitoring
- Periodically check RSS feeds without user interaction  
- Run checks on a timer or scheduler  
- Allow start/stop monitoring from the UI  

### 🔔 Webhook Enhancements
- Improve Discord embed formatting  
- Add support for rich embeds (author, footer, thumbnail)  
- Optional mentions (e.g. `@everyone`, role pings)  

### 🧠 Improved Duplicate Checking
- Track last published date or GUID per feed  
- Store last sent item in the database  
- Prevent reposting old items after application restart  
