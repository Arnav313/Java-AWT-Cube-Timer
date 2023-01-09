package scramblegenerator;

import java.security.SecureRandom;

public class Scramble {
	private int cube;

	private char[] notations = new char[] { 'R', 'L', 'U', 'D', 'F', 'B' };

	private boolean[] faceDisrupted = new boolean[] { true, true, true, true, true, true };

	private String[] suffix = new String[] { "", "2", "'", "w", "w'", "w2" };

	private int length;

	private StringBuilder scramble;

	SecureRandom random;

	public Scramble(int n, int size) {
		length = n;
		cube = size;
		scramble = new StringBuilder();
		random = new SecureRandom();
	}

	private void addToScramble(int index) {
		int k;
		scramble.append(notations[index]);
		if (cube <= 3) {
			k = random.nextInt(3);
		} else {
			k = random.nextInt(6);
		}
		if (k != 0)
			scramble.append(suffix[k]);
		scramble.append(' ');
		faceDisrupted[index] = false;
		for (int i = 0; i < 3; i++) {
			if (index / 2 != i) {
				faceDisrupted[2 * i + 1] = true;
				faceDisrupted[2 * i] = true;
			}
		}
	}

	public String generateScramble() {
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(6);
			if (faceDisrupted[index]) {
				addToScramble(index);
			} else {
				i--;
			}
		}
		return scramble.toString();
	}

	public static void main(String[] args) {
		Scramble scramble = new Scramble(30, 5);
		System.out.println(scramble.generateScramble());
	}
}
