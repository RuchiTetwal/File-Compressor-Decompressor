package Compressor;

import java.util.*;

public class EncodeCharImpl implements IencodeChar {
    
    //function to generate Huffman Tree using Min-Heap(Priority Queue)
    public HuffmanNode generateTree(PriorityQueue<HuffmanNode> minHeap)
    {
        HuffmanNode root=null;
        while (minHeap.size()>1) {

            
            HuffmanNode min1 = minHeap.peek();
            minHeap.poll();

        
            HuffmanNode min2 = minHeap.peek();
            minHeap.poll();

    
            HuffmanNode newNode = new HuffmanNode();

    
            newNode.freq = min1.freq + min2.freq;
            newNode.ch = '-';

            //extracting 2 minimum frequency nodes form min-heap 
            newNode.left = min1;
            newNode.right = min2;

        
            root = newNode;

            // adding new node back to minHeap
            minHeap.add(newNode);

        }
        return root;
    }

    
    //function to generate huffman codes for each unique character and storing them in a hashmap(huffmanMap)
    public void generateCode(HuffmanNode root, String str, Map<Byte, String> huffmanMap)
    {
        if(root == null)
            return;
        
        if (root.left == null && root.right == null) {


            huffmanMap.put(root.ch, str);
 
            return;
        }            

        generateCode(root.left, str + "0", huffmanMap);
       
        generateCode(root.right, str + "1", huffmanMap);
    }
}
