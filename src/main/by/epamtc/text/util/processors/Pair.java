package by.epamtc.text.util.processors;

public class Pair<U, V> {

    private U first;
    private V second;

    public Pair() {
    }

    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    public U getFirst() {
        return first;
    }

    public void setFirst(U first) {
        this.first = first;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = hashCode * 31 + ((first != null) ? first.hashCode() : 0);
        hashCode = hashCode * 31 + ((second != null) ? second.hashCode() : 0);
        return hashCode;
    }

    @SuppressWarnings("EqualsReplaceableByObjectsCall")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return ((first == pair.first) || (first != null && first.equals(pair.first))) &&
               ((second == pair.second) || (second != null && second.equals(pair.second)));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@{" + "first=" + first + ", second=" + second + '}';
    }
}
