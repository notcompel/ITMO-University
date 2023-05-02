package info.kgeorgiy.ja.isaeva.concurrent;

import info.kgeorgiy.java.advanced.concurrent.AdvancedIP;
import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.concurrent.ScalarIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IterativeParallelism implements AdvancedIP {
    ParallelMapper mapper;

    public IterativeParallelism(ParallelMapper mapper) {
        this.mapper = mapper;
    }
    public IterativeParallelism() {
        mapper = null;
    }

    private <T, R> List<R> threadsResult(int threads, List<T> values, Function<List<T>, R> func) throws InterruptedException {
        threads = Math.min(threads, values.size());
        int size = values.size() / threads;
        int rest = values.size() % threads;
        List<Thread> thrds = new ArrayList<>();
        List<List<T>> tasks = new ArrayList<>();
        List<R> result = new ArrayList<>(Collections.nCopies(threads, null));


        for (int i = 0, indThread = 0; i < values.size(); i += size, indThread++) {
            int start = i;
            int end = start + size;
            if (rest > 0) {
                rest--;
                end++;
                i++;
            }
            tasks.add(values.subList(start, Math.min(end, values.size())));
        }
        if (mapper != null) {
            return mapper.map(func, tasks);
        }
        for (int i = 0; i < tasks.size(); i++) {
            int finalI = i;
            thrds.add(new Thread(() -> result.set(finalI, func.apply(tasks.get(finalI)))));
        }
        for (int i = 0; i < threads; ++i) {
            thrds.get(i).start();
        }
        for (int i = 0; i < threads; ++i) {
            thrds.get(i).join();
        }
        return result;
    }

    @Override
    public String join(int threads, List<?> values) throws InterruptedException {
        return String.join("", threadsResult(
                threads,
                values,
                list -> list.stream().map(Object::toString).collect(Collectors.joining())
        ));
    }

    @Override
    public <T> List<T> filter(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        List<T> result = new ArrayList<>();
        List<List<? extends T>> threadResult = threadsResult(threads, values, list -> list.stream().filter(predicate).toList());
        for (List<? extends T> list : threadResult) {
            result.addAll(list);
        }
        return result;
    }

    @Override
    public <T, U> List<U> map(int threads, List<? extends T> values, Function<? super T, ? extends U> f) throws InterruptedException {
        List<U> result = new ArrayList<>();
        List<List<? extends U>> threadResult = threadsResult(threads, values, list -> list.stream().map(f).toList());
        for (List<? extends U> list : threadResult) {
            result.addAll(list);
        }
        return result;
    }


    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return threadsResult(
                threads,
                values,
                list -> list.stream().max(comparator).orElse(null)
        ).stream().max(comparator).orElse(null);
    }

    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return maximum(threads, values, comparator.reversed());
    }

    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return threadsResult(
                threads,
                values,
                list -> list.stream().allMatch(predicate)
        ).stream().allMatch(x -> x);
    }

    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return !all(threads, values, predicate.negate());
    }

    @Override
    public <T> int count(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return threadsResult(
                threads,
                values,
                list -> list.stream().filter(predicate).toList().size()
        ).stream().mapToInt(i -> i).sum();
    }

    @Override
    public <T> T reduce(int threads, List<T> values, Monoid<T> monoid) throws InterruptedException {
        return threadsResult(
                threads,
                values,
                list -> list.stream().reduce(monoid.getIdentity(), monoid.getOperator())
        ).stream().reduce(monoid.getIdentity(), monoid.getOperator());
    }

    @Override
    public <T, R> R mapReduce(int threads, List<T> values, Function<T, R> lift, Monoid<R> monoid) throws InterruptedException {
        return threadsResult(
                threads,
                values,
                list -> list.stream().map(lift).reduce(monoid.getIdentity(), monoid.getOperator())
        ).stream().reduce(monoid.getIdentity(), monoid.getOperator());
    }
}
