package backend;

public class TestItem {
    private String question;
    private ItemType type;
    private String[] options;
    private String correctAnswer;
    private BloomLevel bloomLevel;
    private int time;

    public TestItem(String question, ItemType type, String[] options, String correctAnswer, BloomLevel bloomLevel, int time) {
        this.question = question;
        this.type = type;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.bloomLevel = bloomLevel;
        this.time = time;
    }

    public String getQuestion() {
        return question;
    }

    public ItemType getType() {
        return type;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public BloomLevel getBloomLevel() {
        return bloomLevel;
    }

    public int getTime() {
        return time;
    }
}