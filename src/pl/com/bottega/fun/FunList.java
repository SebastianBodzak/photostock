package pl.com.bottega.fun;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by Dell on 2016-06-04.
 */
public interface FunList<T> {
    FunList<T> add(T el);
    FunList<T> remove(T el);
    boolean contains(T el);
    int size();
    T find(Predicate<T> predicate);
    T get(int i);
    String toString();
    boolean empty();
    <R> FunList<R> map(Function<T, R> mapper);
    static <T> FunList<T> create() {
        return new EmptyList<>();
    }
}
