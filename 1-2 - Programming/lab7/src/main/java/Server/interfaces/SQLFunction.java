package Server.interfaces;

@FunctionalInterface
public interface SQLFunction<T, R> {
    R apply(T t) throws Exception;
}