package MineMap;

import Entity.Block;
import Entity.BlockStatus;

import java.util.ArrayList;
import java.util.Random;

public class MineMap {
    private int rowLength;
    private int colLength;
    private int numOfMine;
    private int checked;//记录已经翻开了多少个格子
    private int leftMine;//记录还有多少个雷没被发现
    private Block[][] blockMap;//比实际游玩地图大一圈，方便判断每个block周围有几个雷
    private boolean[][] map;//这个地图比实际游玩地图大一圈，目的是为了方便生成雷区进行判断，对应实际雷区格子的坐标从1到rowLength和colLength

    //这里的构造器只接受正确参数，排除错误参数应该在其他地方完成
    public MineMap(int custom, int rowLength, int colLength, int numOfMine){
        if (custom==0) {
            this.rowLength = rowLength;
            this.colLength = colLength;
            this.numOfMine = numOfMine;
            map = new boolean[this.rowLength + 2][this.colLength + 2];
        } else {
            switch (custom){
                case 1:
                    this.rowLength = 9;
                    this.colLength = 9;
                    this.numOfMine = 10;
                    map = new boolean[this.rowLength + 2][this.colLength + 2];
                    break;
                case 2:
                    this.rowLength = 16;
                    this.colLength = 16;
                    this.numOfMine = 40;
                    map = new boolean[this.rowLength + 2][this.colLength + 2];
                    break;
                case 3:
                    this.rowLength = 16;
                    this.colLength = 30;
                    this.numOfMine = 99;
                    map = new boolean[this.rowLength + 2][this.colLength + 2];
                    break;
            }
        }
        initializeMap1();//把map外圈埋雷
    }

    public void initializeMap1(){
        this.leftMine=numOfMine;
        for (int col=0; col<colLength+2; col++){
            map[0][col]=true;
            map[rowLength+1][col]=true;
        }//map的第一行和最后一行初始化（埋雷）
        for (int row=1; row<rowLength+1; row++){
            map[row][0]=true;
            map[row][colLength+1]=true;
        }//map的第一列和最后一列初始化（埋雷）
        initializeBlocks1();
    }//把数组map的最外一圈全部设为有雷
    public void initializeMap2(int initialRow, int initialCol){
        ArrayList<int[]> listOfMinedBlocks = new ArrayList<>(this.getNumOfMine());
        int[] temp;//用于储存当前的block的位置
        int row;//范围：1-rowLength
        int col;//范围：1-colLength
        Random random = new Random();
        for (int count=0; count<numOfMine;){
            row=random.nextInt(this.rowLength)+1;
            col=random.nextInt(this.colLength)+1;
            temp = new int[]{row, col};
            //if判断条件：（list为空 或者 list不含temp）并且 这个位置周围雷数（map）不是8 并且这个位置不是初始初始
            if ((listOfMinedBlocks.isEmpty() || (!containsPosition(listOfMinedBlocks,temp))) && validPosition(row,col) && ( row!=initialRow || col!=initialCol)){
                listOfMinedBlocks.add(temp);
                map[row][col]=true;
                count++;
            }
        }
        initializeBlocks2();
    }//把map里面正确地填起来,这个方法要等到第一次点击之后再调用
    public void initializeBlocks1(){
        blockMap = new Block[getRowLength()+2][getColLength()+2];
        for (int row=0; row<getRowLength()+2; row++){
            for (int col=0; col<getColLength()+2; col++){
                blockMap[row][col]=new Block(BlockStatus.CLICKED,false,row,col);
            }
        }
    }//把blocks全部初始化为已点击且无雷
    public void initializeBlocks2() {
        for (int row=1; row < rowLength+1; row++) {
            for (int col=1; col<colLength+1; col++){
                blockMap[row][col].setBlockState(BlockStatus.COVERED);//把有效的block全部设为COVERED
                blockMap[row][col].setHasMine(map[row][col]);//设置blockMap的有效部分的是否埋雷属性
            }
        }
        for (int row=1; row < rowLength+1; row++) {
            for (int col=1; col<colLength+1; col++){
                blockMap[row][col].setNeighborhoodMine(blockNeighborhood(row,col));//这句话正确地初始化了有效的block的周围雷数属性
            }
        }
        //上面两个循环把blockMap的里面初始化
        // 必须分开，因为第二个循环内的方法setNeighborhood方法要求周围的blocks的雷已经埋好

    }//用于把map的有效部分抄到blocks里面

    //以下是四个初始化的四个辅助方法
    public static boolean containsPosition(ArrayList<int[]> listOfMinedBlocks, int[] temp){
        for (int[] listOfMinedBlock : listOfMinedBlocks) {
            if (listOfMinedBlock[0] == temp[0] && listOfMinedBlock[1] == temp[1])
                return true;
        }
        return false;
    }
    public int mapNeighborhood(int row, int col){
        int counter=0;
        if (map[row-1][col-1])
            counter++;
        if (map[row-1][col])
            counter++;
        if (map[row-1][col+1])
            counter++;
        if (map[row][col-1])
            counter++;
        if (map[row][col+1])
            counter++;
        if (map[row+1][col-1])
            counter++;
        if (map[row+1][col])
            counter++;
        if (map[row+1][col+1])
            counter++;
        return counter;

    }//统计一个map位置相邻的雷数
    public boolean validPosition(int row, int col){
        if (mapNeighborhood(row,col)==8){
            return false;
        }else return true;
    }//判断一个map位置相邻的雷数是否为8，若是则不能放雷，否则可以放雷
    public int blockNeighborhood(int row, int col){
        int counter=0;
        if (blockMap[row-1][col-1].hasMine)
            counter++;
        if (blockMap[row-1][col].hasMine)
            counter++;
        if (blockMap[row-1][col+1].hasMine)
            counter++;
        if (blockMap[row][col-1].hasMine)
            counter++;
        if (blockMap[row][col+1].hasMine)
            counter++;
        if (blockMap[row+1][col-1].hasMine)
            counter++;
        if (blockMap[row+1][col].hasMine)
            counter++;
        if (blockMap[row+1][col+1].hasMine)
            counter++;
        return counter;
    }//统计一个block相邻的雷数

