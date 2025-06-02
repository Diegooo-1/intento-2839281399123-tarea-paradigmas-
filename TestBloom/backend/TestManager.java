package backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestManager {
    private List<TestItem> items;
    private List<String> userAnswers;
    private TestListener listener;
    private int currentIndex;

    public TestManager() {
        items = new ArrayList<>();
        userAnswers = new ArrayList<>();
        currentIndex = 0;
    }

    public void setListener(TestListener listener) {
        this.listener = listener;
    }

    public void loadTest(String filePath) {
        items.clear();
        userAnswers.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length != 6) {
                    throw new IllegalArgumentException("Formato de CSV inválido: " + line);
                }
                String question = parts[0].trim();
                ItemType type = ItemType.valueOf(parts[1].trim());
                String[] options = parts[2].isEmpty() ? new String[0] : parts[2].split(";");
                String correctAnswer = parts[3].trim();
                BloomLevel bloomLevel = BloomLevel.valueOf(parts[4].trim());
                int time = Integer.parseInt(parts[5].trim());
                items.add(new TestItem(question, type, options, correctAnswer, bloomLevel, time));
                userAnswers.add(null);
            }
            if (listener != null) {
                listener.onTestLoaded(items.size(), items.stream().mapToInt(TestItem::getTime).sum());
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onError("Error al cargar el archivo: " + e.getMessage());
            }
        }
    }

    public TestItem getCurrentItem() {
        if (currentIndex >= 0 && currentIndex < items.size()) {
            return items.get(currentIndex);
        }
        return null;
    }

    public String getCurrentUserAnswer() {
        if (currentIndex >= 0 && currentIndex < userAnswers.size()) {
            return userAnswers.get(currentIndex);
        }
        return null;
    }

    public void setUserAnswer(String answer) {
        if (currentIndex >= 0 && currentIndex < userAnswers.size()) {
            userAnswers.set(currentIndex, answer);
        }
    }

    public boolean hasPrevious() {
        return currentIndex > 0;
    }

    public boolean hasNext() {
        return currentIndex < items.size() - 1;
    }

    public void previousItem() {
        if (hasPrevious()) {
            currentIndex--;
            if (listener != null) {
                listener.onItemChanged(currentIndex, getCurrentItem(), getCurrentUserAnswer());
            }
        }
    }

    public void nextItem() {
        if (hasNext()) {
            currentIndex++;
            if (listener != null) {
                listener.onItemChanged(currentIndex, getCurrentItem(), getCurrentUserAnswer());
            }
        }
    }

    public void calculateResults() {
        Map<BloomLevel, Integer> correctByBloom = new HashMap<>();
        Map<ItemType, Integer> correctByType = new HashMap<>();
        Map<BloomLevel, Integer> totalByBloom = new HashMap<>();
        Map<ItemType, Integer> totalByType = new HashMap<>();
        Map<BloomLevel, Double> bloomPercentages = new HashMap<>();
        Map<ItemType, Double> typePercentages = new HashMap<>();

        for (int i = 0; i < items.size(); i++) {
            TestItem item = items.get(i);
            String userAnswer = userAnswers.get(i);
            BloomLevel level = item.getBloomLevel();
            ItemType type = item.getType();
            totalByBloom.put(level, totalByBloom.getOrDefault(level, 0) + 1);
            totalByType.put(type, totalByType.getOrDefault(type, 0) + 1);
            if (userAnswer != null && userAnswer.equals(item.getCorrectAnswer())) {
                correctByBloom.put(level, correctByBloom.getOrDefault(level, 0) + 1);
                correctByType.put(type, correctByType.getOrDefault(type, 0) + 1);
            }
        }

        for (BloomLevel level : BloomLevel.values()) {
            int total = totalByBloom.getOrDefault(level, 0);
            if (total > 0) {
                bloomPercentages.put(level, (double) correctByBloom.getOrDefault(level, 0) / total * 100);
            }
        }

        for (ItemType type : ItemType.values()) {
            int total = totalByType.getOrDefault(type, 0);
            if (total > 0) {
                typePercentages.put(type, (double) correctByType.getOrDefault(type, 0) / total * 100);
            }
        }

        if (listener != null) {
            listener.onResultsCalculated(bloomPercentages, typePercentages);
        }
    }

    public void startReview() {
        currentIndex = 0;
        if (listener != null && !items.isEmpty()) {
            listener.onItemChanged(currentIndex, getCurrentItem(), getCurrentUserAnswer());
        }
    }
}