import java.util.Comparator;

/**
 * Created by priyadarshini on 4/1/15.
 */
public class QueueComparator implements Comparator<CSRequest>
    {
        public int compare(String x, String y)
        {
            // Assume neither string is null. Real code should
            // probably be more robust
            // You could also just return x.length() - y.length(),
            // which would be more efficient.
            if (x.length() < y.length())
            {
                return -1;
            }
            if (x.length() > y.length())
            {
                return 1;
            }
            return 0;
        }

        @Override
        public int compare(CSRequest firstRequest, CSRequest secondRequest) {
            return (firstRequest.sequenceNumber == secondRequest.sequenceNumber) ? (Integer.valueOf(firstRequest.nodeNumber).compareTo(Integer.valueOf(secondRequest.nodeNumber)))
                    : (firstRequest.sequenceNumber - secondRequest.sequenceNumber);
        }
    }
