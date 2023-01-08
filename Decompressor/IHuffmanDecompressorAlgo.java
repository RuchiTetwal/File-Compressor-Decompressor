package Decompressor;
import java.io.*;

public interface IHuffmanDecompressorAlgo {
    public int bytesToInt(byte[] bytes);

    //function to generate huffmann tree from header of compressed file
    public Node generateHuffmanTree(byte[] header, int headerSize);
 
    //Function to convert encoding to char and write them in decompressed file
    public void GetCharFromTreeWriteInDecompFile(FileOutputStream DecompFile, Node root, byte[] compArr, byte bitsInLastByteComp, int j)throws IOException;

    //Function to compare Original and Decompressed File
    public void CompareOrgDecompFiles(String orgFileName, String decompFileName);
}
