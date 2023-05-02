package info.kgeorgiy.ja.isaeva.student;

import info.kgeorgiy.java.advanced.student.*;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDB implements AdvancedQuery {

    private final Comparator<Student> comparatorOfStudents = Comparator
            .comparing(Student::getLastName, Comparator.reverseOrder())
            .thenComparing(Student::getFirstName, Comparator.reverseOrder())
            .thenComparingInt(Student::getId);

    private final Comparator<Group> comparatorOfGroups = Comparator.comparing(Group::getName);
    private final Comparator<Group> comparatorOfGroupsBySize = Comparator
            .comparing((Group group) -> group.getStudents().size())
            .thenComparing(Group::getName);

    private final Comparator<Group> comparatorOfGroupsByDistinctFirstNames = Comparator
            .comparing((Group group) -> group.getStudents().stream().map(Student::getFirstName).collect(Collectors.toSet()).size())
            .thenComparing(Group::getName, Comparator.reverseOrder());


    private <R, C> C getStudentsField(List<Student> students, Function<Student, R> function, Collector<R, ?, C> toColl) {
        return students.stream().map(function).collect(toColl);
    }

    private Stream<Student> getSortedAndFilteredStudent(Collection<Student> students, Comparator<Student> cmp, Predicate<Student> predicate) {
        return students.stream().sorted(cmp).filter(predicate);
    }

    private <C> C getFilteredStudents(Collection<Student> students, Predicate<Student> predicate, Collector<Student, ?, C> toColl) {
        return students.stream().filter(predicate).collect(toColl);
    }



    private <R> Stream<Group> getGroupsByField(Collection<Student> students, Comparator<Student> cmp) {
        return students.stream()
                .sorted(cmp)
                .collect(Collectors.groupingBy(Student::getGroup))
                .entrySet().stream()
                .map(group -> new Group(group.getKey(), group.getValue()));
    }

    private List<Group> getSortedGroups(Collection<Student> students, Comparator<Student> cmpStudents, Comparator<Group> cmpGroups) {
        return getGroupsByField(students, cmpStudents)
                .sorted(cmpGroups)
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
        return getSortedGroups(students, comparatorOfStudents, comparatorOfGroups);
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return getSortedGroups(students, Comparator.comparingInt(Student::getId), comparatorOfGroups);
    }

    @Override
    public GroupName getLargestGroup(Collection<Student> students) {
        return getGroupsByField(students, Comparator.naturalOrder())
                .max(comparatorOfGroupsBySize)
                .map(Group::getName)
                .orElse(null);
    }

    @Override
    public GroupName getLargestGroupFirstName(Collection<Student> students) {
        return getGroupsByField(students, Comparator.naturalOrder())
                .max(comparatorOfGroupsByDistinctFirstNames)
                .map(Group::getName)
                .orElse(null);
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getStudentsField(students, Student::getFirstName, Collectors.toList());
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getStudentsField(students, Student::getLastName, Collectors.toList());
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return getStudentsField(students, Student::getGroup, Collectors.toList());
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getStudentsField(students, student -> student.getFirstName() + " " + student.getLastName(), Collectors.toList());
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return getStudentsField(students, Student::getFirstName, Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream()
                .max(Comparator.comparingInt(Student::getId))
                .map(Student::getFirstName)
                .orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return getSortedAndFilteredStudent(students, Comparator.comparingInt(Student::getId), student -> true)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return getSortedAndFilteredStudent(students, comparatorOfStudents, student -> true)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return getSortedAndFilteredStudent(students, comparatorOfStudents, student -> student.getFirstName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return getSortedAndFilteredStudent(students, comparatorOfStudents, student -> student.getLastName().equals(name))
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return getSortedAndFilteredStudent(students, comparatorOfStudents, student -> student.getGroup().equals(group))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return getFilteredStudents(
                students,
                student -> student.getGroup().equals(group),
                Collectors.toMap(
                        Student::getLastName,
                        Student::getFirstName,
                        BinaryOperator.minBy(Comparator.naturalOrder())));
    }

    @Override
    public String getMostPopularName(Collection<Student> students) {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getFirstName))
                .entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey(),
                        entry.getValue().stream()
                                .map(Student::getGroup)
                                .collect(Collectors.toSet()).size()))
                .max(Comparator.comparingInt(Map.Entry<String, Integer>::getValue)
                        .thenComparing(Map.Entry::getKey, Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .orElse("");
    }

    private <R> List<R> getByIndicies(Collection<Student> students, int[] indices, Function<Student, R> function) {
        return Arrays.stream(indices)
                .mapToObj(ind -> students.stream().collect(Collectors.toMap(Student::getId, Function.identity())).get(ind))
                .filter(Objects::nonNull)
                .map(function)
                .collect(Collectors.toList());
    }
    @Override
    public List<String> getFirstNames(Collection<Student> students, int[] indices) {
        return getByIndicies(students, indices, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(Collection<Student> students, int[] indices) {
        return getByIndicies(students, indices, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(Collection<Student> students, int[] indices) {
        return getByIndicies(students, indices, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(Collection<Student> students, int[] indices) {
        return getByIndicies(students, indices, student -> student.getFirstName() + " " + student.getLastName());
    }
}
