package frontend;

import backend.*;
import javax.swing.*;
import java.util.Map;

public class MainFrame extends JFrame implements TestListener {
    private TestManager testManager;
    private TestLoaderPanel loaderPanel;
    private TestAdminPanel adminPanel;
    private SummaryPanel summaryPanel;
    private boolean isReviewMode;

    public MainFrame() {
        setTitle("Sistema de Pruebas - Taxonomía de Bloom");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        testManager = new TestManager();
        testManager.setListener(this);
        loaderPanel = new TestLoaderPanel(testManager);
        adminPanel = new TestAdminPanel(testManager);
        summaryPanel = new SummaryPanel(testManager);

        setContentPane(loaderPanel);
    }

    @Override
    public void onTestLoaded(int questionCount, int totalTime) {
        setContentPane(adminPanel);
        adminPanel.showItem(0, testManager.getCurrentItem(), questionCount, false);
        revalidate();
        repaint();
        JOptionPane.showMessageDialog(this, "Preguntas: " + questionCount + ", Tiempo total estimado: " + totalTime + " segundos");
    }

    @Override
    public void onItemChanged(int index, TestItem item, String userAnswer) {
        adminPanel.showItem(index, item, testManager.getCurrentItem() != null ? testManager.getCurrentItem().getQuestion().length() : 0, isReviewMode);
    }

    @Override
    public void onResultsCalculated(Map<BloomLevel, Double> bloomPercentages, Map<ItemType, Double> typePercentages) {
        setContentPane(summaryPanel);
        summaryPanel.showResults(bloomPercentages, typePercentages);
        revalidate();
        repaint();
    }

    @Override
    public void onError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void startReview() {
        isReviewMode = true;
        setContentPane(adminPanel);
        adminPanel.showItem(0, testManager.getCurrentItem(), testManager.getCurrentItem() != null ? testManager.getCurrentItem().getQuestion().length() : 0, true);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}