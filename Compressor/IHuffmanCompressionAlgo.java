package Compressor;

import java.io.IOException;
import java.util.Map;
import java.util.PriorityQueue;

public interface IHuffmanCompressionAlgo {

    //Function to count frequency of each character in original file and to store it in a hashmap
    Map<Byte, Integer> countFreq(byte[] orgFileList);

    PriorityQueue<HuffmanNode> generateMinHeap(Map<Byte, Integer> freqMap);

    HuffmanNode generateTree(PriorityQueue<HuffmanNode> minHeap);

    Map<Byte, String> generateHuffmanCode(HuffmanNode root);

    void encodeOrgFile(byte[] orgFileList, String compFileName, Map<Byte, String> huffmanMap, HuffmanNode root) throws IOException;
    
}
