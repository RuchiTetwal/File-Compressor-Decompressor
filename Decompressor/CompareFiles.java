package Decompressor;

import java.nio.file.Path;

public interface CompareFiles {
    
    //Function to check whether two files of given path are equal or not
    public boolean areFilesEqual(Path file1, Path file2);

    //Function to compare files 
    public void CompareOrgDecompFiles(String CompressedTextFile, String DecompressedTextFile);

}
