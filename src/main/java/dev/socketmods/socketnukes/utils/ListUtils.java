package dev.socketmods.socketnukes.utils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ListUtils {

    public static <E> boolean removeIf(List<E> list, Predicate<? super E> filter, Consumer<E> callback) {
        boolean removed = false;

        final Iterator<E> each = list.iterator();
        while (each.hasNext()) {
            E element = each.next();
            if (filter.test(element)) {
                each.remove();
                callback.accept(element);
                removed = true;
            }
        }

        return removed;
    }

}
