package frontend;

import backend.*;
import java.awt.*;
import javax.swing.*;

public class TestAdminPanel extends JPanel {
    private TestManager testManager;
    private JLabel questionLabel;
    private JComponent answerComponent;
    private JButton prevButton, nextButton, submitButton;
    private boolean isReviewMode;

    public TestAdminPanel(TestManager testManager) {
        this.testManager = testManager;
        setLayout(null);

        questionLabel = new JLabel();
        questionLabel.setBounds(50, 50, 500, 30);
        add(questionLabel);

        prevButton = new JButton("Anterior");
        prevButton.setBounds(50, 300, 100, 30);
        prevButton.addActionListener(e -> testManager.previousItem());
        add(prevButton);

        nextButton = new JButton("Siguiente");
        nextButton.setBounds(160, 300, 100, 30);
        nextButton.addActionListener(e -> testManager.nextItem());
        add(nextButton);

        submitButton = new JButton("Enviar respuestas");
        submitButton.setBounds(270, 300, 150, 30);
        submitButton.addActionListener(e -> testManager.calculateResults());
        add(submitButton);
    }

    public void showItem(int index, TestItem item, int totalQuestions, boolean isReviewMode) {
        this.isReviewMode = isReviewMode;
        if (item == null) {
            questionLabel.setText("No hay pregunta disponible");
            return;
        }

        questionLabel.setText((index + 1) + "/" + totalQuestions + ": " + item.getQuestion());

        if (answerComponent != null) {
            remove(answerComponent);
        }

        String userAnswer = testManager.getCurrentUserAnswer();
        System.out.println("Index: " + index + ", User Answer: " + userAnswer + ", Correct Answer: " + item.getCorrectAnswer() + ", Review Mode: " + isReviewMode);

        if (item.getType() == ItemType.MULTIPLE_CHOICE) {
            ButtonGroup group = new ButtonGroup();
            JPanel radioPanel = new JPanel();
            radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
            radioPanel.setBounds(50, 100, 500, 150);
            String[] options = item.getOptions();
            for (String option : options) {
                JRadioButton radio = new JRadioButton(option);
                radio.setEnabled(!isReviewMode);
                group.add(radio);
                radioPanel.add(radio);
                if (userAnswer != null && option.equals(userAnswer)) {
                    radio.setSelected(true);
                    if (isReviewMode) {
                        boolean isCorrect = option.equals(item.getCorrectAnswer());
                        radio.setForeground(isCorrect ? Color.GREEN : Color.RED);
                        radio.setOpaque(true);
                        radio.setBackground(isCorrect ? new Color(200, 255, 200) : new Color(255, 200, 200));
                        System.out.println("Setting color for " + option + ": " + (isCorrect ? "GREEN" : "RED"));
                    }
                }
                if (!isReviewMode) {
                    radio.addActionListener(e -> testManager.setUserAnswer(option));
                }
            }
            answerComponent = radioPanel;
        } else {
            JPanel answerPanel = new JPanel();
            answerPanel.setLayout(null);
            answerPanel.setBounds(50, 100, 250, 50);

            JComboBox<String> comboBox = new JComboBox<>(new String[]{"True", "False"});
            comboBox.setBounds(0, 0, 100, 30);
            comboBox.setEnabled(!isReviewMode);
            if (userAnswer != null) {
                comboBox.setSelectedItem(userAnswer);
            }
            // Asegurar que el ActionListener se active correctamente
            if (!isReviewMode) {
                comboBox.addActionListener(e -> {
                    String selectedAnswer = (String) comboBox.getSelectedItem();
                    testManager.setUserAnswer(selectedAnswer);
                    System.out.println("ComboBox selection saved: " + selectedAnswer + " at index " + index);
                });
            }

            JLabel statusLabel = new JLabel();
            statusLabel.setBounds(110, 0, 100, 30);
            statusLabel.setVisible(isReviewMode && userAnswer != null);
            if (isReviewMode && userAnswer != null) {
                boolean isCorrect = userAnswer.equals(item.getCorrectAnswer());
                statusLabel.setText(isCorrect ? "Correcto" : "Incorrecto");
                statusLabel.setForeground(isCorrect ? Color.GREEN : Color.RED);
                statusLabel.setOpaque(true);
                statusLabel.setBackground(isCorrect ? new Color(200, 255, 200) : new Color(255, 200, 200));
                System.out.println("Setting status for " + userAnswer + ": " + (isCorrect ? "GREEN" : "RED"));
            }

            answerPanel.add(comboBox);
            answerPanel.add(statusLabel);
            answerComponent = answerPanel;
        }

        add(answerComponent);
        prevButton.setEnabled(testManager.hasPrevious());
        nextButton.setEnabled(testManager.hasNext());
        submitButton.setVisible(!isReviewMode);

        answerComponent.revalidate();
        answerComponent.repaint();
        revalidate();
        repaint();
    }
}