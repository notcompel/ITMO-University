package info.kgeorgiy.ja.isaeva.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * This class generates implementation for given class or interface
 */
public class Implementor implements JarImpler {

    /**
     * The system-dependent line separator which used to separate lines in a string.
     */
    final String SEPARATOR = System.lineSeparator();

    /**
     * The suffix that is appended to the name of the original class or interface
     */
    final String JAVA_SUFFIX = "Impl.java";

    /**
     * The suffix that is appended to the name of the original compiled class or interface
     */
    final String ClASS_SUFFIX = "Impl.class";

    /**
     * Main function to run {@code Implementor} program which generates implementation for given class or interface.
     * Can be run in two modes:
     * <ol>
     *     <li> two-argument mode: generates implementation of given class or interface in given directory
     *     </li>
     *     <li> generates implementation of given class or interface in given <var>.jar</var> file
     *     using {@link #implementJar(Class, Path)}
     *     </li>
     * </ol>
     *
     * @param args arguments passed from command line
     * @throws ImplerException when invalid arguments are passed or class/interface cannot be found
     */
    public static void main(String[] args) throws ImplerException {
        if (args.length < 1) {
            throw new ImplerException("Wrong arguments");
        }

        Class<?> token;
        try {
            if (args[0].equals("-jar")) {
                token = Class.forName(args[1]);
            } else {
                token = Class.forName(args[0]);
            }
        } catch (ClassNotFoundException e) {
            throw new ImplerException("Class not found " + Arrays.toString(args));
        }
        final JarImpler generator = new Implementor();
        if (args[0].equals("-jar")) {
            Path jarFile = Path.of(args[2]);
            generator.implementJar(token, jarFile);
        } else {
            Path root = Path.of(args[1]);
            generator.implement(token, root);
        }
    }

