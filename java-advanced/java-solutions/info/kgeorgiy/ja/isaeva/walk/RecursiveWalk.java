package info.kgeorgiy.ja.isaeva.walk;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class RecursiveWalk {
    static final int HASH_SIZE = 64;
    static final String ZERO = "0".repeat(HASH_SIZE);
    static MessageDigest md;
    static {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("NO SUCH ALGORITHM");
        }
    }
    private static String bytesToHex(byte[] hash) {
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(Integer.toHexString((b & 0xff) + 0x100).substring(1));
        }
        return hex.toString();
    }

    public static void writeHash(BufferedWriter writer, String path) throws IOException {
        String str = ZERO;
        try (FileInputStream file = new FileInputStream(path)) {
            byte[] dataBytes = new byte[1024];
            int n;
            while ((n = file.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, n);
            }
            str = bytesToHex(md.digest());
        } catch (FileNotFoundException ignored) {
        } finally {
            writer.write( str + " " + path + System.lineSeparator());
        }
    }

    public static class MyFileVisitor extends SimpleFileVisitor<Path> {
        BufferedWriter writer;
        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
            writeHash(writer, String.valueOf(path));
            return FileVisitResult.CONTINUE;
        }
    }

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.err.println("WRONG NUMBER OF ARGUMENTS");
            return;
        }
        if (args[0] == null) {
            System.err.println("WRONG 1 ARGUMENT");
            return;
        }
        if (args[1] == null) {
            System.err.println("WRONG 2 ARGUMENT");
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(Path.of(args[0]))) {
            Path outputPath = Path.of(args[1]);
            File outputDir = outputPath.toFile().getParentFile();
            if (outputDir != null && outputDir.mkdirs()) {
                System.out.println("Output directories have been created");
            }
            try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
                MyFileVisitor visitor = new MyFileVisitor();
                visitor.writer = writer;
                String path;
                while ((path = reader.readLine()) != null) {
                    try {
                        Path filePath = Paths.get(path);
                        if (Files.isDirectory(filePath)) {
                            try {
                                Files.walkFileTree(filePath, visitor);
                            } catch (NoSuchFileException e) {
                                writer.write(ZERO + " " + path + System.lineSeparator());
                            }
                        } else {
                            writeHash(writer, path);
                        }
                    } catch (InvalidPathException e) {
                        writer.write(ZERO + " " + path + System.lineSeparator());
                    }
                }
            } catch (FileNotFoundException | InvalidPathException e) {
                System.err.println("WRONG 2 ARGUMENT: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("CANNOT WRITE");
            }
        } catch (FileNotFoundException | InvalidPathException e) {
            System.err.println("WRONG 1 ARGUMENT: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("CANNOT READ");
        }
    }
}