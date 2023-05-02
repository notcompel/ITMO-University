module info.kgeorgiy.ja.isaeva.implementor {
    requires info.kgeorgiy.java.advanced.implementor;
    requires info.kgeorgiy.java.advanced.student;
    requires info.kgeorgiy.java.advanced.arrayset;
    requires java.compiler;
    requires info.kgeorgiy.java.advanced.concurrent;
    requires info.kgeorgiy.java.advanced.mapper;
    requires info.kgeorgiy.java.advanced.crawler;

    exports info.kgeorgiy.ja.isaeva.implementor;

    opens info.kgeorgiy.ja.isaeva.implementor;
}