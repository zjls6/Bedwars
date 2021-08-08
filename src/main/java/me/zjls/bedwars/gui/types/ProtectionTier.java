package me.zjls.bedwars.gui.types;

public enum ProtectionTier {

    Default,
    ONE,
    TWO,
    THREE,
    FOUR;

    public String getName() {
        switch (this) {
            case ONE:
                return "I";
            case TWO:
                return "II";
            case THREE:
                return "III";
            case FOUR:
                return "IV";
        }
        return "";
    }
}
