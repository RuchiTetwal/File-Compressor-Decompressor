package Compressor;

import java.util.*;
import java.io.*;

public class CompressorImpl implements Icompressor{
  
    public void compress(String orgFileName, String compFileName) throws IOException{
     
        FileInputStream orgFileReader = new FileInputStream(orgFileName);
        byte[] orgFileList = new byte[orgFileReader.available()];
        orgFileReader.read(orgFileList);

        IHuffmanCompressionAlgo helper = new HuffmanCompressionAlgoImpl();
        Map<Byte, Integer> freqMap = helper.countFreq(orgFileList);
        PriorityQueue<HuffmanNode> minHeap = helper.generateMinHeap(freqMap);
        HuffmanNode root = helper.generateTree(minHeap);
        Map<Byte, String> huffmanMap =  helper.generateHuffmanCode(root);
        File CompressedFile = new File(compFileName);
        System.out.println("File created: " + CompressedFile.getName());
        helper.encodeOrgFile(orgFileList,compFileName , huffmanMap,root);
        orgFileReader.close();
       
    }
}

class HuffmanNode {
    int freq;
    byte ch;
    HuffmanNode left;
    HuffmanNode right;
}

class comp implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode n1, HuffmanNode n2)
    {
        return n1.freq - n2.freq;
    }
}
