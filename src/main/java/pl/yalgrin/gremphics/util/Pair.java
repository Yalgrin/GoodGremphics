package pl.yalgrin.gremphics.util;

public class Pair<A, B> {
    private A a;
    private B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair<?, ?>) {
            Pair<?, ?> pair = (Pair<?, ?>) obj;
            return a.equals(pair.getA()) && b.equals(pair.getB());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return a.hashCode() + b.hashCode();
    }

    @Override
    public String toString() {
        return "(" + a.toString() + ", " + b.toString() + ")";
    }

}
