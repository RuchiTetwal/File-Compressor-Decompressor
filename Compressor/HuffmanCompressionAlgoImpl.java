package Compressor;

import java.io.*;
import java.util.*;

public class HuffmanCompressionAlgoImpl implements IHuffmanCompressionAlgo{

    @Override
    public Map<Byte, Integer> countFreq(byte[] orgFileList) {
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

    @Override
    public PriorityQueue<HuffmanNode> generateMinHeap(Map<Byte, Integer> freqMap) {
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

    @Override
    public HuffmanNode generateTree(PriorityQueue<HuffmanNode> minHeap) {
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
    private void generateCode(HuffmanNode root, String str, Map<Byte, String> huffmanMap)
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

    @Override
    public Map<Byte, String> generateHuffmanCode(HuffmanNode root) {
        Map<Byte, String> huffmanMap= new HashMap<Byte, String>();
        generateCode(root, "", huffmanMap);
        return huffmanMap;
    }

    @Override
    public void encodeOrgFile(byte[] orgFileList, String compFileName, Map<Byte, String> huffmanMap, HuffmanNode root) throws IOException{     
        IEncodeOrgFile encoder = new HuffmanEncodeOrgFileImpl();
        encoder.encodeOrgFile(orgFileList, compFileName, huffmanMap, root);    
    }   
}


