package decompressor;
import java.io.*;

public interface IHuffmanDecompressorAlgo {
 
    //function to generate huffmann tree from header of compressed file
    public Node generateHuffmanTree(byte[] header, int headerSize);
 
    //Function to convert encoding to char and write them in decompressed file
    public void getCharFromTreeWriteInDecompFile(FileOutputStream DecompFile, Node root, byte[] compArr, byte bitsInLastByteComp)throws IOException;

    //Function to compare Original and Decompressed File
    public void compareOrgDecompFiles(String orgFileName, String decompFileName);

    //Function to get header size in number of bytes
    public int getHeaderSize(byte[] compFileArr);

    //Function to get header information
    public byte[] getHeader(byte[] compFileArr, int headerSize);

    //Function to get number of relevant bits in last byte of compressed file
    public byte getBitsInLastByteCompFile(byte[] compFileArr);

}
