package me.zjls.bedwars.games;

public enum GameState {

    PRELOBBY, LOBBY, STARTING, ACTIVE, WON, RESET;

    public String getName(){
        switch (this){
            case PRELOBBY:
                return "pre大厅";
            case LOBBY:
                return "大厅";
            case STARTING:
                return "等待中";
            case ACTIVE:
                return "开始";
            case WON:
                return "胜利";
            case RESET:
                return "重置";
        }
        return "";
    }

}
