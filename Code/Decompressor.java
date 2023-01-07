import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;

public class Decompressor {

        //Function to check whether two files of given path are equal or not
        public static boolean areFilesEqual(Path file1, Path file2)
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

        //Function to compare files 
        static void CompareOrgDecompFiles(String CompressedTextFile, String DecompressedTextFile){
            System.out.println("Comparing decompressed and original files....");
 
             String orgFileName = CompressedTextFile.substring(11);
             //checking if decompressed file and original files are same or not
             File orgFile = new File("C:/Users/India/OneDrive/Desktop/capillary/File Compressor Decompressor/"+orgFileName);
             File decompFile = new File("C:/Users/India/OneDrive/Desktop/capillary/File Compressor Decompressor/"+DecompressedTextFile);
  
             boolean equal = areFilesEqual(orgFile.toPath(), decompFile.toPath());
             if (equal) {
                  System.out.println("Original and decompressed files are equal.");
             }
             else {
              System.out.println("Original and decompressed files are not equal.");
             }
        }

        //function to convert byte array to int
        static int bytesToInt(byte[] bytes){
            int ans=0;
            for (byte by : bytes) {
                ans = (ans << 8) + (by & 0xFF);
            }
            return ans;
        }

        //function to generate huffmann tree from header of compressed file
        static Node generateHuffmanTree(byte[] header, int headerSize){
            Node root=null;

            if(headerSize==0)
                return null;

            Stack<Node> stack = new Stack<Node>();

            int i=0;

            StringBuilder headerStr= new StringBuilder();

            for(i=0;i<header.length;i++){                  
                
                String tempStr = String.format("%8s", Integer.toBinaryString(header[i] & 0xFF)).replace(' ', '0');  
                
                headerStr.append(tempStr);
            }

 
            i=0;

            while(i<headerStr.length()){
                Node newNode = new Node();
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


                    if(stack.size()==1){
                        root=stack.peek();
                        break;
                    }
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
        public static void HandleLastByteOfCompFile(FileOutputStream DecompFile, Node root, Node currNode, byte[] compArr,byte bitsInLastByteComp,int j) throws FileNotFoundException, IOException{
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


        //Function to convert encoding to char and write them in decompressed file
        public static void GetCharFromTreeWriteInDecompFile(FileOutputStream DecompFile, Node root, byte[] compArr, byte bitsInLastByteComp, int j) throws FileNotFoundException, IOException{


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
            HandleLastByteOfCompFile(DecompFile,root, currNode, compArr, bitsInLastByteComp, j);


        }


        public static void decodeCompFile(String CompressedTextFile, String DecompressedTextFile) throws FileNotFoundException, IOException{
        
         
             FileInputStream compFileReader = new FileInputStream(CompressedTextFile);
 
             FileOutputStream DecompFile = new FileOutputStream(DecompressedTextFile);
  
             byte[] compArr = new byte[compFileReader.available()];
            
             //reading data of compressed file
             compFileReader.read(compArr);

             int j=0;

             //Getting header size in bytes
             byte[] headerSizeBytes = new byte[4];
             for(int i=0;i<4;i++){
                headerSizeBytes[i]=compArr[j++];
             }

             int headerSize= bytesToInt(headerSizeBytes);
             
             //Getting number of relevant bits in last byte of compressed file
             byte bitsInLastByteComp= compArr[j++];

             //Getting header information 
             byte[] header = new byte[headerSize];

             for(int i=0;i<headerSize;i++){
                header[i]=compArr[j++];
             }
 
             //Generating huffman tree from header information
             Node root =generateHuffmanTree( header, headerSize);
 
             
            

             GetCharFromTreeWriteInDecompFile(DecompFile, root, compArr, bitsInLastByteComp, j);
             
             CompareOrgDecompFiles(CompressedTextFile, DecompressedTextFile);
 
             
 
             compFileReader.close();
             DecompFile.close();
         }

    public static void main(String args[]) throws FileNotFoundException,IOException{
        String compressedTextFile = args[0];
        String DecompressedTextFile="[decompressed]-"+compressedTextFile;
        decodeCompFile(compressedTextFile, DecompressedTextFile);
    }

}

class Node {

    char ch;
 
    Node left;
    Node right;
}