package api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Permutations {

	public List<List<Integer>> permuteUnique(int x, int nbs) {
		System.out.println("KNF Clause, "+x + " für " +nbs);
		int[] nums = new int[nbs];
		Arrays.fill(nums,0);
		for(int i = 0; i < x; i++) {
			nums[i] = 1;
		}
		List<List<Integer>> bigList = 
			new ArrayList<List<Integer>>();
		Arrays.sort(nums);
		permute(nums, 0, bigList);
		return bigList;
	}

	private void permute(int[] nums, int index, List<List<Integer>> bigList) {
		if (index == nums.length) {
			List l = new ArrayList<Integer>(nums.length);
			for (int num : nums)
				l.add(num);
			bigList.add(l);
			return;
		}
		Set<Integer> dups = new HashSet();
		for (int i = index; i < nums.length; i++) {
			if (dups.add(nums[i])) {
				swap(nums, i, index);
				permute(nums, index + 1, bigList);
				swap(nums, i, index);
			}
		}
	}

	private void swap(int[] nums, int i, int index) {
		int temp = nums[i];
		nums[i] = nums[index];
		nums[index] = temp;
	}
}
