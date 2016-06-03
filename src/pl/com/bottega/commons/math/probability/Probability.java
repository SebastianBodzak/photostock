package pl.com.bottega.commons.math.probability;

/**
 * Created by Dell on 2016-04-16.
 */
public class Probability {
//    public enum ProbabilityRepresentation{
//        PERC, FRAC;
//    }

    private double value; //wartość procentowa

    private Probability(double value) {
        this.value = value;
    }

    public Probability and(Probability v2) {
        return new Probability(value * v2.value);
    }

    public static Probability fromFraction(double value) {
        if (value < 0 || value >1)
            throw new IllegalArgumentException("value must be <0, 1>");
        return new Probability(value);
    }

    public static Probability fromPercentage(double value) {
        if (value < 0 || value > 100)
            throw new IllegalArgumentException("value must be <0, 100>");
        return new Probability(value / 100);
    }

    @Override
    public boolean equals(Object o) {
        double delta = 0.00001;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Probability that = (Probability) o;

        return (Math.abs(value - that.value) < delta);

    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }

    @Override
    public String toString(){
        return Double.toString(value);
    }
}
