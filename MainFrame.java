import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JPanel cardPanel; // Panel that holds different screens
    private CardLayout cardLayout;
    private DefaultListModel<String> taskListModel;
    
    // Theme Colors
    private Color sidebarColor = new Color(43, 47, 59);
    private Color activePanelColor = Color.WHITE;
    private Color textColor = Color.BLACK;

    public MainFrame() {
        // 1. Basic Window Setup
        setTitle("Personal Productivity Dashboard v2.0");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 2. Sidebar Navigation Panel
        JPanel sidebar = new JPanel();
        sidebar.setBackground(sidebarColor);
        sidebar.setPreferredSize(new Dimension(220, 600));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JLabel titleLabel = new JLabel("WORKSPACE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        sidebar.add(titleLabel);

        // Sidebar Buttons
        JButton btnDashboard = createSidebarButton("Dashboard");
        JButton btnTasks = createSidebarButton("Tasks Tracker");
        JButton btnAnalytics = createSidebarButton("Analytics");
        JButton btnSettings = createSidebarButton("Settings");

        sidebar.add(btnDashboard);
        sidebar.add(btnTasks);
        sidebar.add(btnAnalytics);
        sidebar.add(btnSettings);
        add(sidebar, BorderLayout.WEST);

        // 3. Main Content Panel with CardLayout (Crucial for switching tabs)
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Creating different views
        JPanel dashboardPanel = createWelcomePanel();
        JPanel tasksPanel = createTasksPanel();
        JPanel analyticsPanel = createAnalyticsPanel();
        JPanel settingsPanel = createSettingsPanel();

        // Adding views to the card container
        cardPanel.add(dashboardPanel, "Dashboard");
        cardPanel.add(tasksPanel, "Tasks");
        cardPanel.add(analyticsPanel, "Analytics");
        cardPanel.add(settingsPanel, "Settings");

        add(cardPanel, BorderLayout.CENTER);

        // 4. Button Action Listeners for switching tabs
        btnDashboard.addActionListener(e -> cardLayout.show(cardPanel, "Dashboard"));
        btnTasks.addActionListener(e -> cardLayout.show(cardPanel, "Tasks"));
        btnAnalytics.addActionListener(e -> {
            // Refresh stats before showing analytics
            cardPanel.remove(analyticsPanel);
            cardPanel.add(createAnalyticsPanel(), "Analytics");
            cardLayout.show(cardPanel, "Analytics");
        });
        btnSettings.addActionListener(e -> cardLayout.show(cardPanel, "Settings"));
    }

    // Helper to create modern styled sidebar buttons
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(190, 40));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // TAB 1: Welcome / Dashboard Overview
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(activePanelColor);
        JLabel label = new JLabel("<html><center><h1>Welcome to Your Productivity Suite</h1><p>Select 'Tasks Tracker' from the sidebar to manage your day.</p></center></html>");
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(textColor);
        panel.add(label);
        return panel;
    }

    // TAB 2: The Core Task Management System
    private JPanel createTasksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(activePanelColor);

        // Header
        JLabel headerLabel = new JLabel(" Quick Overview / Task Tracker", JLabel.LEFT);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        headerLabel.setForeground(textColor);
        panel.add(headerLabel, BorderLayout.NORTH);

        // Task List
        taskListModel = new DefaultListModel<>();
        if(taskListModel.isEmpty()) {
            taskListModel.addElement("Review Class 12 Computer Science Syllabus");
            taskListModel.addElement("Optimize Meta Ads Strategy for Zappify Store");
            taskListModel.addElement("Check Persian Kitten's Hydration & Diet Plan");
        }
        
        JList<String> taskList = new JList<>(taskListModel);
        taskList.setFont(new Font("Arial", Font.PLAIN, 16));
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Controls Panel
        JPanel controlsPanel = new JPanel(new BorderLayout(10, 10));
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        controlsPanel.setOpaque(false);

        JTextField inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel buttonGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonGroup.setOpaque(false);

        JButton btnAdd = new JButton("Add Task");
        JButton btnRemove = new JButton("Remove Selected");
        btnAdd.setFont(new Font("Arial", Font.PLAIN, 14));
        btnRemove.setFont(new Font("Arial", Font.PLAIN, 14));

        buttonGroup.add(btnAdd);
        buttonGroup.add(btnRemove);

        controlsPanel.add(new JLabel("New Task: "), BorderLayout.WEST);
        controlsPanel.add(inputField, BorderLayout.CENTER);
        controlsPanel.add(buttonGroup, BorderLayout.EAST);
        panel.add(controlsPanel, BorderLayout.SOUTH);

        // Logic
        btnAdd.addActionListener(e -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                taskListModel.addElement(text);
                inputField.setText("");
            }
        });

        btnRemove.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                taskListModel.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to remove!", "Tip", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    // TAB 3: Advanced Analytics Panel (Calculates dynamically)
    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(activePanelColor);
        
        int totalTasks = (taskListModel != null) ? taskListModel.size() : 3;
        
        String statsText = "<html><center><h1 style='color:#2B2F3B;'>📊 Workspace Analytics</h1><br>" +
                           "<p style='font-size:14px;'>Total Active Tasks Pending: <b>" + totalTasks + "</b></p><br>" +
                           "<p style='font-size:12px; color:gray;'>Keep grinding! Complete your tasks to clear the board.</p></center></html>";
        
        JLabel statsLabel = new JLabel(statsText);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(statsLabel);
        return panel;
    }

    // TAB 4: Settings / UI Customization Panel
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 50));
        panel.setBackground(activePanelColor);

        JLabel themeLabel = new JLabel("Choose Workspace Theme: ");
        themeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        themeLabel.setForeground(textColor);

        JButton btnDark = new JButton("Classic Dark Sidebar");
        JButton btnBlue = new JButton("Ocean Blue Sidebar");

        panel.add(themeLabel);
        panel.add(btnDark);
        panel.add(btnBlue);

        // Theme switching engine
        btnDark.addActionListener(e -> {
            sidebarColor = new Color(43, 47, 59);
            JOptionPane.showMessageDialog(this, "Sidebar set to Dark! Restart App to apply fully.", "Theme Updated", JOptionPane.INFORMATION_MESSAGE);
        });

        btnBlue.addActionListener(e -> {
            sidebarColor = new Color(24, 116, 205);
            JOptionPane.showMessageDialog(this, "Sidebar set to Ocean Blue! Restart App to apply fully.", "Theme Updated", JOptionPane.INFORMATION_MESSAGE);
        });

        return panel;
    }
}