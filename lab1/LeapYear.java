public class LeapYear {
	int year = 2000;
	public static boolean isLeapYear(int year) {
		if (year % 100 == 0 && year % 400 != 0) {
			return false;
		}
		else if (year % 4 == 0) {
			return true;
		}
		else {
			return false;
		}
	}

	private static void checkingLeapYear(int year) {
		if (isLeapYear(year)) {
			System.out.printf("%x is a leap year.\n", year);
		} else {
			System.out.printf("%x is not a leap year.\n", year);
		}
	}
}