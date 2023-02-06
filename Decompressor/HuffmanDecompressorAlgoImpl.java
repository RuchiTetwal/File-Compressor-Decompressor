package Decompressor;

import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.*;

public class HuffmanDecompressorAlgoImpl implements IHuffmanDecompressorAlgo {

    Logger logger = Logger.getLogger(HuffmanDecompressorAlgoImpl.class.getName());

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
            headerStr.append(Integer.toBinaryString((header[i] & 0xFF) + 0x100).substring(1));
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
                // else if current bit is 0 and stack have only one node then entire tree is constructed
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

    private byte[] handleLastByteOfCompFile(Node root, Node currNode, byte[] compArr, byte bitsInLastByteComp, int compFileIterator, byte[] decompFileList, int decompFileIterator) throws IOException {
        // converting byte to binary string so that we can read each bit of current byte
        String tempStr = Integer.toBinaryString((compArr[compFileIterator] & 0xFF) + 0x100).substring(1);

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
            // if encountering a leaf node of huffman tree writing character of node to decompressed file
            if (currNode.left == null && currNode.right == null) {
                decompFileList[decompFileIterator]=(byte)(currNode.ch);
                currNode = root;
            }
        }

        return decompFileList;
    }

    @Override
    // Function to convert encoding to char and write them in decompressed file
    public void getCharFromTreeWriteInDecompFile(FileOutputStream decompFileWriter, Node root, byte[] compArr, byte bitsInLastByteComp, int headerSize, int orgFileSize) throws IOException {
        int compFileIterator=0, decompFileIterator=0;
        if (root == null) 
            return;

        logger.log(Level.INFO, "Generating Decompressed file....");

        Node currNode = root;

        byte[] decompFileList = new byte[orgFileSize];

        // Reading bytes of compressed data except last byte
        for (; compFileIterator < compArr.length - 1; compFileIterator++) {
            // converting byte to binary string so that we can read each bit of current byte
            String tempStr = Integer.toBinaryString((compArr[compFileIterator] & 0xFF) + 0x100).substring(1);
            // reading bytes bit-by-bit
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
        byte [] newDecompFileList = handleLastByteOfCompFile(root, currNode, compArr, bitsInLastByteComp, compFileIterator, decompFileList, decompFileIterator);
   
        decompFileWriter.write(newDecompFileList);
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