    /**
     * Generates a java-file with the implementation of the given class or interface
     *
     * @param token the {@link Class} or interface token to be implemented
     * @param root  the {@link Path} of root directory
     * @throws ImplerException if the given token cannot be implemented
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        if (token.isPrimitive() ||
                token == Enum.class ||
                Modifier.isFinal(token.getModifiers()) ||
                Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("Can not implement class or interface");
        }
        if (!token.isInterface() && Arrays.stream(token.getDeclaredConstructors()).filter(constructor ->
                !Modifier.isPrivate(constructor.getModifiers())).toList().isEmpty()) {
            throw new ImplerException("Can not implement class or interface with no public constructors");
        }
        try {
            Path filePath = root.resolve(
                    Path.of(token.getPackageName().replace('.', '\\'),
                            token.getSimpleName() + JAVA_SUFFIX)
            );
            Files.createDirectories(filePath.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                StringBuilder sb = new StringBuilder();
                writeHead(token, sb);
                sb.append("{").append(SEPARATOR);
                writeBody(token, sb);
                sb.append("}");
                writer.write(new String(sb.toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new ImplerException("Can not write to file");
        }
    }

    /**
     * Searches for a method with the same name, return type, and parameters as the given method in the given set of methods.
     *
     * @param methods {@link ArrayList} of methods to search in
     * @param method  {@link Method} to find an equivalent of
     * @return {@code true} if such a method exists in the set of methods, {@code false} otherwise
     */
    boolean findMethod(ArrayList<Method> methods, Method method) {
        for (Method other : methods) {
            if (Arrays.equals(other.getParameters(), method.getParameters())
                    && other.getName().equals(method.getName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns a list of unique methods from a given list of methods.
     * Unique methods are grouping by name and parameter types, and then selecting
     * the method with the most specific return type for each name-parameter pair.
     *
     * @param methods {@link ArrayList} of methods to extract unique methods from
     * @return {@link List} of unique methods
     */
    private List<Method> getUniqueMethods(ArrayList<Method> methods) {
        List<Method> uniqMethods;
        Comparator<Method> myComparator = (m1, m2) -> {
            if (m1.equals(m2)) {
                return 0;
            }
            if (m2.getReturnType().isAssignableFrom(m1.getReturnType())) {
                return -1;
            }
            return 1;
        };
        Map<String, List<Method>> tmp = methods.stream()
                .collect(
                        Collectors.groupingBy(method -> method.getName() + Arrays.toString(method.getParameterTypes()),
                                Collectors.toList())
                );
        uniqMethods = tmp.entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey(),
                        entry.getValue().stream().min(myComparator).get()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).values().stream().toList();

        return uniqMethods;
    }

    /**
     * Generates the body of the implementation of the given class or interface.
     * Writes to the given {@link StringBuilder} the implementation of every abstract
     * method declared in the given class or interface, and every constructor.
     *
     * @param token the {@link Class} or interface to generate the implementation for
     * @param sb    the {@link StringBuilder} to write the implementation to
     * @throws IOException if an I/O error occurs while writing to the writer
     */
    private void writeBody(Class<?> token, StringBuilder sb) throws IOException {
        ArrayList<Method> methods = new ArrayList<>(Arrays.asList(token.getDeclaredMethods()));
        methods.addAll(Arrays.asList(token.getMethods()));

        HashSet<Constructor<?>> constructors = new HashSet<>(Arrays.asList(token.getDeclaredConstructors()));
        constructors.addAll(Arrays.asList(token.getConstructors()));

        Class<?> parent = token.getSuperclass();
        while (parent != null) {
            methods.addAll(Arrays.stream(parent.getDeclaredMethods())
                    .filter(method -> Modifier.isAbstract(method.getModifiers())
                            && !Modifier.isPublic(method.getModifiers())
                            && !findMethod(methods, method))
                    .toList());
            parent = parent.getSuperclass();
        }

        List<Method> uniqMethods = getUniqueMethods(methods);
        for (Method method : uniqMethods) {
            writeMethod(method, sb);
        }

        for (Constructor<?> constructor : constructors) {
            writeConstructor(constructor, sb);
        }
    }

    /**
     * Generates the beginning of the implementation file for the given class or interface.
     *
     * @param token the {@link Class} or interface to generate the implementation for
     * @param sb    the {@link StringBuilder} to write the implementation to
     * @throws IOException if an I/O error occurs while writing to the writer
     */
    private void writeHead(Class<?> token, StringBuilder sb) throws IOException {
        String str =
                // :NOTE: не то что нужно
                (token.getPackage() == null ? "" : "package " + token.getPackageName() + ";" + SEPARATOR) +
                        "public class " +
                        token.getSimpleName() +
                        "Impl" +
                        (token.isInterface() ? " implements " : " extends ") +
                        token.getCanonicalName();
        sb.append(str);
    }

    /**
     * Generates the implementation for the given abstract {@link Method}.
     * If the {@link Method} is not abstract, returns without doing anything.
     *
     * @param method the abstract {@link Method} to generate the implementation for
     * @param sb     the {@link StringBuilder} to write the implementation to
     */
    private void writeMethod(Method method, StringBuilder sb) {
        if (!Modifier.isAbstract(method.getModifiers())) {
            return;
        }
        // :NOTE: что такое трансиент?
        sb.append(Modifier.toString(method.getModifiers() & ~Modifier.ABSTRACT & Modifier.methodModifiers()))
                .append(" ")
                .append(method.getReturnType().getCanonicalName())
                .append(" ")
                .append(method.getName());

        writeParameters(method, sb);
        writeExceptions(method, sb);
        sb.append(" {return ").append(generateReturn(method.getReturnType())).append(";}").append(SEPARATOR);
    }

    /**
     * Generates the implementation for the given {@link Constructor}.
     * If the {@link Constructor} is private, nothing will be written.
     * Finally, the string includes a super call, passing in the constructor's parameters.
     *
     * @param constructor the {@link Constructor} to generate the implementation for
     * @param sb          the {@link StringBuilder} to write the implementation to
     */
    private void writeConstructor(Constructor<?> constructor, StringBuilder sb) {
        if (Modifier.isPrivate(constructor.getModifiers())) {
            return;
        }
        sb.append(Modifier.toString(constructor.getModifiers() & ~Modifier.ABSTRACT & Modifier.constructorModifiers()));
        sb.append(" ").append(constructor.getDeclaringClass().getSimpleName()).append("Impl ");
        StringBuilder args = new StringBuilder();
        writeParameters(constructor, sb);
        Parameter[] parameters = constructor.getParameters();
        for (int i = 0; i < parameters.length; ++i) {
            args.append(parameters[i].getName());
            if (i < parameters.length - 1) {
                args.append(", ");
            }
        }
        writeExceptions(constructor, sb);
        sb.append("{super(").append(args).append(");}").append(SEPARATOR);
    }

    /**
     * Generates a default return value of the same type as the given return {@link Class} type.
     *
     * @param returnType the return type to generate a default value for
     * @return a default value of the same type as the given return type, or null if the type is not recognized
     */
    private String generateReturn(Class<?> returnType) {
        if (returnType.isAssignableFrom(Object.class)) {
            return null;
        } else if (returnType.equals(boolean.class)) {
            return "false";
        } else if (returnType.equals(void.class)) {
            return "";
        } else if (returnType.isPrimitive()) {
            return "0";
        }
        return null;
    }

    /**
     * Generates the string with parameters of the given executable and with their names to the given writer.
     *
     * @param executable the {@link Executable} to describe the parameters of
     * @param sb         the {@link StringBuilder} to write the parameter description to
     */
    private void writeParameters(Executable executable, StringBuilder sb) {
        sb.append("(");
        Parameter[] parameters = executable.getParameters();
        for (int i = 0; i < parameters.length; ++i) {
            sb.append(parameters[i].getType().getCanonicalName()).append(" ").append(parameters[i].getName());
            if (i < parameters.length - 1) sb.append(", ");
        }
        sb.append(")");
    }

    /**
     * Generate a string describing the exceptions that may be thrown by the given executable to the given writer.
     * If the executable does not throw any exceptions, nothing will be written.
     *
     * @param executable the {@link Executable} to describe the possible exceptions of
     * @param sb         the {@link StringBuilder} to write the exception description to
     */
    private void writeExceptions(Executable executable, StringBuilder sb) {
        Class<?>[] exceptions = executable.getExceptionTypes();
        if (exceptions.length != 0) {
            sb.append(" throws ");
            for (int i = 0; i < exceptions.length; ++i) {
                sb.append(exceptions[i].getCanonicalName());
                if (i < exceptions.length - 1) {
                    sb.append(", ");
                }
            }
        }
    }


    // JAR

    /**
     * Implements a <var>.jar</var> file for the given class token.
     *
     * @param token   the {@link Class} token to implement and package into a <var>.jar</var> file
     * @param jarFile the {@link Path} at which to save the output <var>.jar</var> file
     * @throws ImplerException if the implementation, compilation, or packaging fails
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        Path dir = Paths.get("tmp");
        try {
            Files.createDirectories(jarFile.getParent());
            implement(token, dir);
            compile(token, dir);
            makeJar(token, jarFile, dir);
        } catch (IOException e) {
            throw new ImplerException("Can not create directories for jar file");
        }
    }

    /**
     * Compiles the implemented class {@code token} located in the temporary directory and saves it at the same directory.
     *
     * @param token the {@link Class} for which to compile the implemented class file
     * @param dir   the {@link Path} of directory where the implemented class file is located
     * @throws ImplerException if the compilation fails
     */
    private void compile(Class<?> token, Path dir) throws ImplerException {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new ImplerException("Could not find java compiler, include tools.jar to classpath");
        }
        final String classPathName = Path.of(dir.toString(), makeCorrectPath(token) + JAVA_SUFFIX)
                .toString();

        final String[] args = {"-encoding", StandardCharsets.UTF_8.name(), classPathName, "-cp",
                dir + File.pathSeparator + getClassPath(token)};
        final int exitCode = compiler.run(null, null, null, args);
        if (exitCode != 0) {
            throw new ImplerException("Compiler exit code " + exitCode);
        }
    }

    /**
     * Generates a path with {@code File.separatorChar}.
     *
     * @param token the {@link Class} object for which to generate the path
     * @return the correct {@link Path}
     */
    private Path makeCorrectPath(Class<?> token) {
        return Path.of(token.getPackageName().
                replace('.', File.separatorChar), token.getSimpleName());
    }

    /**
     * Returns {@link Class} file path
     *
     * @param token the {@link Class} whose location is needed
     * @return the absolute {@link Path}
     */
    private static String getClassPath(Class<?> token) {
        try {
            return Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (final URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Creates a jar file containing the implementation of the given class and saves it at the specified location.
     *
     * @param token   the {@link Class} whose implementation is to be saved in the jar file
     * @param jarFile the {@link Path} at which to save the output <var>.jar</var> file
     * @param root    the {@link Path} of directory where the implemented class file is located
     * @throws ImplerException if an error occurs while creating the jar file
     */
    private void makeJar(Class<?> token, Path jarFile, Path root) throws ImplerException {
        String classpath = makeCorrectPath(token) + ClASS_SUFFIX;
        Path location = root.resolve(classpath);
        try (JarOutputStream writer = new JarOutputStream(Files.newOutputStream(jarFile))) {
            writer.putNextEntry(new ZipEntry(classpath.replace(File.separatorChar, '/')));
            Files.copy(location, writer);
        } catch (IOException e) {
            throw new ImplerException("Can not make jar file");
        }
    }
}
