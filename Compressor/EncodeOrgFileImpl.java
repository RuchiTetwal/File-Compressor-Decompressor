package Compressor;

import java.util.*;
import java.io.*;

public class EncodeOrgFileImpl implements IencodeOrgFile {

    public byte bitsInLastByteComp=0;
    public StringBuilder postOrderString= new StringBuilder();


    IbyteArrIntConversion byteIntObj = new ByteArrIntConversionImpl();
    

    //Postorder traversal of huffmann tree for generating header information
    public void generatePostOrder(HuffmanNode root){
          

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
    public byte[] generateHeader(HuffmanNode root){

        generatePostOrder(root);

        String postOrderStr= postOrderString.toString();
        postOrderStr+='0';


        byte[] header= new byte[(int) Math.ceil(postOrderStr.length()/8.0)];


        StringBuilder temp = new StringBuilder();

        int j=0; 

        for(int i=0;i<postOrderStr.length();i++){

            temp.append(postOrderStr.charAt(i));

            if(temp.length()==8){

                String str=temp.toString();
                int a=Integer.parseInt(str,2);
                byte b=(byte)a;
                header[j++]=b;
                temp.setLength(0);

            }
        }
        
        if(temp.length()!=0){

            int n=temp.length();

            for(int i=0;i<(8-n);i++){
                temp.append('0');
            }

            String str=temp.toString();
            int a=Integer.parseInt(str,2);
            byte b=(byte)a;
            header[j++]=b;
        
        
        }

        return header;
    }



    //function for genrating encoding of given file and writing encoded data to new compressed file
    public void encodeOrgFile(byte[] orgFileList, String CompressedTextFile, Map<Byte, String> huffmanMap,HuffmanNode root ) throws IOException{
        StringBuilder strOrgFile = new StringBuilder();
    
        FileOutputStream compWriter = new FileOutputStream(CompressedTextFile);

        byte[] header = generateHeader(root);

        int headerSize = header.length;
        byte[] headerSizeBytes = byteIntObj.intToBytes(headerSize);

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

        
        compWriter.close();
        
    }

}
