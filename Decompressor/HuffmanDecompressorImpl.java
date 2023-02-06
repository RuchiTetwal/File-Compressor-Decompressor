package Decompressor;

import java.io.*;
import java.util.logging.*;

public class HuffmanDecompressorImpl implements IDecompressor {

    Logger logger = Logger.getLogger(HuffmanDecompressorImpl.class.getName());
    private int bytesToInt(byte[] bytes) {
        int ans = 0;
        for (byte by : bytes) {
            ans = (ans << 8) + (by & 0xFF);
        }
        return ans;
    }

    public void decompress(String compFileName, String orgFileName, String decompFileName) throws IOException {

        try (FileInputStream compFileReader = new FileInputStream(compFileName);
            FileOutputStream decompFileWriter = new FileOutputStream(decompFileName)) {
            int compFileLength= compFileReader.available();

            byte[] headerSizeBytes = new byte[4];
            compFileReader.read(headerSizeBytes);
            int headerSize = bytesToInt(headerSizeBytes);

            byte[] bitsInLastByteCompFile = new byte[1];
            compFileReader.read(bitsInLastByteCompFile);

            byte[] orgFileSizeBytes = new byte[4];
            compFileReader.read(orgFileSizeBytes);
            int orgFileSize = bytesToInt(orgFileSizeBytes);

            byte[] header = new byte[headerSize];
            compFileReader.read(header);

            byte[] compFileArr = new byte[compFileLength - (4+1+4+headerSize)];
            compFileReader.read(compFileArr);

            IHuffmanDecompressorAlgo helper = new HuffmanDecompressorAlgoImpl();
            Node root = helper.generateHuffmanTree(header, headerSize);
            helper.getCharFromTreeWriteInDecompFile(decompFileWriter, root, compFileArr, bitsInLastByteCompFile[0], headerSize, orgFileSize);
            helper.compareOrgDecompFiles(orgFileName, decompFileName);

        }
    }
}

