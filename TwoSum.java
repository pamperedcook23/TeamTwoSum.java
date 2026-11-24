/**
 * Every team implements this interface in its own class.
 *
 * Rules:
 *  - Return the two VALUES from nums that add up to target, e.g. {8, 5}.
 *    Order does not matter. If several pairs work, any one is fine.
 *  - Return null if no such pair exists.
 *  - Each element may be used only once (a single 5 can't make 10).
 *  - Do not modify nums (copy it first if your algorithm sorts).
 *  - Your class needs a public no-argument constructor.
 */
public interface TwoSum {
    int[] findPair(int[] nums, int target);
}
