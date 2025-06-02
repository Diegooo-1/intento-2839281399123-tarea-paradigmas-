package frontend;

import backend.TestManager;
import javax.swing.*;
import java.io.File;

public class TestLoaderPanel extends JPanel {
    private TestManager testManager;

    public TestLoaderPanel(TestManager testManager) {
        this.testManager = testManager;
        setLayout(null);

        JLabel label = new JLabel("Cargar archivo de preguntas (CSV):");
        label.setBounds(50, 50, 300, 30);
        add(label);

        JButton loadButton = new JButton("Cargar Archivo");
        loadButton.setBounds(50, 100, 150, 30);
        add(loadButton);

        JButton startButton = new JButton("Iniciar Prueba");
        startButton.setBounds(220, 100, 150, 30);
        startButton.setEnabled(false);
        add(startButton);

        loadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                testManager.loadTest(file.getAbsolutePath());
                startButton.setEnabled(true);
            }
        });

        startButton.addActionListener(e -> {
            MainFrame frame = (MainFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new TestAdminPanel(testManager));
            frame.revalidate();
            frame.repaint();
        });
    }
}