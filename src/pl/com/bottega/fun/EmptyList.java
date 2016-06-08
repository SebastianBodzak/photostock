package pl.com.bottega.fun;

import java.util.function.*;

/**
 * Created by Dell on 2016-06-04.
 */
public class EmptyList<T> implements FunList<T> {

    @Override
    public FunList<T> add(T el) {
        return new NonEmptyList<>(el);
    }

    @Override
    public FunList<T> remove(T el) {
        return this;
    }

    @Override
    public FunList<T> filter(Predicate<T> predicate) {
        return this;
    }

    @Override
    public void each(Consumer<T> consumer) {

    }

    @Override
    public FunList<T> concat(FunList<T> other) {
        return null;
    }

    @Override
    public FunList<T> sublist(int startIndex, int endIndex) {
        return null;
    }

    @Override
    public boolean contains(T el) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public T find(Predicate<T> predicate) {
        return null;
    }

    @Override
    public T get(int i) {
        return null;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public boolean empty() {
        return true;
    }

    @Override
    public <R> FunList<R> map(Function<T, R> mapper) {
        return new EmptyList<>();
    }

    @Override
    public <R> R reduce(R initial, BiFunction<R, T, R> reductor) {
        return initial;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof FunList && ((FunList) other).empty();
    }
}
