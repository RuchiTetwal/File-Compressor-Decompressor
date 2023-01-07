package Compressor;

import java.util.*;

public interface GenerateMinHeap {
    

    //Function to count frequency of each character in original file and to store it in a hashmap
    public HashMap<Byte, Integer> countFreq(byte[] orgFileList,HashMap<Byte, Integer> freqMap);
   

    //Function to generate priority queue (min heap) from frequrncy hashmap
    public PriorityQueue<HuffmanNode> generateMinHeap(HashMap<Byte, Integer> freqMap);


}
