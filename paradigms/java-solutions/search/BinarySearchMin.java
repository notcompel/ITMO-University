package search;

public class BinarySearchMin {

    //pred: for i, j: 0 <= i < j < k && arr[i] > arr[j] &&
    //      for i, j: k <= i < j < arr.length && arr[i] < arr[j] && -1 <= l' <= r' <= size
    //post: R == min(arr[i]: 0 <= i < arr.length)
    static int binarySearchRec(int l, int r, int[] arr) {
        // P = ((l' < 0 && r' >= arr.length) ||
        //      (l' < 0 && min < arr[r']) ||
        //      (r' >= arr.length && min < arr[l']) ||
        //      (arr[l'] > min && min < arr[r']))
        if (r - l <= 1) {
            //r' - l' <= 1 && P

            if (l >= 0 && r < arr.length) {
                //(l >= 0 && r < arr.length) && (r' - l' <= 1) && P
                //(l >= 0 && r < arr.length) && (r' - l' <= 1) && arr[l'] > min && min < arr[r']
                //(r' - l' <= 1) && arr[l'] > min && min < arr[r']
                return Math.min(arr[l], arr[r]);
            } else if (l >= 0) {
                //r' > arr.length && l' >= 0 && (r' - l' <= 1) && P
                //r' - l' <= 1 && r' >= arr.length && min < arr[l'])
                return arr[l];
            }
            //l' < 0 && (r' - l' <= 1) && P
            //r' - l' <= 1 && l' < 0 && min < arr[r'])
            return arr[r];
        }
        //r' - l' > 1 && P
        int m = l + (r - l) / 2;
        //(m' == (l' + r') / 2) && P

        if (m + 1 < arr.length && arr[m] > arr[m + 1]
                || m - 1 >= 0 && arr[m - 1] > arr[m]) {
            // P && (m' == l + (r - l) / 2 && (m' + 1 < arr.length && arr[m'] > arr[m' + 1] || m' - 1 >= 0 && arr[m' - 1] > arr[m'])
            // P && (m' == l + (r - l) / 2 && for i, j: m' <= i < j < k && arr[i] > arr[j] && -1 <= l' <= (l' + r') / 2 <= r' <= arr.length
            // P && (m' == l + (r - l) / 2 && for i, j: m' <= i < j < k && arr[i] > arr[j] && -1 <= l' <= m' <= r' <= arr.length

            // P && for i, j: m' <= i < j < k && arr[i] < arr[j] && -1 <= m' <= r' <= arr.length
            return binarySearchRec(m, r, arr);
        } else {
            // P && (m' == (l' + r') / 2) && (arr[m'] < arr[m' + 1] || arr[m' - 1] > arr[m'])
            // P && (m' == (l' + r') / 2) && for i, j: k <= i < j < m' && arr[i] < arr[j] && -1 <= l' <= (l' + r') / 2 <= r' <= arr.length
            // P && (m' == (l' + r') / 2) && for i, j: k <= i < j < m' && arr[i] < arr[j] && -1 <= l' <= m' <= r' <= arr.length

            // P && for i, j: k <= i < j < m' && arr[i] < arr[j] && -1 <= m' <= r' <= arr.length
            return binarySearchRec(l, m, arr);
        }
    }

    //pred: for i, j: 0 <= i < j < k && arr[i] > arr[j] &&
    //      for i, j: k <= i < j < arr.length && arr[i] < arr[j] && -1 <= l' <= r' <= size
    //post: R == min(arr[i]: 0 <= i < arr.length)
    static int binarySearchIter(int[] arr) {
        int l = -1, r = arr.length;

        // P = ((l' < 0 && r' >= arr.length) ||
        //      (l' < 0 && min <= arr[r']) ||
        //      (r' >= arr.length && min <= arr[l']) ||
        //      (arr[l'] >= min && min <= arr[r']))
        while (r - l > 1) {
            //r' - l' > 1 && P
            int m = l + (r - l) / 2;
            //(m' == l + (r - l) / 2) && P
            if (m + 1 < arr.length && arr[m] > arr[m + 1]
                    || m - 1 >= 0 && arr[m] < arr[m - 1]) {
                // I && (m' == l + (r - l) / 2) && (m' + 1 < arr.length && arr[m'] > arr[m' + 1] || m' - 1 >= 0 && arr[m' - 1] > arr[m'])
                // I && (m' == l + (r - l) / 2) && (arr[m'] >= min)

                l = m;
                // I && (arr[l'] >= min)
                // I
            } else {
                // I && (m' == l + (r - l) / 2) && (arr[m'] < arr[m' + 1] || arr[m' - 1] > arr[m'])
                // I && (m' == l + (r - l) / 2) && (arr[m'] >= min)

                r = m;
                // I && (min <= arr[r'])
                // I
            }
            // I
        }
        //r' - l' <= 1 && I
        if (l >= 0 && r < arr.length) {
            //(l >= 0 && r < arr.length) && (r' - l' <= 1) && I
            //(l >= 0 && r < arr.length) && (r' - l' <= 1) && arr[l'] > min && min < arr[r']
            //(r' - l' <= 1) && arr[l'] > min && min < arr[r']
            return Math.min(arr[l], arr[r]);
        } else if (l >= 0) {
            //r' > arr.length && l' >= 0 && (r' - l' <= 1) && I
            //r' - l' <= 1 && r' >= arr.length && min < arr[l'])
            return arr[l];
        }
        //l' < 0 && (r' - l' <= 1) && I
        //r' - l' <= 1 && l' < 0 && min < arr[r'])
        return arr[r];
    }

    //pred: args.length > 1
    //post: false
    public static void main(String[] args) {
        int[] arr = new int[args.length];
        for(int i = 0; i < args.length; i++) {
            arr[i] = Integer.parseInt(args[i]);
        }
        System.out.println(binarySearchIter(arr));
        //System.out.println(binarySearchRec(-1, arr.length, arr));
    }
}

