package xyz.fluxinc.moddedadditions.storage;

public class ResultContainer<T> {

    private final T result;

    public ResultContainer(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }
}
