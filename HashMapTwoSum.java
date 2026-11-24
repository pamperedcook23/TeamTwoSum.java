import java.util.Arrays; 
import java.util.HashMap; 

import java.util.Map; 

 

public class HashMapTwoSum implements TwoSum {
public int[] findPair(int[] nums, int target) {
   return twoSum(nums, target);
}

public static int[] twoSum(int[] nums, int target) { 

   Map<Integer, Integer> seen = new HashMap<>(); 

   for (int i = 0; i < nums.length; i++) { 

       int complement = target - nums[i]; 

       if (seen.containsKey(complement)) { 

           return new int[] { complement, nums[i] }; 

       } 

       seen.put(nums[i], i); 

   } 

 

   return null; 

} 

 

public static void main(String[] args) { 

       int[] result = twoSum(new int[] {8, 1, 5, 3}, 8); 

       System.out.println(Arrays.toString(result)); // [5, 3] 

   } 
}
