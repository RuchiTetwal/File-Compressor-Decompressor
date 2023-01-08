package Compressor;

import java.util.*;
import java.io.*;

public class CompressorMain{

      public static void main(String args[]) throws IOException{
        String originalFileName=args[0];
        String compFileName=args[1];


        Icompressor compObj = new CompressorImpl();

        compObj.compress(originalFileName, compFileName);
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