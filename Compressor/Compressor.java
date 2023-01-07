package Compressor;
import java.util.*;


public class Compressor{

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