package decompressor;

import java.io.*;
import java.util.logging.*;

public class HuffmanDecompressorImpl implements IDecompressor {

    Logger logger = Logger.getLogger(HuffmanDecompressorImpl.class.getName());

    public void decompress(String compFileName, String orgFileName, String decompFileName) throws IOException {

        try (FileInputStream compFileReader = new FileInputStream(compFileName);
                FileOutputStream decompFileWriter = new FileOutputStream(decompFileName)) {

            byte[] compFileArr = new byte[compFileReader.available()];
            compFileReader.read(compFileArr);

            IHuffmanDecompressorAlgo helper = new HuffmanDecompressorAlgoImpl();
            int headerSize = helper.getHeaderSize(compFileArr);
            byte bitsInLastByteCompFile = helper.getBitsInLastByteCompFile(compFileArr);
            byte[] header = helper.getHeader(compFileArr, headerSize);
            Node root = helper.generateHuffmanTree(header, headerSize);
            helper.getCharFromTreeWriteInDecompFile(decompFileWriter, root, compFileArr, bitsInLastByteCompFile);
            helper.compareOrgDecompFiles(orgFileName, decompFileName);

        }
    }
}

class Node {
    char ch;
    Node left;
    Node right;
}