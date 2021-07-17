package me.zjls.bedwars.worlds.generators;

public enum GeneratorTier {

    ONE, TWO, THREE;

    public String getName() {
        switch (this) {
            case ONE:
                return "I";
            case TWO:
                return "II";
            case THREE:
                return "III";
        }
        return "";
    }
}
