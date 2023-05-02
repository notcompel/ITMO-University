package info.kgeorgiy.ja.isaeva.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ParallelMapperImpl implements ParallelMapper {
    private final List<Thread> threads;
    private final SynchronizedQueue tasks = new SynchronizedQueue();

    public ParallelMapperImpl(final int threads) {
        this.threads = Stream.generate(this::makeTread).limit(threads).toList();
        for (final Thread thread : this.threads) {
            thread.start();
        }
    }

    private Thread makeTread() {
        return new Thread(() -> {
            while (!Thread.interrupted()) {
                final Runnable task;
                try {
                    task = tasks.getTask();
                } catch (InterruptedException e) {
                    return;
                }
                task.run();
            }
        });
    }

    private static class SynchronizedQueue {
        private final Queue<Runnable> tasks;
        public SynchronizedQueue() {
            tasks = new ArrayDeque<>();
        }

        synchronized public Runnable getTask() throws InterruptedException {
            while (tasks.isEmpty()) {
                wait();
            }
            return tasks.poll();
        }

        synchronized public void add(Runnable task) {
            tasks.add(task);
            notify();
        }
    }


    private static class WaitingList<R> {
        List<R> data;
        int realSize = 0;

        public WaitingList(final int requiredSize) {
            data = new ArrayList<>(Collections.nCopies(requiredSize, null));
        }

        synchronized public void set(final int i, final R value) {
            data.set(i, value);
            realSize++;
            notify();
        }

        synchronized public List<R> getData() throws InterruptedException {
            while (data.size() != realSize) {
                wait();
            }
            return data;
        }
    }

    private static class ExceptionList {
        List<RuntimeException> data;

        public ExceptionList() {
            data = new ArrayList<>();
        }

        synchronized public void addException(RuntimeException e) {
            data.add(e);
        }

        synchronized public void throwResultException() {
            if (data.size() != 0) {
                RuntimeException result = data.get(0);
                for (int i = 1; i < data.size(); i++) {
                    result.addSuppressed(data.get(i));
                }
                throw result;
            }
        }
    }

    @Override
    public <T, R> List<R> map(final Function<? super T, ? extends R> f, final List<? extends T> args) throws InterruptedException {
        final WaitingList<R> result = new WaitingList<>(args.size());
        final ExceptionList exceptionList = new ExceptionList();

        IntStream.range(0, args.size()).forEach((i) -> {
            final Runnable task = () -> {
                try {
                    result.set(i, f.apply(args.get(i)));
                } catch (RuntimeException e) {
                    exceptionList.addException(e);
                }
            };
            tasks.add(task);
        });
        exceptionList.throwResultException();
        return result.getData();
    }

    @Override
    public void close() {
        for (final Thread thread : threads) {
            thread.interrupt();
        }
        for (final Thread thread : threads) {
            try {
                thread.join();
            } catch (final InterruptedException ignored) {

            }
        }
    }
}
