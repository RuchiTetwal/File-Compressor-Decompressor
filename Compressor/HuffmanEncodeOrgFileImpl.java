package compressor;

import java.util.*;
import java.io.*;

public class HuffmanEncodeOrgFileImpl implements IHuffmanEncodeOrgFile {

    // function to convert int to byte array
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

        for (byte currChar : orgFileList) {
            strOrgFile.append(huffmanMap.get(currChar));
        }

        byte[] compByte = new byte[(int) Math.ceil(strOrgFile.length() / 8.0)];

        String temp = "";
        int j = 0;

        for (int i = 0; i < strOrgFile.length(); i++) {
            temp += (strOrgFile.charAt(i));
            if (temp.length() == 8) {
                int a = Integer.parseInt(temp, 2);
                byte b = (byte) a;
                compByte[j++] = b;
                temp = "";
            }
        }

        if (temp.length() != 0) {
            int n = temp.length();
            bitsInLastByteComp = (byte) n;
            for (int i = 0; i < (8 - n); i++) {
                temp += '0';
            }
            int a = Integer.parseInt(temp, 2);
            byte b = (byte) a;
            compByte[j] = b;
        }

        return compByte;
    }


    @Override
    // function for generating encoding of given file and writing encoded data to
    // new compressed file
    public void encodeOrgFile(byte[] orgFileList, String CompressedTextFile, Map<Byte, String> huffmanMap,
            byte[] header) throws IOException {
       
        try (FileOutputStream compWriter = new FileOutputStream(CompressedTextFile)) {

            int headerSize = header.length;
            byte[] headerSizeBytes = intToBytes(headerSize);
            byte[] compByte = getCompByteArr(orgFileList, huffmanMap);

            // Writing Byte size of header information in compressed file
            compWriter.write(headerSizeBytes);      
            // Writing Number of relevant bits in Last byte of compressed file
            compWriter.write(bitsInLastByteComp);
            // Writing header information to compressed file
            compWriter.write(header);
            // Writing encoded data to compressed file
            compWriter.write(compByte);
        }
    }
}
