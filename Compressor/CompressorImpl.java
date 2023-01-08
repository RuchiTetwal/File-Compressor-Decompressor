package Compressor;

import java.util.*;
import java.io.*;

public class CompressorImpl implements Icompressor{
  
    public void compress(String orgFileName, String compFileName) throws IOException{
     
        FileInputStream orgFileReader = new FileInputStream(orgFileName);

        byte[] orgFileList = new byte[orgFileReader.available()];

        //reading data from original file in byte array
        orgFileReader.read(orgFileList);

        IgenerateMinHeap minHeapGenerator = new GenerateMinHeapImpl();

        //Hashmap for storing frequencies of each unique character of original file
        Map<Byte, Integer> freqMap = minHeapGenerator.countFreq(orgFileList);

        //generating a min-heap using priority queue 
        PriorityQueue<HuffmanNode> minHeap = minHeapGenerator.generateMinHeap(freqMap);

        IencodeChar charEncodingGenerator = new EncodeCharImpl();

        //----Creating huffmann tree using min heap
        //Creating root node of huffmann tree
        HuffmanNode root = charEncodingGenerator.generateTree(minHeap);

        //Extracting 2 min. frequecy value from the heap and adding them as single node and pushing pack to priority queue
            
        Map<Byte, String> huffmanMap = new HashMap<Byte, String>();

        //generating huffmann code for each character by tree traversal
        charEncodingGenerator.generateCode(root, "",huffmanMap);
    
        File CompressedFile = new File(compFileName);
        System.out.println("File created: " + CompressedFile.getName());
           
        IencodeOrgFile fileEncodingGenerator = new EncodeOrgFileImpl();
        fileEncodingGenerator.encodeOrgFile(orgFileList,compFileName , huffmanMap,root);
       
        orgFileReader.close();
        
    }
}
