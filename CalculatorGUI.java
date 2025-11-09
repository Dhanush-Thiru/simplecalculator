import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorGUI extends JFrame implements ActionListener {

    // --- Components ---
    private final JTextField display;
    private double firstOperand = 0;
    private String operator = null;
    private boolean startNewNumber = true;

    // Button labels for a standard calculator layout
    private final String[] buttonLabels = {
        "7", "8", "9", "/",
        "4", "5", "6", "*",
        "1", "2", "3", "-",
        "0", ".", "=", "+"
    };

    public CalculatorGUI() {
        // --- Frame Setup ---
        setTitle("Java Swing Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Use a BorderLayout for the main frame (display top, buttons bottom)
        setLayout(new BorderLayout(5, 5));

        // --- Display Setup (North) ---
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 36));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        add(display, BorderLayout.NORTH);

        // --- Clear Button Setup (Top Center) ---
        JButton clearButton = new JButton("C");
        clearButton.setFont(new Font("Arial", Font.BOLD, 24));
        clearButton.setBackground(new Color(255, 102, 102)); // Red color
        clearButton.setForeground(Color.WHITE);
        clearButton.setActionCommand("C");
        clearButton.addActionListener(this);

        JPanel clearPanel = new JPanel(new BorderLayout());
        clearPanel.add(clearButton, BorderLayout.CENTER);

        // --- Button Panel Setup (Center) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        // Add 4x4 grid buttons
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 24));
            button.addActionListener(this);

            // Set different colors for operators and the equals button
            if (label.matches("[+\\-*/]")) {
                button.setBackground(new Color(153, 204, 255)); // Light blue for operators
            } else if (label.equals("=")) {
                button.setBackground(new Color(51, 153, 255)); // Darker blue for equals
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(Color.LIGHT_GRAY); // Grey for numbers and decimal
            }
            buttonPanel.add(button);
        }

        // Combine clear button and 4x4 grid in the center
        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.add(clearPanel, BorderLayout.NORTH);
        mainContentPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainContentPanel, BorderLayout.CENTER);

        // --- Finalize and Display ---
        pack(); // Sizes the frame based on component sizes
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("[0-9]")) {
            // Handle number buttons
            if (startNewNumber) {
                display.setText(command);
                startNewNumber = false;
            } else {
                display.setText(display.getText() + command);
            }
        } else if (command.equals(".")) {
            // Handle decimal point
            if (startNewNumber) {
                display.setText("0.");
                startNewNumber = false;
            } else if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
        } else if (command.matches("[+\\-*/]")) {
            // Handle operator buttons
            if (operator != null && !startNewNumber) {
                // If an operator is already set and a number has been entered, calculate the result
                calculate();
            }
            operator = command;
            firstOperand = Double.parseDouble(display.getText());
            startNewNumber = true;
        } else if (command.equals("=")) {
            // Handle equals button
            if (operator != null && !startNewNumber) {
                calculate();
                operator = null; // Reset operator after calculation
            }
        } else if (command.equals("C")) {
            // Handle clear button
            display.setText("0");
            firstOperand = 0;
            operator = null;
            startNewNumber = true;
        }
    }

    private void calculate() {
        try {
            double secondOperand = Double.parseDouble(display.getText());
            double result = 0;

            switch (operator) {
                case "+":
                    result = firstOperand + secondOperand;
                    break;
                case "-":
                    result = firstOperand - secondOperand;
                    break;
                case "*":
                    result = firstOperand * secondOperand;
                    break;
                case "/":
                    if (secondOperand == 0) {
                        display.setText("Error: Div by 0");
                        firstOperand = 0;
                        operator = null;
                        startNewNumber = true;
                        return;
                    }
                    result = firstOperand / secondOperand;
                    break;
            }

            // Display result, use Integer if result is a whole number for a cleaner look
            if (result == (long) result) {
                display.setText(String.format("%d", (long) result));
            } else {
                display.setText(String.valueOf(result));
            }

            firstOperand = result;
            startNewNumber = true;
        } catch (NumberFormatException ex) {
            display.setText("Error");
            firstOperand = 0;
            operator = null;
            startNewNumber = true;
        }
    }

    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(CalculatorGUI::new);
    }
}
