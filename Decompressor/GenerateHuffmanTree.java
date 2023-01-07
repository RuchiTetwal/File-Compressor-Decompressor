package Decompressor;

public interface GenerateHuffmanTree {
    
    //function to generate huffmann tree from header of compressed file
    public Node generateHuffmanTree(byte[] header, int headerSize);
}
