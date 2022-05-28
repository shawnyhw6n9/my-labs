package tool;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * Use Java util to gen random UUID.
 *
 * <ol>
 * Two types:
 * <li>getJavaUUID(int len)</li>
 * <li>getUUID()</li>
 * </ol>
 * <pre>
 *
 * Ex:
 * tool.JavaUtil.java usage is
 * JavaUtil.getJavaUUID()	= 2956ec4d-1a63-4dbf-b9e9-b34639d6
 * JavaUtil.getJavaUUID(20)	= 90e7ecf1-f5f3-480c-b
 * JavaUtil.getUUID()		= A881911866B319762837
 * </pre>
 */
public class JavaUtil {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println("tool.JavaUtil.java usage:");
        System.out.printf("\tJavaUtil.getJavaUUID() \t\t= %s\n", JavaUtil.getJavaUUID());
        System.out.printf("\tJavaUtil.getJavaUUID(%d) \t= %s\n", 20, JavaUtil.getJavaUUID(20));

        System.out.printf("\tJavaUtil.getUUID() \t\t\t= %s\n", JavaUtil.getUUID());
    }

    /**
     * Get reandom UUID.
     *
     * @param len
     * @return java.util.UUID
     */
    public static String getJavaUUID(int len) {
        return UUID.randomUUID().toString().substring(0, len);
    }

    public static String getJavaUUID() {
        return getJavaUUID(32);
    }

    /**
     * Get the UUID
     *
     * @return
     */
    public static Object getUUID() throws NoSuchAlgorithmException {
        return "A" + JavaUtil.genRandInt(100000000, 999999999) + "B" + JavaUtil.genRandInt(100000000, 999999999);
    }

    /**
     * Random the min to max number
     *
     * @param min
     * @param max
     * @return
     */
    private static int genRandInt(int min, int max) throws NoSuchAlgorithmException {
        SecureRandom rand = getRandom();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Gen Securn random
     *
     * @return
     */
    private static SecureRandom getRandom() throws NoSuchAlgorithmException {
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
        return rand;
    }
}
