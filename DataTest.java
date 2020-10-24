public class DataTest {

	int p1, p2, p3;
	boolean pp1, pp2, pp3;
	String part1, part2, part3;

	public boolean input(String f) {

		int pont = 0;
		for (int i = 0; i < f.length(); i++) {
			if (f.charAt(i) == '.') {
				pont++;
			}
		}

		if (pont != 2) {
			return false;
		}

		try {
			String[] parts = f.split("\\.");
			part1 = parts[0];
			part2 = parts[1];
			part3 = parts[2];
		} catch (Exception e) {
			return false;
		}

		try {
			p1 = Integer.parseInt(part1);
			pp1 = p1v();
		} catch (Exception e) {
		}

		try {
			p2 = Integer.parseInt(part2);
			pp2 = p2v();
		} catch (Exception e) {
		}

		try {
			p3 = Integer.parseInt(part3);
			pp3 = p3v();
		} catch (Exception e) {
		}

		return pp1 && pp2 && pp3;

	}

	private boolean p1v() {

		if (p1 > 0 && p1 < 32) {
			return true;
		} else {
			return false;
		}
	}

	private boolean p2v() {

		if (p2 > 0 && p2 < 13) {
			return true;
		} else {
			return false;
		}
	}

	private boolean p3v() {

		if (p3 >= 2020 && p3 <= 2099) {
			return true;
		} else {
			return false;
		}
	}
}
