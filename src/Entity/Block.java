package Entity;

import java.util.Arrays;

//这个类用来表示一个地雷格子
public class Block {
    public BlockStatus blockStatus;
    public boolean hasMine;
    public int row;//坐标从0开始算（因为blocks用数组储存）
    public int col;//坐标从0开始算
    public int neighborhoodMine;//相邻的地雷数

    public Block(BlockStatus blockStatus, boolean hasMine, int row, int col){
        this.blockStatus = blockStatus;
        this.hasMine=hasMine;
        this.row =row;
        this.col = col;
    }


    public void setNeighborhoodMine(int neighborhoodMine){ this.neighborhoodMine=neighborhoodMine; }
    public void setBlockState(BlockStatus blockStatus) { this.blockStatus = blockStatus; }
    public boolean isHasMine() {
        return hasMine;
    }
    public void setHasMine(boolean hasMine) {
        this.hasMine = hasMine;
    }
    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public int getNeighborhoodMine() {
        return neighborhoodMine;
    }

    public int[] getPosition(){ return new int[]{row, col}; }
    public BlockStatus getBlockState() { return blockStatus; }

    @Override
    public String toString(){//输出结果根据玩家点击的结果确定
        switch (blockStatus){
            case COVERED:
                return "#";
            case CLICKED:
                return Integer.toString(neighborhoodMine);
            case FLAG:
                return "F";
            case EXPLODED:
            case WRONG:
                return "X";
            default:return "!";
        }
    }


    public static void main(String[] args) {
        Block aBlock = new Block(BlockStatus.COVERED,false,2,3);
        System.out.println(Arrays.toString(aBlock.getPosition()));
    }
}
