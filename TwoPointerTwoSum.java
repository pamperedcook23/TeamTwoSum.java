import java.util.Arrays;

public class TwoPointerTwoSum implements TwoSum {
    // Return two values from distinct positions, or null if none match.
    public int[] findPair(int[] numbers, int target) {
        int[] sorted = numbers.clone();
        Arrays.sort(sorted);
        int left = 0;
        int right = sorted.length - 1;

        while (left < right) {
            long sum = (long) sorted[left] + sorted[right];
            if (sum == target) {
                return new int[] {sorted[left], sorted[right]};
            }
            if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        int[] numbers = {8, 1, 5, 3};
        System.out.println(Arrays.toString(new TwoPointerTwoSum().findPair(numbers, 8)));
    }
}
