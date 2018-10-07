package ai;

import java.util.Comparator;

public class MinMaxThreadComparator implements Comparator<MinMaxThread> {

	@Override
	public int compare(MinMaxThread o1, MinMaxThread o2) {
		return o1.getValue() != o2.getValue() ? o1.getValue() > o2.getValue() ? -1 : 1 : 0;
	}

}
