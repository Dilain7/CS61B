public class Summation implements IntUnaryFunction {
    private int count = 0;

    public Summation(int p) {
        this.count = p;
    }

    public int apply(int k) {
        this.count += k;
        return k;


    }
    public int countResult(int i) {
        return this.count;
    }


}
