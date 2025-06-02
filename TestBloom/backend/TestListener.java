package backend;

import java.util.Map;

public interface TestListener {
    void onTestLoaded(int questionCount, int totalTime);
    void onItemChanged(int index, TestItem item, String userAnswer);
    void onResultsCalculated(Map<BloomLevel, Double> bloomPercentages, Map<ItemType, Double> typePercentages);
    void onError(String message);
}