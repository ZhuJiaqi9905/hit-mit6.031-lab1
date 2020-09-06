package P1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MagicSquare {
    private static int rowNum;//行数
    private static int columnNum;//列数
    private static int[][] magicSquare;

    /**
     * 先读文件，然后判断是否为magicsquare
     * @param fileName
     * @return
     */
    public static boolean isLegalMagicSquare(String fileName){
        boolean readSuccess = false;
        try{
            readSuccess = readFile(fileName);
        }catch (FileNotFoundException fe){
            fe.printStackTrace();
            return false;
        }catch(IOException ioe){
            ioe.printStackTrace();
            return false;
        }catch(NumberFormatException nfe){
            nfe.printStackTrace();
            return false;
        }
        if(readSuccess == false) return false;
        return judgeMagicSquare();
    }
    private static boolean judgeMagicSquare(){
        int magicSum = 0;
        for(int m: magicSquare[0]) {
            magicSum += m;
        }
        //检查每行
        for(int i = 1; i < magicSquare.length; ++i) {
            int rowSum = 0;
            for(int m : magicSquare[i]) {
                rowSum += m;
            }
            if(rowSum != magicSum) {
                return false;
            }
        }
        //检查每列
        for(int i = 0; i < magicSquare.length; ++i) {
            int columnSum = 0;
            for(int j = 0;j < magicSquare.length; ++j) {
                columnSum += magicSquare[i][j];
            }
            if(columnSum != magicSum) {
                return false;
            }
        }
        //检查每个对角线
        int lcSum = 0; int rcSum = 0;
        for(int i = 0; i < magicSquare.length; ++i) {
            lcSum += magicSquare[i][i];
            rcSum += magicSquare[i][magicSquare.length - i - 1];
        }
        return lcSum == magicSum && rcSum == magicSum;
    }

    /**
     * 读入文件，要处理各种异常情况，并把异常再抛出
     * 注意抛异常前要把reader给close
     * @param fileName
     * @return
     * @throws IOException
     */
    private static boolean readFile(String fileName) throws IOException{
        rowNum = columnNum = 0;
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(new File(fileName)));

        String line = null;
        try{
            line = reader.readLine();
        }catch (IOException e){
            reader.close();
            throw e;
        }

        if(line == null){
            rowNum = columnNum = 0;
            reader.close();
            return true;
        }
        String[] nums = line.split("\t");
        columnNum = nums.length;
        magicSquare = new int[columnNum][];//由于行列号相同，可以这样做
        magicSquare[rowNum] = new int[columnNum];
        for(int i = 0; i < columnNum; ++i){
            try{
                magicSquare[rowNum][i] = Integer.parseInt(nums[i]);
            }catch (NumberFormatException e){
                reader.close();
                throw e;
            }

        }
        try{
            while((line = reader.readLine()) != null){
                rowNum++;
                if(rowNum > columnNum) {
                    reader.close();
                    return false;
                }
                nums = line.split("\t");
                if(nums.length != columnNum){
                    reader.close();
                    return false;
                }
                magicSquare[rowNum] = new int[columnNum];
                for(int i = 0; i < columnNum; ++i){
                    magicSquare[rowNum][i] = Integer.parseInt(nums[i]);
                }
            }
        }finally {
            reader.close();
        }
        rowNum++;
        return rowNum == columnNum;

    }
    public static boolean generateMagicSquare(int n) {
        int magic[][] = new int[n][n];
        int row = 0, col = n / 2, i, j, square = n * n;
        for (i = 1; i <= square; i++) {
            magic[row][col] = i;
            if (i % n == 0)
                row++;
            else {
                if (row == 0)
                    row = n - 1;
                else
                    row--;
                if (col == (n - 1))
                    col = 0;
                else
                    col++;
            }
        }
        BufferedWriter writer = null;
        try  {
            writer = new BufferedWriter(new FileWriter(new File("src/P1/txt/6.txt")));
            for(i = 0; i < n; ++i){
                StringBuilder sb = new StringBuilder();
                sb.append(magic[i][0]);
                for(j = 1; j < n; ++j){
                    sb.append('\t');
                    sb.append(magic[i][j]);
                }
                writer.write(sb.toString());
                writer.newLine();
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++)
                System.out.print(magic[i][j] + "\t");
            System.out.println();
        }
        return true;
    }
    private boolean writeMagicSquare(int n){
        if(n < 0){
            System.out.println("n can not be negative");
            return false;
        }
        if( n % 2 == 0){
            System.out.println("n can not be even");
            return false;
        }
        return generateMagicSquare(n);
    }
    public static void main(String[] args) {
        MagicSquare ms = new MagicSquare();
        String rootPath = "src/P1/txt/";
        
        System.out.println(isLegalMagicSquare(rootPath + "1.txt"));
        System.out.println(isLegalMagicSquare(rootPath + "2.txt"));
        System.out.println(isLegalMagicSquare(rootPath + "3.txt"));
        System.out.println(isLegalMagicSquare(rootPath + "4.txt"));
        System.out.println(isLegalMagicSquare(rootPath + "5.txt"));



        generateMagicSquare(5);
        System.out.println(isLegalMagicSquare(rootPath + "6.txt"));
    }
}

