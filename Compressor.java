import java.util.*;
import java.lang.Math;
import java.io.*;

public class Compressor {

        public static byte bitsInLastByteComp=0;
        public static StringBuilder postOrderString= new StringBuilder();

        //function to convert int to byte array
        static byte[] intToBytes(int val)
        {
            byte[] ans = new byte[4];

            ans[0] = (byte) (val >> 24);
            ans[1] = (byte) (val >> 16);
            ans[2] = (byte) (val >> 8);
            ans[3] = (byte) (val /*>> 0*/);
            return ans;
        }

        //function to convert byte array to int
        static int bytesToInt(byte[] bytes){
            int ans=0;
            for (byte by : bytes) {
                ans = (ans << 8) + (by & 0xFF);
            }
            return ans;
        }


        //function to generate huffman codes for each unique character and storing them in a hashmap(huffmanMap)
        public static void generateCode(HuffmanNode root, String str, HashMap<Byte, String> huffmanMap)
        {
            if(root == null)
                return;
            
            if (root.left == null && root.right == null) {


                huffmanMap.put(root.ch, str);
     
                return;
            }            

            generateCode(root.left, str + "0", huffmanMap);
           
            generateCode(root.right, str + "1", huffmanMap);
        }


    //function to generate Huffman Tree using Min-Heap(Priority Queue)
    public static HuffmanNode generateTree(PriorityQueue<HuffmanNode> minHeap)
    {
        HuffmanNode root=null;
        while (minHeap.size()>1) {

            
            HuffmanNode min1 = minHeap.peek();
            minHeap.poll();

        
            HuffmanNode min2 = minHeap.peek();
            minHeap.poll();

    
            HuffmanNode newNode = new HuffmanNode();

    
            newNode.freq = min1.freq + min2.freq;
            newNode.ch = '-';

            //extracting 2 minimum frequency nodes form min-heap 
            newNode.left = min1;
            newNode.right = min2;

        
            root = newNode;

            // adding new node back to minHeap
            minHeap.add(newNode);

        }
        return root;
    }


        //Postorder traversal of huffmann tree for generating header information
        public static void generatePostOrder(HuffmanNode root){
          

            if(root==null){
                return ;
            }
            generatePostOrder(root.left);
            generatePostOrder(root.right);

            if(root.left==null && root.right==null){

                postOrderString.append('1');
                String temp= Integer.toBinaryString(root.ch & 0xFF );
                int n=temp.length();

                for(int i=n;i<8;i++){
                   temp='0'+temp;
                }
                postOrderString.append(temp);

            }
            else{
                postOrderString.append('0');
        
            }
           
        }


        //function for generating header information using postorder traversal of huffmann encoding tree
        public static byte[] generateHeader(HuffmanNode root){

            generatePostOrder(root);

            String postOrderStr= postOrderString.toString();
            postOrderStr+='0';


            byte[] header= new byte[(int) Math.ceil(postOrderStr.length()/8.0)];


            String temp="";
            int j=0;
            for(int i=0;i<postOrderStr.length();i++){

                temp+=(postOrderStr.charAt(i));

                if(temp.length()==8){

                    int a=Integer.parseInt(temp,2);
                    byte b=(byte)a;
                    header[j++]=b;
                    temp="";

                }
            }
            
            if(temp.length()!=0){

                int n=temp.length();

                for(int i=0;i<(8-n);i++){
                    temp+='0';
                }

        
                int a=Integer.parseInt(temp,2);
                byte b=(byte)a;
                header[j++]=b;
            
            }

            return header;
        }

        

