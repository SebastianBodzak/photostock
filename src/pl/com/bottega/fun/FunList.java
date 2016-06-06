package pl.com.bottega.fun;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by Dell on 2016-06-04.
 */
public interface FunList<T> {
    FunList<T> add(T el);
    boolean contains(T el);
    int size();
    T find(Predicate<T> predicate);
    T get(int i);
    String toString();
    boolean empty();
    <R> FunList<R> map(Function<T, R> mapper);
    <R> R reduce(R initial, BiFunction<R, T, R> reductor);

    //// TODO: 2016-06-05
    FunList<T> remove(T el);

    //Find all elements on the list matching predicates
    FunList<T> filter(Predicate<T> predicate); //musi zwrócić wszystkie elementy listy

    //Iterates over all elements and calls consumer on each elements
    void each(Consumer<T> consumer);

    //Concatenates list with the other list
    FunList<T> concat(FunList<T> other);

    //*returns sublist of elements starting at startIndex and ending at endIndex (inclusive)
    FunList<T> sublist(int startIndex, int endIndex);

    static <T> FunList<T> create() {
        return new EmptyList<>();
    }
}
