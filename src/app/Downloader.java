package app;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Downloader {

    public static void download(String ytDlpPath, String url, String output, String[] extraArgs) throws IOException, InterruptedException {
        File file = new File(ytDlpPath);
        if (!file.exists()) throw new FileNotFoundException("yt-dlp not found: " + ytDlpPath);

        List<String> command = new ArrayList<>();
        command.add(ytDlpPath);
        command.add("-o");
        command.add(output);
        command.addAll(Arrays.asList(extraArgs));
        command.add(url);

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[yt-dlp] " + line);
            }
        }

        int exit = process.waitFor();
        if (exit != 0) throw new RuntimeException("yt-dlp exited with code " + exit);
    }
}
