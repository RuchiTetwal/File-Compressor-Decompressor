package Compressor;

public class ByteArrIntConversionImpl implements IbyteArrIntConversion{

    //function to convert int to byte array
    public byte[] intToBytes(int val)
    {
        byte[] ans = new byte[4];

        ans[0] = (byte) (val >> 24);
        ans[1] = (byte) (val >> 16);
        ans[2] = (byte) (val >> 8);
        ans[3] = (byte) (val /*>> 0*/);
        return ans;
    }

    //function to convert byte array to int
    public int bytesToInt(byte[] bytes){
        int ans=0;
        for (byte by : bytes) {
            ans = (ans << 8) + (by & 0xFF);
        }
        return ans;
    }
}
