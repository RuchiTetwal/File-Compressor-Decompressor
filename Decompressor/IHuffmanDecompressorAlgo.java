package decompressor;
import java.io.*;

public interface IHuffmanDecompressorAlgo {
 
    //function to generate huffmann tree from header of compressed file
    public Node generateHuffmanTree(byte[] header, int headerSize);
 
    //Function to convert encoding to char and write them in decompressed file
    public void getCharFromTreeWriteInDecompFile(FileOutputStream DecompFile, Node root, byte[] compArr, byte bitsInLastByteComp,int headerSize, int orgFileSize)throws IOException;

    //Function to compare Original and Decompressed File
    public void compareOrgDecompFiles(String orgFileName, String decompFileName);

}
class Node {
    char ch;
    Node left;
    Node right;
}
