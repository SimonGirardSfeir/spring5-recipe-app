package guru.springframework.domain;

public enum Difficulty {

    EASY("Easy"), MODERATE("Moderate"), HARD("Hard");

    private final String description;

    Difficulty(String str) {
        this.description = str;
    }

    public String getDescription() {
        return description;
    }
}