    public void displayMap(){
        for ( int row=1; row<=rowLength; row++){
            for (int col=1; col<=colLength; col++){
                System.out.print(map[row][col]?"*  ":"-  ");
            }
            System.out.println();
        }
    }//在控制台输出Boolean数组map，用于开发阶段检查map是否正确

    public boolean rightUpdateBlock(int row, int col){//判断玩家右击的格子是否有雷,有雷则把该block状态改为FLAG并返回true，无雷则把block状态改为WRONG并返回false
        checked++;
        if (blockMap[row][col].hasMine){
            leftMine--;
            blockMap[row][col].setBlockState(BlockStatus.FLAG);
            return true;
        }else {
            blockMap[row][col].setBlockState(BlockStatus.WRONG);
            return false;
        }
    }//检测右击的block是否有雷，若有则返回true，同时
    public boolean leftUpdateBlocks(int row, int col){//参数row，col表示更新源头block的坐标-----注意！输入前需要保证输入的坐标对应的block必须是COVERED
        if (!blockMap[row][col].hasMine){//没有雷
            blockMap[row][col].setBlockState(BlockStatus.CLICKED);
            if (blockMap[row][col].getNeighborhoodMine()==0) {
                ArrayList<int[]> update = new ArrayList<>(rowLength * colLength - numOfMine);
                int start = 0;
                int end = 0;
                int nextEnd = 0;
                int currentRow;
                int currentCol;
                update.add(blockMap[row][col].getPosition());
                for (; ; ) {
                    for (int current = start; current <= end; current++) {
                        currentRow=update.get(current)[0];
                        currentCol=update.get(current)[1];
                        if (needModification(currentRow-1, currentCol-1)) {
                            update.add(blockMap[currentRow-1][currentCol-1].getPosition());
                            nextEnd++;
                        }
                        if (needModification(currentRow-1, currentCol)) {
                            update.add(blockMap[currentRow-1][currentCol].getPosition());
                            nextEnd++;
                        }
                        if (needModification(currentRow-1, currentCol+1)) {
                            update.add(blockMap[currentRow-1][currentCol+1].getPosition());
                            nextEnd++;
                        }
                        if (needModification(currentRow, currentCol-1)) {
                            update.add(blockMap[currentRow][currentCol-1].getPosition());
                            nextEnd++;
                        }
                        if (needModification(currentRow, currentCol+1)) {
                            update.add(blockMap[currentRow][currentCol+1].getPosition());
                            nextEnd++;
                        }
                        if (needModification(currentRow+1, currentCol-1)) {
                            update.add(blockMap[currentRow+1][currentCol-1].getPosition());
                            nextEnd++;
                        }
                        if (needModification(currentRow+1, currentCol)) {
                            update.add(blockMap[currentRow+1][currentCol].getPosition());
                            nextEnd++;
                        }
                        if (needModification(currentRow+1, currentCol+1)) {
                            update.add(blockMap[currentRow+1][currentCol+1].getPosition());
                            nextEnd++;
                        }
                    }
                    if (nextEnd == end) {
                        break;
                    } else {
                        start = end + 1;
                        end = nextEnd;
                    }
                }
            } else {
                blockMap[row][col].setBlockState(BlockStatus.CLICKED);
                checked++;//被翻开的格子增加了
            }
            return false;
        }else {
            blockMap[row][col].setBlockState(BlockStatus.EXPLODED);
            leftMine--;
            return true;//有雷
        }
    }//用于每次玩家输入要左击翻开的格子后更新所有格子的blockState
    public boolean needModification(int row, int col){
        if (blockMap[row][col].getBlockState()== BlockStatus.COVERED){
            blockMap[row][col].setBlockState(BlockStatus.CLICKED);
            checked++;//被翻开的格子增加了
            if (blockMap[row][col].getNeighborhoodMine()==0){
                return true;
            }
        }
        return false;
    }

    public void displayBlocks(){
        for (int row=1; row<=rowLength; row++){
            for (int col=1; col<=colLength; col++){
                System.out.print(blockMap[row][col].toString()+"  ");
            }
            System.out.println();
        }
    }

    public int getRowLength() { return rowLength; }
    public int getColLength() { return colLength; }
    public int getNumOfMine() { return numOfMine; }
    public Block[][] getBlockMap() { return blockMap; }
    public boolean[][] getMap() { return map; }
    public int getChecked(){return checked;}
    public int getLeftMine(){return leftMine;}

    public static void main(String[] args) {
        MineMap oneMineMap = new MineMap(1,3,3,3);
        oneMineMap.initializeMap2(1,2);
        oneMineMap.displayMap();
        System.out.println();
        oneMineMap.leftUpdateBlocks(1,2);
        oneMineMap.displayBlocks();
    }
}
