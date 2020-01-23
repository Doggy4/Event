package Commands;

import Game.GameCycle;

public class StartEvent {

    public static int secPreStart = 60;
    public static boolean isGameTimerStarted = false;
    public static boolean isGameStarted = false;

    public static void startEvent() {
        secPreStart = 60;
        isGameStarted = false;
        isGameTimerStarted = true;
    }
}
