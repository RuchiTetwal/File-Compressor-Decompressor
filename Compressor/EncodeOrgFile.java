package Compressor;

import java.util.*;

public interface EncodeOrgFile {

    //Function to generate postorder string of huffmann tree
    public void generatePostOrder(HuffmanNode root);

    //Function to generate header for compressing huffman tree
    public byte[] generateHeader(HuffmanNode root);

    //Function to generate compressed file and write compressed data in it
    public void encodeOrgFile(byte[] orgFileList, String CompressedTextFile, HashMap<Byte, String> huffmanMap,HuffmanNode root );
}
