package org.gradle.wrapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Small transparent bootstrap used only because the execution environment that produced this archive could not
 * download Gradle's official wrapper JAR. Running the Gradle `wrapper` task replaces this JAR with the official one.
 */
public final class GradleWrapperMain {
  private GradleWrapperMain() {}

  public static void main(String[] args) throws Exception {
    Path projectRoot = locateProjectRoot();
    Properties properties = loadProperties(projectRoot);
    String distributionUrl = require(properties, "distributionUrl");
    String expectedSha = properties.getProperty("distributionSha256Sum", "").trim();
    String fileName = Path.of(URI.create(distributionUrl).getPath()).getFileName().toString();
    String directoryName = fileName.replace("-bin.zip", "").replace("-all.zip", "");
    Path cacheRoot = Path.of(System.getProperty("user.home"), ".gradle", "wrapper", "dists", directoryName);
    Path distributionHome = cacheRoot.resolve(directoryName);

    if (!Files.isDirectory(distributionHome)) {
      Files.createDirectories(cacheRoot);
      Path zip = cacheRoot.resolve(fileName);
      if (!Files.exists(zip)) downloadFollowingRedirects(distributionUrl, zip);
      if (!expectedSha.isEmpty()) verifySha256(zip, expectedSha);
      unzip(zip, cacheRoot);
    }

    boolean windows = System.getProperty("os.name", "").toLowerCase().contains("win");
    Path launcher = distributionHome.resolve("bin").resolve(windows ? "gradle.bat" : "gradle");
    if (!Files.exists(launcher)) throw new IllegalStateException("Gradle launcher not found: " + launcher);

    String[] command = new String[args.length + (windows ? 1 : 2)];
    int offset = 0;
    if (windows) {
      command[offset++] = launcher.toString();
    } else {
      command[offset++] = "sh";
      command[offset++] = launcher.toString();
    }
    System.arraycopy(args, 0, command, offset, args.length);
    Process process = new ProcessBuilder(command).directory(projectRoot.toFile()).inheritIO().start();
    System.exit(process.waitFor());
  }

  private static Path locateProjectRoot() throws Exception {
    Path jar = Path.of(GradleWrapperMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    Path wrapperDir = jar.getParent();
    return wrapperDir.getParent().getParent();
  }

  private static Properties loadProperties(Path projectRoot) throws IOException {
    Properties properties = new Properties();
    try (InputStream input = Files.newInputStream(projectRoot.resolve("gradle/wrapper/gradle-wrapper.properties"))) {
      properties.load(input);
    }
    return properties;
  }

  private static String require(Properties properties, String name) {
    String value = properties.getProperty(name);
    if (value == null || value.isBlank()) throw new IllegalStateException("Missing wrapper property: " + name);
    return value;
  }

  private static void downloadFollowingRedirects(String source, Path target) throws IOException {
    URL current = URI.create(source).toURL();
    for (int redirects = 0; redirects < 10; redirects++) {
      HttpURLConnection connection = (HttpURLConnection) current.openConnection();
      connection.setInstanceFollowRedirects(false);
      connection.setConnectTimeout(10_000);
      connection.setReadTimeout(60_000);
      connection.setRequestProperty("User-Agent", "reynolds-boids-gradle-bootstrap/0.4.0");
      int status = connection.getResponseCode();
      if (status >= 300 && status < 400) {
        String location = connection.getHeaderField("Location");
        if (location == null) throw new IOException("Redirect without Location from " + current);
        current = URI.create(current.toString()).resolve(location).toURL();
        continue;
      }
      if (status < 200 || status >= 300) throw new IOException("HTTP " + status + " downloading " + current);
      try (InputStream input = connection.getInputStream()) {
        Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING);
      }
      return;
    }
    throw new IOException("Too many redirects downloading Gradle distribution");
  }

  private static void verifySha256(Path file, String expected) throws Exception {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    try (InputStream input = Files.newInputStream(file)) {
      byte[] buffer = new byte[64 * 1024];
      for (int read; (read = input.read(buffer)) >= 0; ) digest.update(buffer, 0, read);
    }
    String actual = HexFormat.of().formatHex(digest.digest());
    if (!actual.equalsIgnoreCase(expected)) {
      Files.deleteIfExists(file);
      throw new SecurityException("Gradle distribution checksum mismatch. Expected " + expected + " but got " + actual);
    }
  }

  private static void unzip(Path archive, Path destination) throws IOException {
    try (ZipInputStream zip = new ZipInputStream(Files.newInputStream(archive))) {
      for (ZipEntry entry; (entry = zip.getNextEntry()) != null; ) {
        Path output = destination.resolve(entry.getName()).normalize();
        if (!output.startsWith(destination)) throw new IOException("Unsafe ZIP entry: " + entry.getName());
        if (entry.isDirectory()) Files.createDirectories(output);
        else {
          Files.createDirectories(output.getParent());
          Files.copy(zip, output, StandardCopyOption.REPLACE_EXISTING);
        }
        zip.closeEntry();
      }
    }
  }
}
