import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main extends JFrame {

    private JTextField urlField;
    private JComboBox<String> typeBox;
    private JButton downloadBtn;
    private JLabel statusLabel;

    public Main() {
        setTitle("Video/Audio Downloader");
        setSize(500, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        urlField = new JTextField();
        urlField.setPreferredSize(new Dimension(450, 30));
        urlField.setToolTipText("Paste video or audio URL here");

        typeBox = new JComboBox<>(new String[]{"Video", "Audio"});

        downloadBtn = new JButton("Download");
        statusLabel = new JLabel(" ");

        downloadBtn.addActionListener(this::onDownload);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        panel.add(urlField);
        panel.add(typeBox);
        panel.add(downloadBtn);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(panel, BorderLayout.CENTER);
        container.add(statusLabel, BorderLayout.SOUTH);
    }

    private void onDownload(ActionEvent e) {
        String url = urlField.getText();
        String type = (String) typeBox.getSelectedItem();

        if (url == null || url.isEmpty()) {
            statusLabel.setText("Please enter a URL");
            return;
        }

        statusLabel.setText("Downloading...");
        downloadBtn.setEnabled(false);

        new Thread(() -> {
            try {
                String ytDlpPath = "lib/yt-dlp.exe";
                String output = "downloads/%(title)s.%(ext)s";
                String[] args = type.equals("Audio")
                        ? new String[]{"--extract-audio", "--audio-format", "mp3"}
                        : new String[]{"-f", "best"};

                Downloader.download(ytDlpPath, url, output, args);
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Download completed");
                    downloadBtn.setEnabled(true);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Error: " + ex.getMessage());
                    downloadBtn.setEnabled(true);
                });
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}


