package Decompressor;

import java.io.*;

public interface GenerateDecompressedFile {
    
    //function to handle last byte of compressed file
    public void HandleLastByteOfCompFile(FileOutputStream DecompFile, Node root, Node currNode, byte[] compArr,byte bitsInLastByteComp,int j);


    //Function to convert encoding to char and write them in decompressed file
    public void GetCharFromTreeWriteInDecompFile(FileOutputStream DecompFile, Node root, byte[] compArr, byte bitsInLastByteComp, int j);


    //Function to get data from compressed file and generate decompressed file
    public void decodeCompFile(String CompressedTextFile, String DecompressedTextFile);
}
