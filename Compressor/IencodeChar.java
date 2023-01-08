package Compressor;

import java.util.*;

public interface IencodeChar {
    

    //Function to generate huffman tree from min heap
    public HuffmanNode generateTree(PriorityQueue<HuffmanNode> minHeap);

    //Function to generate huffman encoding from huffman tree
    public void generateCode(HuffmanNode root, String str, Map<Byte, String> huffmanMap);
    

}
