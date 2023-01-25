package decompressor;

import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.*;

public class HuffmanDecompressorAlgoImpl implements IHuffmanDecompressorAlgo {

    Logger logger = Logger.getLogger(HuffmanDecompressorAlgoImpl.class.getName());

    // function to convert byte array to int
    private int bytesToInt(byte[] bytes) {
        int ans = 0;
        for (byte by : bytes) {
            ans = (ans << 8) + (by & 0xFF);
        }
        return ans;
    }

    @Override
    public int getHeaderSize(byte[] compFileArr) {
        int compFileIterator=0; //0-3 bytes 
        byte[] headerSizeBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            headerSizeBytes[i] = compFileArr[compFileIterator++];
        }
        return bytesToInt(headerSizeBytes);
    }

    // Getting number of relevant bits in last byte of compressed file
    @Override
    public byte getBitsInLastByteCompFile(byte[] compFileArr) {
        //4th byte
        return compFileArr[4];
    }

    @Override
    public int getOrgFileSize(byte[] compFileArr){
        int compFileIterator=5; //5-8 bytes 
        byte[] orgFileSizeBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            orgFileSizeBytes[i] = compFileArr[compFileIterator++];
        }
        return bytesToInt(orgFileSizeBytes);
    }

    // Getting header information
    @Override
    public byte[] getHeader(byte[] compFileArr, int headerSize) {
        int compFileIterator=9; //9th to 9+headersize
        byte[] header = new byte[headerSize];
        for (int i = 0; i < headerSize; i++) {
            header[i] = compFileArr[compFileIterator++];
        }
        return header;
    }

    @Override
    // function to generate huffman tree from header of compressed file
    public Node generateHuffmanTree(byte[] header, int headerSize) {
        Node root = null;
        if (headerSize == 0)
            return null;

        Stack<Node> stack = new Stack<>();

        int i = 0;
        StringBuilder headerStr = new StringBuilder();

        for (; i < header.length; i++) {
            String byteStr = String.format("%8s", Integer.toBinaryString(header[i] & 0xFF)).replace(' ', '0');
            headerStr.append(byteStr);
        }

        i = 0;

        while (i < headerStr.length()) {
            Node newNode = new Node();
            // if current bit is 1 reading next 8 bits and extracting char from them
            if (headerStr.charAt(i) == '1') {
                i++;
                newNode.ch = (char) (Integer.parseInt(headerStr.substring(i, i + 8), 2));
                newNode.left = null;
                newNode.right = null;

                i += 8;
            } else {
                // else if current bit is 0 and stack have only one node then entire tree is
                // constructed
                if (stack.size() == 1) {
                    root = stack.peek();
                    break;
                }
                // ele if current bit si 0 and stack have more than one node, create new node
                else {
                    Node right = stack.peek();
                    stack.pop();
                    Node left = stack.peek();
                    stack.pop();
                    newNode.ch = '-';
                    newNode.left = left;
                    newNode.right = right;
                }
                i++;
            }
            stack.push(newNode);
        }
        return root;
    }

    // function to handle last byte of compressed file
    private void handleLastByteOfCompFile(FileOutputStream decompFileWriter, Node root, Node currNode, byte[] compArr, byte bitsInLastByteComp, int compFileIterator, byte[] decompFileList, int decompFileIterator) throws IOException {
        // converting byte to binary string so that we can read each bit of current byte
        
        String tempStr = String.format("%8s", Integer.toBinaryString(compArr[compFileIterator] & 0xFF)).replace(' ',
                '0');

        // reading byte bit-by-bit
        for (byte i = 0; i < bitsInLastByteComp; i++) {
            // if current bit is 1 moving to right child of current node
            if (tempStr.charAt(i) == '1') {
                currNode = currNode.right;
            }
            // else if current bit is 0 moving to left child of current node
            else {
                currNode = currNode.left;
            }
            // if encountering a leaf node of huffman tree writing character of node to
            // decompressed file
            if (currNode.left == null && currNode.right == null) {
                decompFileList[decompFileIterator]=(byte)(currNode.ch);
                currNode = root;
            }
        }

   
        decompFileWriter.write(decompFileList);
    }

    @Override
    // Function to convert encoding to char and write them in decompressed file
    public void getCharFromTreeWriteInDecompFile(FileOutputStream decompFileWriter, Node root, byte[] compArr,
        byte bitsInLastByteComp,int headerSize) throws IOException {
        int compFileIterator=headerSize+9; //compArr[0-4] bytes for headersize(int), compArr[4]:1 byte for bitsInlastByteofComp(byte), headerSize number of bytes for headersize
        if (root == null) 
            return;

        logger.log(Level.INFO, "Generating Decompressed file....");

        Node currNode = root;

        byte[] decompFileList = new byte[getOrgFileSize(compArr)];

        int decompFileIterator=0;

        // Reading bytes of compressed data except last byte
        for (; compFileIterator < compArr.length - 1; compFileIterator++) {
            // converting byte to binary string so that we can read each bit of current byte
            String tempStr = String.format("%8s", Integer.toBinaryString(compArr[compFileIterator] & 0xFF)).replace(' ',
                    '0');
            // reading byte bit-by-bit
            for (int i = 0; i < 8; i++) {
                // if current bit is 1 moving to right child of current node
                if (tempStr.charAt(i) == '1') {
                    currNode = currNode.right;
                }
                // else if current bit is 0 moving to left child of current node
                else {
                    currNode = currNode.left;
                }
                // if encountering a leaf node of huffman tree writing character of node to decompressed file
                if (currNode.left == null && currNode.right == null) {
                    decompFileList[decompFileIterator++]=(byte)currNode.ch;
                    currNode = root;
                }
            }
        }
        
        // Handling last byte - (As Last byte of compressed file have only (bitsInLastByteComp) relevant bits)
        handleLastByteOfCompFile(decompFileWriter, root, currNode, compArr, bitsInLastByteComp, compFileIterator, decompFileList, decompFileIterator);
    }

    // Function to check whether two files of given path are equal or not
    private boolean areFilesEqual(Path file1, Path file2) {
        try {
            if (Files.size(file1) != Files.size(file2)) {
                return false;
            }

            byte[] f1 = Files.readAllBytes(file1);
            byte[] f2 = Files.readAllBytes(file2);

            return Arrays.equals(f1, f2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    // Function to compare files
    public void compareOrgDecompFiles(String orgFileName, String decompFileName) {
        // checking if decompressed file and original files are same or not
        String userDirectory = System.getProperty("user.dir");
        File orgFile = new File(userDirectory + '/' + orgFileName);
        File decompFile = new File(userDirectory + '/' + decompFileName);

        boolean equal = areFilesEqual(orgFile.toPath(), decompFile.toPath());
        if (equal) {
            logger.log(Level.INFO, "Original and decompressed files are equal.");
        } else {
            logger.log(Level.INFO, "Original and decompressed files are not equal.");
        }
    }

}
