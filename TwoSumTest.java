import java.util.Arrays;
import java.util.Random;

/**
 * How to run:
 *
 *   javac *.java
 *   java TwoSumTest YourClassName
 *
 *   e.g. java TwoSumTest TwoPointerTwoSum (referring to TwoPointerTwoSum.class compiled from TwoPointerTwoSum.java)
 */
public class TwoSumTest {
    static final int[] BENCHMARK_SIZES = {1_000, 10_000, 100_000, 1_000_000};
    static final double TIME_LIMIT_MS = 100;

    static TwoSum solution;
    static int passed = 0;
    static int failed = 0;
    static long sink = 0; // uses benchmark results so Java can't optimize the calls away

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java TwoSumTest YourClassName");
            return;
        }
        solution = (TwoSum) Class.forName(args[0]).getDeclaredConstructor().newInstance();

        // Pairs that should be found
        expectPair("basic example", new int[] {8, 3, 11, 5, 2}, 13);
        expectPair("pair at the start", new int[] {2, 7, 11, 15}, 9);
        expectPair("pair at the end", new int[] {1, 2, 3, 4, 10, 20}, 30);
        expectPair("pair not next to each other", new int[] {4, 9, 1, 6, 2}, 10);
        expectPair("two-element array", new int[] {3, 4}, 7);
        expectPair("same value used twice when it appears twice", new int[] {5, 1, 5}, 10);
        expectPair("negative numbers", new int[] {-3, 4, 3, 90}, 0);
        expectPair("negative target", new int[] {-1, -2, -3, -4}, -7);
        expectPair("zeros", new int[] {0, 4, 3, 0}, 0);
        expectPair("several valid pairs (any one is fine)", new int[] {1, 9, 2, 8, 3, 7}, 10);
        expectPair("large input, pair at the end", largeEvenArray(), 39994);

        // No pair should be found
        expectNone("no pair exists", new int[] {1, 2, 3}, 100);
        expectNone("empty array", new int[] {}, 5);
        expectNone("single element", new int[] {5}, 5);
        expectNone("can't use the same element twice", new int[] {5, 1, 2}, 10);
        expectNone("large input, no pair", largeEvenArray(), 1);

        System.out.printf("%n%d passed, %d failed%n", passed, failed);

        benchmark();
        if (failed > 0) System.exit(1);
    }

    // Times findPair on shuffled even numbers with an odd target. No pair can exist, so every algorithm does its full amount of work (the worst case).
    static void benchmark() {
        System.out.println("\nBenchmark (worst case: no pair exists, shuffled input)");
        System.out.printf("%12s  %12s%n", "n", "best time");
        try {
            // Java speeds code up as it runs, so run it for a second before timing anything
            int[] warmup = randomEvenArray(10_000);
            long warmupEnd = System.nanoTime() + 1_000_000_000L;
            while (System.nanoTime() < warmupEnd) time(warmup);

            double previous = 0;
            for (int n : BENCHMARK_SIZES) {
                if (previous > TIME_LIMIT_MS) {
                    System.out.printf("%,12d  skipped (previous size took over %,.0f ms)%n", n, TIME_LIMIT_MS);
                    continue;
                }
                int[] nums = randomEvenArray(n);
                double best = time(nums);
                for (int run = 1; run < 5 && best < TIME_LIMIT_MS; run++) { // best of 5 smooths out noise
                    best = Math.min(best, time(nums));
                }
                System.out.printf("%,12d  %10.3f ms%n", n, best);
                previous = best;
            }
        } catch (Throwable t) {
            System.out.println("Benchmark crashed: " + t);
        }
    }

    static double time(int[] nums) {
        int target = 2 * nums.length - 1; // odd, so no two even numbers add up to it
        long start = System.nanoTime();
        int[] result = solution.findPair(nums, target);
        long elapsed = System.nanoTime() - start;
        if (result != null) sink += result.length;
        return elapsed / 1_000_000.0;
    }

    // {0, 2, 4, ..., 2(n-1)} in a random order. The fixed seed gives every team the same data.
    static int[] randomEvenArray(int n) {
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) nums[i] = i * 2;
        Random random = new Random(42);
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int swap = nums[i];
            nums[i] = nums[j];
            nums[j] = swap;
        }
        return nums;
    }

    static void expectPair(String name, int[] nums, int target) {
        check(name, nums, () -> {
            int[] result = solution.findPair(nums, target);
            if (result == null) fail("returned null, but a pair exists");
            if (result.length != 2) fail("expected 2 values, got " + result.length);
            if (result[0] + result[1] != target) {
                fail(result[0] + " + " + result[1] + " does not equal " + target);
            }
            if (!inArray(nums, result[0], result[1])) {
                fail(Arrays.toString(result) + " is not a valid pair from the input");
            }
        });
    }

    static void expectNone(String name, int[] nums, int target) {
        check(name, nums, () -> {
            int[] result = solution.findPair(nums, target);
            if (result != null) fail("expected null, got " + Arrays.toString(result));
        });
    }

    // Runs one test, records pass/fail, and checks that the input wasn't modified
    static void check(String name, int[] nums, Runnable test) {
        int[] original = nums.clone();
        try {
            test.run();
            if (!Arrays.equals(nums, original)) fail("the input array was modified");
            passed++;
            System.out.println("PASS  " + name);
        } catch (Throwable t) {
            failed++;
            System.out.println("FAIL  " + name + " -> " + t.getMessage());
        }
    }

    static void fail(String message) {
        throw new AssertionError(message);
    }

    // True if a and b are two different elements of nums
    static boolean inArray(int[] nums, int a, int b) {
        int countA = 0;
        int countB = 0;
        for (int n : nums) {
            if (n == a) countA++;
            if (n == b) countB++;
        }
        return a == b ? countA >= 2 : countA >= 1 && countB >= 1;
    }

    // {0, 2, 4, ..., 19998}: the only pair summing to 39994 is the last two
    static int[] largeEvenArray() {
        int[] nums = new int[10000];
        for (int i = 0; i < nums.length; i++) nums[i] = i * 2;
        return nums;
    }
}
