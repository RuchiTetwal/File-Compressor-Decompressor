package Compressor;

import java.util.*;

public interface IgenerateMinHeap {
    

    //Function to count frequency of each character in original file and to store it in a hashmap
    public Map<Byte, Integer> countFreq(byte[] orgFileList);
   

    //Function to generate priority queue (min heap) from frequrncy hashmap
    public PriorityQueue<HuffmanNode> generateMinHeap(Map<Byte, Integer> freqMap);


}
