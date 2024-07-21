# Wizard

## Table of Contents

- [Screenshots](#screenshots)
- [Description](#description)
- [What I've learned](#what-ive-learned)
- [How to use](#how-to-use)

## Screenshots

### Getting the curency balance
<p>
  <img style="max-width: 100%; height: auto;" alt="währung info sn" src="https://github.com/user-attachments/assets/d4c9a805-4b5c-48c2-bd0a-b522b1b69886">
</p>

### Performing currency operations

<p>
  <img style="max-width: 100%; height: auto;" alt="währung konfiszieren" src="https://github.com/user-attachments/assets/1e389a85-5b86-4470-94da-8ec7a28c908c">
</p>

### Earning currency
<p>
  <img style="max-width: 100%; height: auto;" alt="währung message counter" src="https://github.com/user-attachments/assets/5eca9a23-806c-4bbe-aaf0-9a19be22afb5">

</p>

### Freezing user's currency balance
<p>
  <img style="max-width: 100%; height: auto;" alt="währung einfrieren" src="https://github.com/user-attachments/assets/0c3f51b6-bae1-41dc-b308-757411d8f05c">
</p>

### Getting the list of inactive members
<p>
  <img style="max-width: 100%; height: auto;" alt="Tagesbericht " src="https://github.com/user-attachments/assets/0141216a-844a-42be-99ee-475b2a51ecf5">
</p>

## Description
- a discord bot that tracks activity of members/students in a private server, using currency system
- users earn currency by chatting and participating in voice chats
- users pay taxes each day to improve motivation to stay active in the server
- admins can specify text channels for currency system and perform currency operations, including un- / freezing user's balance. In this case the user's balance is "immune" to the daily taxes, reports and isn't able to earn the currency. 
- a daily report about the inactive users, whose currency balance equals 0 or less, can be sent in a specified text channel
- the ui is in German

### Tech Stack
- Discord4j
- Project Reactor
- Spring Framework (JDBC)
- PostgreSQL


## What I've learned
- how to interact with Discord API using Project Reactor & Discord4J
- how to perform scheduled tasks in spring
- using a map of beans-interface-imlementations from Spring Application Context to get rid of hardcoding (multiple ifs) and changing the code, in the case when new interface implementations are added to the map

## How to use
- Go to [the discord developer portal]([http://www.example.com](https://discord.com/developers)) register there and get there a discord token for the bot. The bot needs Admin scopes.
- clone the project
- create .env file in the root of the project and configure there environment variables, including the token **WIZARD_TOKEN** based on the docker-compose file in the project. **DATASOURCE_HOST** and **DATASOURCE_PORT** are the database container name and its port in the docker compose file. You can change **services.app** config in the file to use a local **Dockerfile** in the project's directory
- build the project with **gradle :bootJar**
- launch the project with **docker-compose up**

