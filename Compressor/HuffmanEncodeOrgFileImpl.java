package Compressor;

import java.util.*;
import java.io.*;

public class HuffmanEncodeOrgFileImpl implements IHuffmanEncodeOrgFile {

    private byte[] intToBytes(int val) {
        byte[] ans = new byte[4];
        ans[0] = (byte) (val >> 24);
        ans[1] = (byte) (val >> 16);
        ans[2] = (byte) (val >> 8);
        ans[3] = (byte) (val /* >> 0 */);
        return ans;
    }

    private byte bitsInLastByteComp=0;

    private byte[] getCompByteArr(byte[] orgFileList, Map<Byte, String> huffmanMap){
        StringBuilder strOrgFile = new StringBuilder();

        for (int i=0;i<orgFileList.length;i++) {
            strOrgFile.append(huffmanMap.get(orgFileList[i]));
        }

        byte[] compByte = new byte[(int) Math.ceil(strOrgFile.length() / 8.0)];

        StringBuilder temp = new StringBuilder();
        int j = 0;

        for (int i = 0; i < strOrgFile.length(); i++) {
            temp.append(strOrgFile.charAt(i));
            if (temp.length() == 8) {
                compByte[j++] = (byte)Integer.parseInt(temp.toString(), 2);
                temp.setLength(0);
            }
        }

        if (temp.length() != 0) {
            int n = temp.length();
            bitsInLastByteComp = (byte) n;
            for (int i = 0; i < (8 - n); i++) {
                temp.append('0');
            }
            compByte[j] = (byte) Integer.parseInt(temp.toString(), 2);
        }
        return compByte;
    }

    @Override
    // function for generating encoding of given file and writing encoded data to new compressed file
    public void encodeOrgFile(byte[] orgFileList, String compressedTextFile, Map<Byte, String> huffmanMap, byte[] header) throws IOException {      
        try (FileOutputStream compWriter = new FileOutputStream(compressedTextFile)) {

            int headerSize = header.length;
            byte[] headerSizeBytes = intToBytes(headerSize);
            byte[] orgFileSizeBytes =intToBytes(orgFileList.length);
            byte[] compByte = getCompByteArr(orgFileList, huffmanMap);
          
            // Writing Byte size of header information in compressed file
            compWriter.write(headerSizeBytes);      
            // Writing Number of relevant bits in Last byte of compressed file
            compWriter.write(bitsInLastByteComp);
            // Writing Original file size in bits
            compWriter.write(orgFileSizeBytes);
            // Writing header information to compressed file
            compWriter.write(header);
            // Writing encoded data to compressed file
            compWriter.write(compByte);
        }
    }
}
