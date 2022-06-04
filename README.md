# WebPoker
CSE3310 UTA Spring 2022 Project

Full-Stack web-based Poker game for up to 5 players. Developed using Java for server and game logic, and JavaScript for client HTML and UI manipulation. Implemented using Multithreading and a WebSocket connection for multiplayer, in game timer, and browser-based gameplay. 

Up to 5 player Five Card Draw Poker. If a player is joining after the game has started or there are 5 or more players they will be put into a queue.
Can simulate multiplayer by opening multiple tabs of the game.

### From the command line (works in Visual Studio Code as well):
```bash
cd WebPoker
mvn clean
mvn compile
mvn package
mvn exec:java -Dexec.mainClass=uta.cse3310.WebPoker
```
### point a webbrowser to 127.0.0.1:8085/index.html

![Game1](https://user-images.githubusercontent.com/76676640/171970546-e15ace63-046a-4115-969b-c4cfaba880cc.PNG)
![Game2](https://user-images.githubusercontent.com/76676640/171970547-97cf4c0c-f273-4930-a8a3-9473b80c5d1c.PNG)
![Game3](https://user-images.githubusercontent.com/76676640/171970552-cd7990d4-a270-4e19-b31e-c481e2cce5da.PNG)

Where are the jarfiles?
```bash
% find ~/ -name "*.jar"
```

https://www.me.uk/cards/makeadeck.cgi
