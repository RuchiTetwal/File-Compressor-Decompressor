package Decompressor;

import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class HuffmanDecompressorAlgoImpl implements IHuffmanDecompressorAlgo{

    @Override
    //function to convert byte array to int
    public int bytesToInt(byte[] bytes){
        int ans=0;
        for (byte by : bytes) {
            ans = (ans << 8) + (by & 0xFF);
        }
        return ans;
    }

    @Override
    //function to generate huffmann tree from header of compressed file
    public Node generateHuffmanTree(byte[] header, int headerSize){
        Node root=null;
        if(headerSize==0)
            return null;

        Stack<Node> stack = new Stack<Node>();

        int i=0;
        StringBuilder headerStr= new StringBuilder();

        for(i=0;i<header.length;i++){                             
            String byteStr = String.format("%8s", Integer.toBinaryString(header[i] & 0xFF)).replace(' ', '0');            
            headerStr.append(byteStr);
        }

        i=0;

        while(i<headerStr.length()){
            Node newNode = new Node();
            //if current bit is 1 reading next 8 bits and extracting char from them
            if(headerStr.charAt(i)=='1'){
                i++;                              
                String binaryChar=headerStr.substring(i,i+8);
                char ch= (char) (Integer.parseInt(binaryChar, 2) );
                
                newNode.ch=ch;
                newNode.left=null;
                newNode.right=null;
          
                i+=8;
            }
            else{
                //else if current bit is 0 and stack have only one node then entire tree is constructed
                if(stack.size()==1){
                    root=stack.peek();
                    break;
                }
                //ele if current bit si 0 and stack have more then one nodes, create new node
                else{
                    Node right = stack.peek();
                    stack.pop();
                    Node left = stack.peek();
                    stack.pop();
                    newNode.ch='-';
                    newNode.left=left;
                    newNode.right=right;                  
                }                 
                i++;
            }
            stack.push(newNode);
        }
        return root;
    } 

    //function to handle last byte of compressed file
    private void handleLastByteOfCompFile(FileOutputStream DecompFile, Node root, Node currNode, byte[] compArr,byte bitsInLastByteComp,int j) throws FileNotFoundException, IOException{
        //converting byte to binary string so that we can read each bit of current byte
        String tempStr = String.format("%8s", Integer.toBinaryString(compArr[j] & 0xFF)).replace(' ', '0');
      
        //reading byte bit-by-bit
        for(byte i=0;i<bitsInLastByteComp;i++){
           //if current bit is 1 moving to right child of current node 
            if (tempStr.charAt(i)=='1') {
                currNode=currNode.right;
            }
            //else if current bit is 0 moving to left child of current node
            else {
                currNode=currNode.left;
            }
            //if encountering a leaf node of huffman tree wrting character of node to decompressed file
            if(currNode.left==null && currNode.right==null){
               DecompFile.write(currNode.ch);       
               currNode=root;
            }
        }
    }
    
    @Override
    //Function to convert encoding to char and write them in decompressed file
    public void GetCharFromTreeWriteInDecompFile(FileOutputStream DecompFile, Node root, byte[] compArr, byte bitsInLastByteComp, int j) throws IOException{
        if(root==null)
             return;
        System.out.println("Generating Decompressed file....");

        Node currNode=root;

        //Reading bytes of compressed data except last byte
        for(;j<compArr.length-1;j++){
            //converting byte to binary string so that we can read each bit of current byte
            String tempStr = String.format("%8s", Integer.toBinaryString(compArr[j] & 0xFF)).replace(' ', '0');           
            //reading byte bit-by-bit
            for(int i=0;i<8;i++){          
                //if current bit is 1 moving to right child of current node 
                if (tempStr.charAt(i)=='1') {
                    currNode=currNode.right;
                }
                //else if current bit is 0 moving to left child of current node
                else {
                    currNode=currNode.left;
                } 
                //if encountering a leaf node of huffman tree wrting character pf node to decompressed file
                if(currNode.left==null && currNode.right==null){           
                    DecompFile.write(currNode.ch);
                    currNode=root;
                }
            }            
        }
        //Handling last byte - (As Last byte of compressed file have only (bitsInLastByteComp) relevant bits) 
        handleLastByteOfCompFile(DecompFile,root, currNode, compArr, bitsInLastByteComp, j);
    }

    //Function to check whether two files of given path are equal or not
    private boolean areFilesEqual(Path file1, Path file2)
    {
        try {
             if (Files.size(file1) != Files.size(file2)) {
                    return false;
        }

        byte[] f1 = Files.readAllBytes(file1);
        byte[] f2 = Files.readAllBytes(file2);

        return Arrays.equals(f1, f2);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    //Function to compare files 
    public void CompareOrgDecompFiles(String orgFileName, String DecompFileName){
        System.out.println("Comparing decompressed and original files....");

        String userDirectory = System.getProperty("user.dir");
    
         //checking if decompressed file and original files are same or not
         File orgFile = new File(userDirectory+"/"+orgFileName);
         File decompFile = new File(userDirectory+"/"+DecompFileName);

         boolean equal = areFilesEqual(orgFile.toPath(), decompFile.toPath());
         if (equal) {
              System.out.println("Original and decompressed files are equal.");
         }
         else {
          System.out.println("Original and decompressed files are not equal.");
         }
    }
}
