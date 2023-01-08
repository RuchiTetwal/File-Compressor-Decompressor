package Decompressor;

import java.io.*;

public class DecompressorImpl implements IDecompressor{
    public void decompress(String compFileName,String orgFileName, String decompFileName) throws IOException{
     
        FileInputStream compFileReader = new FileInputStream(compFileName);
        byte[] compFileArr = new byte[compFileReader.available()];
        compFileReader.read(compFileArr);
        FileOutputStream DecompFile = new FileOutputStream(decompFileName);
      
        IHuffmanDecompressorAlgo helper = new HuffmanDecompressorAlgoImpl();

        int j=0;

        //Getting header size in bytes
        byte[] headerSizeBytes = new byte[4];
        for(int i=0;i<4;i++){
            headerSizeBytes[i]=compFileArr[j++];
        }
        int headerSize= helper.bytesToInt(headerSizeBytes);
             
        //Getting number of relevant bits in last byte of compressed file
        byte bitsInLastByteCompFile= compFileArr[j++];

        //Getting header information 
        byte[] header = new byte[headerSize];
        for(int i=0;i<headerSize;i++){
            header[i]=compFileArr[j++];
        }

        //Generating huffman tree from header information
        Node root = helper.generateHuffmanTree(header, headerSize);
        helper.GetCharFromTreeWriteInDecompFile(DecompFile, root, compFileArr, bitsInLastByteCompFile, j);           
        helper.CompareOrgDecompFiles(orgFileName, decompFileName);
           
        compFileReader.close();
        DecompFile.close();                   
    }
}
class Node {
    char ch;
    Node left;
    Node right;
}