package search;

public class BinarySearch {

    //pred: for i, j: 0 <= i < j <= arr.length - 1 && arr[i] >= arr[j] && -1 <= l' < r' <= size
    //post: R == min(i: 0 <= i < arr.length && arr[i] <= x)
    static int BinarySearchRec(int l, int r, int x, int[] arr) {
        // P = ((l' == -1 && r' == arr.length) ||
        //      (l' == -1 && x >= arr[r']) ||
        //      (r' == arr.length && x < arr[l']) ||
        //      (arr[l'] > x >= arr[r']))
        if (r - l <= 1) {
            //r' - l' <= 1 && P
            //arr[r' - 1] > x >= arr[r']
            //(arr[r'] <= x) && (arr[r' - 1] > x && r' - 1 < r') - для меньшего r' уже не соблюдается
            //R == min({i: 0 <= i < arr.length && arr[i] <= x}
            return r;
        }
        //r' - l' > 1 && P
        int m = l + (r - l) / 2;
        //(m' == (l' + r') / 2) && P
        if (arr[m] > x) {
            // (m' == (l' + r') / 2) && P && arr[m'] > x
            // (m' == (l' + r') / 2) && P && (arr[l'] > arr[(l' + r') / 2] > x >= arr[r'])
            // (m' == (l' + r') / 2) && P && (arr[m'] > x >= arr[r'])

            // P && (l' + r') / 2 >= (l' + l') / 2 && l' >= -1 && r <= arr.length
            // P && (m' >= l'>= -1) && r' <= arr.length
            // P && -1 <= m' < r' <= arr.length
            return BinarySearchRec(m, r, x, arr);
        } else {
            // (m' == (l' + r') / 2) && I && arr[m'] <= x
            // (m' == (l' + r') / 2) && I && (arr[l'] > x >= arr[(l' + r') / 2] > arr[r'])
            // (m' == (l' + r') / 2) && I && (arr[l'] > x >= arr[m'])

            // P && -1 <= l' && (l' + l') / 2 && < (l' + r') / 2 < (r' + r') / 2 && r <= arr.length
            // P && -1 <= l' < m' < r'<= arr.length)
            // P && -1 <= l' < m <= arr.length
            return BinarySearchRec(l, m, x, arr);
        }
    }

    //pred: for i, j: 0 <= i < j < arr.length && arr[i] >= arr[j]
    //post: R == min(i: 0 <= i < arr.length && arr[i] <= x)
    static int BinarySearchIter(int x, int[] arr) {
        int l = -1, r = arr.length;

        // I = ((l' == -1 && r' == arr.length) ||
        //      (l' == -1 && x >= arr[r']) ||
        //      (r' == arr.length && x < arr[l']) ||
        //      (arr[l'] > x >= arr[r']))
        while (r - l > 1) {
            // I && r' - l' > 1
            int m = l + (r - l) / 2;
            // (m' == (l' + r') / 2) && I
            if (arr[m] > x) {
                // (m' == (l' + r') / 2) && I && arr[m'] > x
                // (m' == (l' + r') / 2) && I && (arr[l'] > arr[(l' + r') / 2] > x >= arr[r'])
                // (m' == (l' + r') / 2) && I && (arr[m'] > x >= arr[r'])

                l = m;
                // I && (arr[l'] > x >= arr[r'])
                // I
            } else {
                // (m' == (l' + r') / 2) && I && arr[m'] <= x
                // (m' == (l' + r') / 2) && I && (arr[l'] > x >= arr[(l' + r') / 2] > arr[r'])
                // (m' == (l' + r') / 2) && I && (arr[l'] > x >= arr[m'])

                r = m;
                // I && (arr[l'] > x >= arr[r'])
                // I
            }
            // I
        }
        // r' - l' <= 1 && I
        // arr[r' - 1] > x >= arr[r']
        // (arr[r'] <= x) && (arr[r' - 1] > x && r' - 1 < r') - для меньшего r' уже не соблюдается
        // R == min({i: 0 <= i < arr.length && arr[i] <= x}
        return r;
    }


    //pred: args.length > 1
    //post: false
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] arr = new int[args.length - 1];
        for(int i = 1; i < args.length; i++) {
            arr[i - 1] = Integer.parseInt(args[i]);
        }
        System.out.print(BinarySearchIter(x, arr));
        //System.out.print(BinarySearchRec(-1, arr.length, x, arr));
    }
}
