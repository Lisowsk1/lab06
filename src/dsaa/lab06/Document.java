package dsaa.lab06;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document {
    public String name;
    public TwoWayCycledOrderedListWithSentinel<Link> link;
    private static int[] weights;

    public Document(String name, Scanner scan) {
        this.name = name.toLowerCase();
        link = new TwoWayCycledOrderedListWithSentinel<Link>();
        load(scan);
    }

    public void load(Scanner scan) {
        String line = scan.nextLine();


        // Ask for input until "eod" command
        while (!line.equals("eod")) {

            String[] words = line.split(" "); // Separate each line into words

            // Loop through all words in a line
            for (String word : words) {

                word = word.toLowerCase();

                if (word.matches("link=([a-z][a-z0-9_]*)(\\([0-9]*\\))?")) {

                    word = word.substring("link=".length());

                    Link link = createLink(word);

                    if (link != null) {
                        this.link.add(link);
                    }

                }

            }

            line = scan.nextLine();
        }
    }

    public static boolean isCorrectId(String id) {
        return id.matches("^[a-zA-Z][a-zA-Z0-9_]*$");
    }

    // accepted only small letters, capitalic letter, digits nad '_' (but not on the beginning)

    /**
     * Creates a link if provided string is of correct pattern.
     *
     * @param link a string from which the new {@link Link} object will be created
     * @return {@link Link} {@code link}
     */
    public static Link createLink(String link) {
        Pattern pattern = Pattern.compile("([a-zA-Z][a-zA-Z0-9_]*)(\\([0-9]*\\))?");
        Matcher matcher = pattern.matcher(link);

        if (matcher.find()) {

            String refGroup = matcher.group(1);
            String weightGroup = matcher.group(0).substring(refGroup.length());

            if (weightGroup.isEmpty())
                return new Link(refGroup.toLowerCase());

            // Extract weight from parentheses
            int weight = Integer.parseInt(weightGroup.substring(1, weightGroup.length() - 1));

            return new Link(refGroup.toLowerCase(), weight);

        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Document: ").append(name).append("\n");

        int counter = 1;

        ListIterator<Link> it = link.listIterator();
        while (it.hasNext()) {

            Link link = it.next();
            sb.append(link.toString());
            counter++;

            if (counter > 10) {
                sb.append("\n");
                counter = 1;
            } else {
                sb.append(" ");
            }

        }

        return sb.toString().trim();
    }

    public String toStringReverse() {
        StringBuilder sb = new StringBuilder();

        sb.append("Document: ").append(name).append("\n");

        int counter = 1;

        ListIterator<Link> it = link.listIterator();
        while (it.hasPrevious()) {

            Link link = it.previous();
            sb.append(link.toString());
            counter++;

            if (counter > 10) {
                sb.append("\n");
                counter = 1;
            } else {
                sb.append(" ");
            }
        }

        return sb.toString().trim();
    }

    public int[] getWeights() {
        int i = 0;
        weights = new int[link.size];
        for (Link l : link) {
            weights[i] = l.weight;
            i++;
        }

        return weights;
    }

    public static void showArray(int[] arr) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < weights.length; i++) {
            if (i != 0)
                s.append(" ");
            s.append(weights[i]);
        }
        System.out.println(s);
    }

    void bubbleSort(int[] arr) {
        showArray(arr);

        int n = arr.length;
        //start from left side
        for (int i = 0; i < n - 1; i++) {
            for (int j = n - 1; j > i; j--) {//
                if (arr[j] < arr[j - 1]) {
                    //swap if smaller
                    int temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                }
            }
            //1st iteration comment: now at the arr[0] is the smallest number
            showArray(arr);
        }
    }

    public void insertSort(int[] arr) {
        showArray(arr);

        int n = arr.length;

        //start from highest index so the sorted array part will grow from right side
        for (int i = n - 2; i >= 0; i--) {
            int k = arr[i];
            int j = i + 1;

            while (j < n && arr[j] < k) {
                arr[j - 1] = arr[j]; //shifting left to make room
                j++;
            }
            arr[j - 1] = k;
            showArray(arr);
        }

    }

    public void selectSort(int[] arr) {
        showArray(arr);

        int n = arr.length;
        //start from highest index so the sorted array part will grow from right side
        for (int i = n - 1; i > 0; i--) {
            //find max element index
            int maxIndex = 0;
            for (int j = 1; j <= i; j++) {
                if (arr[j] > arr[maxIndex]) {
                    maxIndex = j;
                }
            }

            //swap max element into i
            int temp = arr[maxIndex];
            arr[maxIndex] = arr[i];
            arr[i] = temp;

            showArray(arr);
        }
    }

    public void iterativeMergeSort(int[] arr) {
        showArray(arr);

        int n = arr.length;
        int[] temp = new int[n];

        for (int width = 1; width < n; width *= 2) {
            //Merge
            for (int left = 0; left < n; left += 2 * width) {
                int mid = Math.min(left + width, n);
                int right = Math.min(left + 2 * width, n);
                merge(arr, temp, left, mid, right);
            }
            showArray(arr);
        }


    }

    //Merge arr[left:mid-1] and arr[mid:right-1]
    public void merge(int[] arr, int[] temp, int left, int mid, int right) {

        for (int i = left; i < right; i++) {
            temp[i] = arr[i];
        }
        int i = left;
        int j = mid;
        for (int k = left; k < right; k++) {
            if (i < mid && (j >= right || temp[i] <= temp[j])) { // check if left subarray is exhausted and check if the right subarray is exhausted or
                // if they are both not exhausted, then just compare elements and choose which one is smaller and put it into the array
                arr[k] = temp[i];
                i++;
            } else { //left is empty or right exists and is smaller, take from right
                arr[k] = temp[j];
                j++;
            }
        }

    }

    public void radixSort(int[] arr) {
        showArray(arr);

        int n = arr.length;
        if(n==0)
            return;

        int max=Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if(arr[i]>max)
                max=arr[i];
        }

        int[] temp = new int[n]; //temporary
        int[] count = new int[10];

        for (int exp = 1; max / exp > 0; exp *= 10) { //Pass by every power of 10 to extract the digits
            Arrays.fill(count,0);

            for (int i = 0; i < arr.length; i++) {
                count[(arr[i]/exp)%10]++; //the extracted digit count
            }

            for (int i = 1; i < 10; i++) {
                count[i]+=count[i-1]; //Change count[] to now have position of this digit in temp
            }


            for (int i = n - 1; i >= 0; i--) { //go in reverse order to have it sorted in ascending order in respect to the current exponent
                int digit = (arr[i] / exp) % 10; //current digit
                count[digit]--; //point the index
                temp[count[digit]] = arr[i]; //build the partially sorted temporary helper array
            }
            System.arraycopy(temp, 0, arr, 0, n);

            showArray(arr);
        }
        showArray(arr);

    }

}
