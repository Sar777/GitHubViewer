package instinctools.android.utility;

import java.util.Random;

public class RandomGenerator {
    public static int rand(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }

        return new Random().nextInt(max - min) + min;
    }
}
