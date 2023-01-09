package times;

import java.util.ArrayList;
import java.util.List;

public class Statistics {
	private ArrayList<Long> times;

	private int count;

	private long sum;

	private long best;

	private long mean;

	private int[] n = new int[] { 5, 12, 50, 100, 1000 };

	private long[] sumN = new long[5];

	private long[] bestN = new long[5];

	private long[] meanN = new long[5];

	private long[] worstN = new long[5];

	public Statistics() {
		count = 0;
		best = 0L;
		sum = 0L;
		mean = 0L;
		times = new ArrayList<>();
	}

	public String convertToString(long time) {
		String s;
		long milliseconds = time % 1000L;
		time /= 1000L;
		long seconds = time % 60L;
		time /= 60L;
		if (time == 0L) {
			s = String.valueOf(seconds) + "." + String.format("%03d", new Object[] { Long.valueOf(milliseconds) });
		} else {
			s = String.valueOf(time) + ":" + String.format("%02d", new Object[] { Long.valueOf(seconds) }) + "."
					+ String.format("%03d", new Object[] { Long.valueOf(milliseconds) });
		}
		return s;
	}

	private long findNewBest(List<Long> list) {
		long bestTime = Long.MAX_VALUE;
		for (int i = 0; i < list.size(); i++) {
			if (((Long) list.get(i)).longValue() < bestTime)
				bestTime = ((Long) list.get(i)).longValue();
		}
		return bestTime;
	}

	private long findNewWorst(List<Long> list) {
		long worstTime = 0L;
		for (int i = 0; i < list.size(); i++) {
			if (((Long) list.get(i)).longValue() > worstTime)
				worstTime = ((Long) list.get(i)).longValue();
		}
		return worstTime;
	}

	private void calculateStats(long time) {
		for (int i = 0; i < 5; i++) {
			sumN[i] = sumN[i] + time;
			worstN[i] = time;
			bestN[i] = time;
			if (time < bestN[i]) {
				bestN[i] = time;
			} else if (time > worstN[i]) {
				worstN[i] = time;
			}
			if (count >= n[i]) {
				if (count > n[i]) {
					long tempTime = ((Long) times.get(count - n[i] - 1)).longValue();
					sumN[i] = sumN[i] - tempTime;
					if (tempTime == bestN[i]) {
						bestN[i] = findNewBest(times.subList(count - n[i], count));
					} else if (tempTime == worstN[i]) {
						worstN[i] = findNewWorst(times.subList(count - n[i], count));
					}
				}
				meanN[i] = (sumN[i] - bestN[i] - worstN[i]) / (n[i] - 2);
			}
		}
		System.out.print(String.valueOf(bestN[0]) + " ");
		System.out.print(worstN[0]);
		System.out.println();
		if (time < best || count == 1)
			best = time;
		sum += time;
		mean = sum / count;
	}

	public void addTime(long time) {
		times.add(Long.valueOf(time));
		count++;
		calculateStats(time);
	}

	public void deleteTime(int index) {
		long deletedTime = ((Long) times.remove(index - 1)).longValue();
		count--;
		for (int i = 0; i < 5; i++) {
			if (count - index + 1 < n[i]) {
				sumN[i] = sumN[i] - deletedTime;
				worstN[i] = 0L;
				bestN[i] = 0L;
				if (deletedTime == bestN[i]) {
					bestN[i] = findNewBest(times.subList(Math.max(0, count - n[i]), count));
				} else if (deletedTime == worstN[i]) {
					worstN[i] = findNewBest(times.subList(Math.max(0, count - n[i]), count));
				}
				if (count < n[i]) {
					meanN[i] = 0L;
				} else {
					sumN[i] = sumN[i] + ((Long) times.get(count - n[i])).longValue();
					meanN[i] = (sumN[i] - bestN[i] - worstN[i]) / (n[i] - 2);
				}
			}
		}
		sum -= deletedTime;
		if (count != 0) {
			mean = sum / count;
			best = findNewBest(times.subList(0, count));
		} else {
			best = 0L;
			mean = 0L;
		}
	}

	public String[] getStats() {
		String[] stats = { "  Best:      " + convertToString(best), "Mean:   " + convertToString(mean),
				"  Ao5:       " + convertToString(meanN[0]), "Ao12:    " + convertToString(meanN[1]),
				"  Ao50:     " + convertToString(meanN[2]), "Ao100:  " + convertToString(meanN[3]),
				"  Ao1000:  " + convertToString(meanN[4]) };
		return stats;
	}

	public int getCount() {
		return count;
	}
}
