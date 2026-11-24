public class BruteForceTwoSum implements TwoSum {
    public int[] findPair(int[] nums, int target) {
        // Iterate through each number as the first number in the sum.
        for (int i = 0; i < nums.length; i++) {
            // Iterate through each number as the second number in the sum.
            // This iteration starts in a way that the same number never appears twice.
            for (int j = i + 1; j < nums.length; j++) {
                // Returns the first pair of numbers that add up to the target.
                if ((nums[i] + nums[j]) == target) {
                    return new int[] {nums[i], nums[j]};
                }
            }
        }
        // The case where no pair of numbers adds up to the target.
        return null;
    }
}
