package Algo;

import java.util.Comparator;

/**
 * Created by priyadarshini on 4/1/15.
 */
public class QueueComparator implements Comparator<CSRequest>
    {
        @Override
        public int compare(CSRequest firstRequest, CSRequest secondRequest) {
            return (firstRequest.sequenceNumber == secondRequest.sequenceNumber) ? (Integer.valueOf(firstRequest.nodeNumber).compareTo(Integer.valueOf(secondRequest.nodeNumber)))
                    : (firstRequest.sequenceNumber - secondRequest.sequenceNumber);
        }
    }