        //function for genrating encoding of given file and writing encoded data to new compressed file
        public static void encodeOrgFile(byte[] orgFileList, String CompressedTextFile, HashMap<Byte, String> huffmanMap,HuffmanNode root )throws FileNotFoundException, IOException{
            StringBuilder strOrgFile = new StringBuilder();
        
            FileOutputStream compWriter =new FileOutputStream(CompressedTextFile);

            byte[] header = generateHeader(root);

            int headerSize = header.length;
            byte[] headerSizeBytes = intToBytes(headerSize);

            //Writing Byte size of header information in compressed file
            compWriter.write(headerSizeBytes);

            for(byte currChar:orgFileList){
                strOrgFile.append(huffmanMap.get(currChar));
                
            }

            byte[] compByte= new byte[(int) Math.ceil(strOrgFile.length()/8.0)];

            String temp = "";

            int j=0;


            for(int i=0;i<strOrgFile.length();i++){

                temp+=(strOrgFile.charAt(i));

                if(temp.length()==8){

                    int a=Integer.parseInt(temp,2);
                    byte b=(byte)a;
                    compByte[j++]=b;
                    temp="";
                }
            }

             if(temp.length()!=0){

                int n=temp.length();

                bitsInLastByteComp=(byte)n;

                for(int i=0;i<(8-n);i++){
                    temp+='0';
                }

                

                int a=Integer.parseInt(temp,2);
                byte b=(byte)a;
                compByte[j++]=b;
            
            }


            //Writing Number of relevant bits in Last byte of compressed file
            compWriter.write(bitsInLastByteComp);

            //Writing header information to compressed file
            compWriter.write(header);

            //Wrting encoded data to comppressed file
            compWriter.write(compByte);

            // byte by= 10;
            //     String te = String.format("%8s", Integer.toBinaryString(by & 0xFF)).replace(' ', '0'); 
            // System.out.println(te);

            //System.out.println("Bit count of original file: " + orgBitCount );
            //System.out.println("Bit count of new compresses file: " + compBitCount);
            //System.out.println("Percentage of compression: " + (int)(((double)compBitCount/orgBitCount) * 100) + "%");

            compWriter.close();
        }

        

        public static void main (String args[]) throws FileNotFoundException,IOException{
    
            
            FileInputStream orgFileReader = new FileInputStream("OriginalTextFile.txt");

            //Hashmap for storing frequencies of each unique character of original file
            HashMap<Byte, Integer> freqMap = new HashMap<Byte, Integer>();

            byte[] orgFileList = new byte[orgFileReader.available()];

            //reading data from original file in byte array
            orgFileReader.read(orgFileList);

  
            //storing frequency of each char in hashmap
            for(byte currChar:orgFileList){
 
                if(freqMap.containsKey(currChar)){
                    freqMap.put(currChar, freqMap.get(currChar)+1);
                }
                else{
                    freqMap.put(currChar,1);
                }

            }

                  

            //generating a min-heap using priority queue 

            PriorityQueue<HuffmanNode> minHeap = new PriorityQueue<HuffmanNode>(freqMap.size(), new comp());
        
        
            for (Map.Entry<Byte,Integer> element : freqMap.entrySet()) {

                Byte ch= element.getKey();
                int chFreq = element.getValue();

                // creating a Huffman node object and adding it to the priority queue
                 HuffmanNode node = new HuffmanNode();

                node.ch = ch;
                node.freq = chFreq;

                node.left = null;
                node.right = null;

                //adding node to queue
                minHeap.add(node);
            }


            //----Creating huffmann tree using min heap

            //Creating root node of huffmann tree
            HuffmanNode root = generateTree(minHeap);

            //Extracting 2 min. frequecy value from the heap and adding them as single node and pushing pack to priority queue
            
            HashMap<Byte, String> huffmanMap = new HashMap<Byte, String>();

            //generating huffmann code for each character by tree traversal
            generateCode(root, "", huffmanMap);

           
            Boolean isCompExist=false;

            File CompressedFile = new File("CompressedTextFile.txt");

            if (CompressedFile.createNewFile()) {
                 System.out.println("File created: " + CompressedFile.getName());
            } 
            else {
                isCompExist=true;
                System.out.println("Compressed File already exists.");
            }

        
            if(!isCompExist)
                encodeOrgFile(orgFileList,"CompressedTextFile.txt" , huffmanMap,root);
  

            orgFileReader.close();
        }
    
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
