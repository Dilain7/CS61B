import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        assertEquals(2050 - CompoundInterest.THIS_YEAR, CompoundInterest.numYears(2050));
        assertEquals(0, CompoundInterest.numYears(2019));

        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2021), tolerance);
        assertEquals(11, CompoundInterest.futureValue(10, 10, 2020), tolerance);
        assertEquals(11.8098, CompoundInterest.futureValue(20, -10, 2024), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(11.8026496, CompoundInterest.futureValueReal(10, 12, 2021, 3), tolerance);
        assertEquals(138.2999736, CompoundInterest.futureValueReal(100, 10, 2024, 3), tolerance);
        assertEquals(36.55125, CompoundInterest.futureValueReal(50, -10, 2021, 5), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2021, 10), tolerance);
        assertEquals(34390, CompoundInterest.totalSavings(10000, 2022, -10), tolerance);


    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(15894.62, CompoundInterest.totalSavingsReal(5000, 2021, 10, 2), tolerance);
        assertEquals(31386.82447, CompoundInterest.totalSavingsReal(10000, 2022, -10, 3), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
