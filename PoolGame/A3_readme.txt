The game will start by 'gradle run'. A small flaw is that if player has won the game,
the text “win and bye” will always on the screen. I can’t hide it by load the previous state.

Realized functions:
1. Pockets and More Coloured Balls
2. Difficulty Level
3. Time and Score
4. Save, Load and Cheat

Builder pattern -> More coloured balls is realized by builder pattern. In the Builder folder:
BlackBallBuilder, BrownBallBuilder, GreenBallBuilder, OrangeBallBuilder, PurpleBallBuilder, WhiteBallBuilder, YellowBallBuilder

Observer pattern -> Score. In the Observer folder:
concreteSubject
And update(String u), getUU() in Game.java;
fallIntoPocket(Game game) in Ball.java

Memento patter -> Save and Load. In the Memento folder:
CareTaker, Memento.
And updateScore(String s), updateTime(String t) in Game.java;
setState(ArrayList<String> state), getState(), saveStateToMemento(),
getStateFromMemento(Memento memento), Save() and Load(Game game) in PoolTable.java

Strategy pattern -> If black ball and brown ball fall into pockets (three lives). In the Strategy folder:
PocketThird

Switch levels is easy. Just press the button in the game, which are "Easy", "Normal" and "Hard".

The button "Save" is used to save the state on the table currently, and the button "Load" is to recover the state which the user saved.
If there is no saved state, system will report an error.

Press "Cheat" button to remove all same coloured ball immediately. And will add the corresponding scores.

Javadoc is in the folder: PoolGame/build/docs/javadoc/
Json files are in the folder: PoolGame/src/main/resources/