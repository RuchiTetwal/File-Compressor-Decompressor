package Compressor;

import java.util.*;

public class GenerateMinHeapImpl implements IgenerateMinHeap{
    
    public Map<Byte, Integer> countFreq(byte[] orgFileList){
        HashMap<Byte, Integer> freqMap = new HashMap<Byte, Integer>();



        //storing frequency of each char in hashmap
        for(byte currChar:orgFileList){

            if(freqMap.containsKey(currChar)){
                freqMap.put(currChar, freqMap.get(currChar)+1);
            }
            else{
                freqMap.put(currChar,1);
            }

        }

        return freqMap;
    }

    public PriorityQueue<HuffmanNode> generateMinHeap(Map<Byte, Integer> freqMap){

        PriorityQueue<HuffmanNode> minHeap = new PriorityQueue<HuffmanNode>(freqMap.size(), new comp());
    
    
        for (Map.Entry<Byte,Integer> element : freqMap.entrySet()) {

            Byte ch= element.getKey();
            int chFreq = element.getValue();

            // creating a Huffman node object and adding it to the priority queue
             HuffmanNode node = new HuffmanNode();

            node.ch = ch;
            node.freq = chFreq;

            node.left = null;
            node.right = null;

            //adding node to queue
            minHeap.add(node);
        }

        return minHeap;

    }
}
