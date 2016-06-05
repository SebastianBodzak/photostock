package pl.com.bottega.fun;

import java.util.function.Predicate;

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
}
