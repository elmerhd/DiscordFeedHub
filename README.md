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

## ☕ Requirements

- **[Java 25](https://www.oracle.com/java/technologies/javase/jdk25-archive-downloads.html) (or newer)**
- Internet connection (for RSS feeds & Discord webhooks)

### Verify Java Version
```bash
java -version
```
---

## 📝 TODO

### UI
- [x] Add other look and feel
- [ ] Improve notifications

### ⏳ Background Monitoring
- [x] Periodically check RSS feeds without user interaction  
- [x] Run checks on a timer or scheduler  
- [x] Allow start/stop monitoring from the UI
- [x] Add Configuration settings for scheduler
- [x] Add logs viewer

### 🔔 Webhook Enhancements
- [ ] Improve Discord embed formatting  
- [ ] Add support for rich embeds (author, footer, thumbnail)  
- [ ] Optional mentions (e.g. `@everyone`, role pings)  

### 🧠 Improved Duplicate Checking
- [x] Track last published date or GUID per feed  
- [x] Store last sent item in the database  
- [x] Prevent reposting old items after application restart