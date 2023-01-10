package compressor;

import java.io.*;
import java.util.*;

public class HuffmanCompressionAlgoImpl implements IHuffmanCompressionAlgo {

    @Override
    public Map<Byte, Integer> countFreq(byte[] orgFileList) {
        HashMap<Byte, Integer> freqMap = new HashMap<>();

        // storing frequency of each char in hashmap
        for (byte currChar : orgFileList) {
            if (freqMap.containsKey(currChar)) {
                freqMap.put(currChar, freqMap.get(currChar) + 1);
            } else {
                freqMap.put(currChar, 1);
            }
        }
        return freqMap;
    }

    @Override
    public PriorityQueue<HuffmanNode> generateMinHeap(Map<Byte, Integer> freqMap) {
        PriorityQueue<HuffmanNode> minHeap = new PriorityQueue<>(freqMap.size(), new Comp());

        for (Map.Entry<Byte, Integer> element : freqMap.entrySet()) {
            Byte ch = element.getKey();
            int chFreq = element.getValue();
            // creating a Huffman node object and adding it to the priority queue
            HuffmanNode node = new HuffmanNode();
            node.ch = ch;
            node.freq = chFreq;
            node.left = null;
            node.right = null;
            // adding node to queue
            minHeap.add(node);
        }
        return minHeap;
    }

    @Override
    public HuffmanNode generateTree(PriorityQueue<HuffmanNode> minHeap) {
        HuffmanNode root = null;
        while (minHeap.size() > 1) {
            HuffmanNode min1 = minHeap.peek();
            minHeap.poll();
            HuffmanNode min2 = minHeap.peek();
            minHeap.poll();

            HuffmanNode newNode = new HuffmanNode();
            newNode.freq = min1.freq + min2.freq;
            newNode.ch = '-';

            // extracting 2 minimum frequency nodes form min-heap
            newNode.left = min1;
            newNode.right = min2;
            root = newNode;
            // adding new node back to minHeap
            minHeap.add(newNode);
        }
        return root;
    }

    // function to generate huffman codes for each unique character and storing them
    // in a hashmap(huffmanMap)
    private void generateCode(HuffmanNode root, String str, Map<Byte, String> huffmanMap ) {
        if (root == null)
            return;
        
        generateCode(root.left, str + "0", huffmanMap);
        generateCode(root.right, str + "1", huffmanMap);

        if (root.left == null && root.right == null) {
            huffmanMap.put(root.ch, str);          
        }
    }

    @Override
    public Map<Byte, String> generateHuffmanCode(HuffmanNode root) {
        Map<Byte, String> huffmanMap = new HashMap<>();
        generateCode(root, "", huffmanMap);
        return huffmanMap;
    }
    

    // Postorder traversal of huffman tree for generating header information
    private StringBuilder generatePostOrder(HuffmanNode root, StringBuilder postOrderString) {
        if (root == null) {
            return postOrderString;
        }
        generatePostOrder(root.left, postOrderString);
        generatePostOrder(root.right, postOrderString);

        if (root.left == null && root.right == null) {
            postOrderString.append('1');
            String temp = Integer.toBinaryString(root.ch & 0xFF);
            int n = temp.length();

            for (int i = n; i < 8; i++) {
                temp = '0' + temp;
            }
            postOrderString.append(temp);
        } else {
            postOrderString.append('0');
        }
        return postOrderString;
    }

    // function for generating header information using postorder traversal of
    // huffman encoding tree
    private byte[] generateHeader(HuffmanNode root) {

        StringBuilder postOrderString = new StringBuilder();
        generatePostOrder(root, postOrderString);

        String postOrderStr = postOrderString.toString();
        postOrderStr += '0';

        byte[] header = new byte[(int) Math.ceil(postOrderStr.length() / 8.0)];

        StringBuilder temp = new StringBuilder();

        int j = 0;

        for (int i = 0; i < postOrderStr.length(); i++) {
            temp.append(postOrderStr.charAt(i));
            if (temp.length() == 8) {
                String str = temp.toString();
                int a = Integer.parseInt(str, 2);
                byte b = (byte) a;
                header[j++] = b;
                temp.setLength(0);
            }
        }

        if (temp.length() != 0) {
            int n = temp.length();
            for (int i = 0; i < (8 - n); i++) {
                temp.append('0');
            }
            String str = temp.toString();
            int a = Integer.parseInt(str, 2);
            byte b = (byte) a;
            header[j] = b;
        }
        return header;
    }

    @Override
    public void encodeOrgFile(byte[] orgFileList, String compFileName, Map<Byte, String> huffmanMap, HuffmanNode root)
            throws IOException {
        byte[] header = generateHeader(root);

        IHuffmanEncodeOrgFile encoder = new HuffmanEncodeOrgFileImpl();
        encoder.encodeOrgFile(orgFileList, compFileName, huffmanMap, header);
    }
}
