package compressor;

import java.util.*;
import java.io.*;

import static java.lang.System.*;

public class HuffmanCompressorImpl implements Icompressor {

    public void compress(String orgFileName, String compFileName) throws IOException {

        try (FileInputStream orgFileReader = new FileInputStream(orgFileName)) {
            byte[] orgFileList = new byte[orgFileReader.available()];
            orgFileReader.read(orgFileList);

            IHuffmanCompressionAlgo helper = new HuffmanCompressionAlgoImpl();
            Map<Byte, Integer> freqMap = helper.countFreq(orgFileList);
            PriorityQueue<HuffmanNode> minHeap = helper.generateMinHeap(freqMap);
            HuffmanNode root = helper.generateTree(minHeap);
            Map<Byte, String> huffmanMap = helper.generateHuffmanCode(root);
            File compressedFile = new File(compFileName);
            out.println("File created: " + compressedFile.getName());
            helper.encodeOrgFile(orgFileList, compFileName, huffmanMap, root);

        }
    }
}

class HuffmanNode {
    int freq;
    byte ch;
    HuffmanNode left;
    HuffmanNode right;
}

class Comp implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode n1, HuffmanNode n2) {
        return n1.freq - n2.freq;
    }
}
