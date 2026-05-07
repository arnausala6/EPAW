package epaw.lab2.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Opens the current file in the browser at localhost:8080.
 * Supports HTML/JSP files (under webapp) and Java Servlets (via @WebServlet).
 *
 * Usage: pass the absolute path to the file as the first argument.
 */
public class OpenBrowser {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Error: Missing file path.");
            System.exit(1);
        }

        String filePath = args[0];
        String url = resolveUrl(filePath);

        if (url != null) {
            System.out.println("Opening browser: " + url);
            openInBrowser(url);
        } else {
            System.err.println("Could not map the file to a server URL.");
            System.exit(1);
        }

        // Short delay before the JVM exits so Desktop.browse is not dropped on Linux.
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }

    private static String resolveUrl(String filePath) {
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        String normalizedPath = path.toString().replace('\\', '/');
        String lowerPath = normalizedPath.toLowerCase();

        int webappIdx = lowerPath.lastIndexOf("/src/main/webapp");
        if (webappIdx != -1) {
            String relative = normalizedPath.substring(webappIdx + "/src/main/webapp".length());
            return "http://localhost:8080" + relative;
        }

        int externalIdx = lowerPath.lastIndexOf("/external_resources");
        if (externalIdx != -1) {
            String relative = normalizedPath.substring(externalIdx + "/external_resources".length());
            return "http://localhost:8080" + relative;
        }

        if (lowerPath.startsWith("src/main/webapp")) {
            return "http://localhost:8080/" + normalizedPath.substring("src/main/webapp/".length());
        }
        if (lowerPath.startsWith("external_resources")) {
            return "http://localhost:8080/" + normalizedPath.substring("external_resources/".length());
        }

        if (normalizedPath.toLowerCase().endsWith(".java")) {
            try {
                String content = Files.readString(path, java.nio.charset.StandardCharsets.UTF_8);

                Pattern pattern = Pattern.compile(
                        "@WebServlet\\s*\\(\\s*(?:(?:value|urlPatterns)\\s*=\\s*)?[\"'](.*?)[\"']",
                        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher matcher = pattern.matcher(content);

                if (matcher.find()) {
                    String servletPath = matcher.group(1);
                    if (!servletPath.startsWith("/")) {
                        servletPath = "/" + servletPath;
                    }
                    return "http://localhost:8080" + servletPath;
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        }

        return null;
    }

    private static void openInBrowser(String url) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                if (java.awt.Desktop.isDesktopSupported()
                        && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                } else {
                    Runtime.getRuntime().exec(new String[] { "rundll32", "url.dll,FileProtocolHandler", url });
                }
            } else if (os.contains("mac")) {
                if (java.awt.Desktop.isDesktopSupported()
                        && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
                } else {
                    Runtime.getRuntime().exec(new String[] { "open", url });
                }
            } else {
                // VS Code may kill child processes when the task ends; setsid avoids that on Linux.
                ProcessBuilder pb = new ProcessBuilder("setsid", "xdg-open", url);
                pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
                pb.redirectError(ProcessBuilder.Redirect.DISCARD);
                pb.start();
            }
        } catch (Exception e) {
            System.err.println("Could not open browser: " + e.getMessage());
        }
    }
}
