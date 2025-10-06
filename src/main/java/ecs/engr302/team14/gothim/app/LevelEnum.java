package ecs.engr302.team14.gothim.app;

public enum LevelEnum {
    ONE {
        public String filename() { return "level1"; }

        public LevelEnum nextLevel() {
            return TWO;
        }
    },
    TWO {
        public String filename() { return null; /*to be replaced*/ }

        public LevelEnum nextLevel() { return THREE; }
    },
    THREE {
        public String filename() { return null; /*to be replaced*/ }

        public LevelEnum nextLevel() { return null; }
    };

    private LevelEnum() {}

    public abstract String filename();

    public abstract LevelEnum nextLevel();
}
