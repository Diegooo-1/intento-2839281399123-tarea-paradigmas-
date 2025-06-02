package frontend;

import backend.*;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SummaryPanel extends JPanel {
    private TestManager testManager;

    public SummaryPanel(TestManager testManager) {
        this.testManager = testManager;
        setLayout(null);

        JLabel titleLabel = new JLabel("Resumen de Resultados");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBounds(50, 20, 200, 30);
        add(titleLabel);

        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setBounds(50, 60, 500, 200);
        add(resultsArea);

        JButton reviewButton = new JButton("Revisar Respuestas");
        reviewButton.setBounds(50, 280, 150, 30);
        reviewButton.addActionListener(e -> {
            MainFrame frame = (MainFrame) SwingUtilities.getWindowAncestor(this);
            frame.startReview();
        });
        add(reviewButton);
    }

    public void showResults(Map<BloomLevel, Double> bloomPercentages, Map<ItemType, Double> typePercentages) {
        JTextArea resultsArea = (JTextArea) getComponent(1); // Second component is the JTextArea
        StringBuilder results = new StringBuilder("Resultados por Nivel de Bloom:\n");
        for (BloomLevel level : BloomLevel.values()) {
            Double percentage = bloomPercentages.get(level);
            if (percentage != null) {
                results.append(level).append(": ").append(String.format("%.2f%%", percentage)).append("\n");
            }
        }
        results.append("\nResultados por Tipo de Pregunta:\n");
        for (ItemType type : ItemType.values()) {
            Double percentage = typePercentages.get(type);
            if (percentage != null) {
                results.append(type).append(": ").append(String.format("%.2f%%", percentage)).append("\n");
            }
        }
        resultsArea.setText(results.toString());
    }
}