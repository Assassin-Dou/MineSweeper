package GameControl;

import Entity.*;
import MineMap.MineMap;

import java.util.Scanner;


public class GameControl {
    //这个类用于实现双人游玩的游戏流程控制
    //TODO:第一次点击之后立即调用生成地图方法，并且调用检测是否为零方法去更新翻开区域
    private Player player1;
    private Player player2;
    private Player playerOnTurn;
    private MineMap mineMap;
    private int turnNum;
    private int turnCounter;
    private int clickType;//点击类型，1代表左击，3代表右击
    private int currentRow;
    private int currentCol;
    private Scanner input = new Scanner(System.in);


    public boolean mapSettingValidation(int mod, int rowLength, int colLength, int mineNum){
        if (mod==0){
            if (rowLength>0 && rowLength<=24 && colLength>0 && colLength<=30 && mineNum<=0.5*rowLength*colLength){
                return true;
            }else return false;
        }else if (mod==1 || mod==2 || mod==3){
            return true;
        }else return false;
    }//判断mineMap的构造参数是否合法

    public boolean checkTurn(){
        turnCounter--;
        if (turnCounter==0){
            return true;
        }else return false;
    }//更新当前玩家剩余次数并判断是否回合结束,回合结束则输出true，否则输出false
    public void changeTurn(){
        if (playerOnTurn==player1){
            setPlayerOnTurn(player2);
        }else {
            setPlayerOnTurn(player1);
        }
        turnCounter=turnNum;
    }//更新回合

    public int checkResult(){
        if (player1.getScore()-player2.getScore()>mineMap.getLeftMine()){
            return 1;
        }else if (player2.getScore()-player1.getScore()>mineMap.getLeftMine()){
            return 2;
        }else if (player1.getScore()-player2.getScore()==mineMap.getLeftMine()){
            if (player1.getMistake()< player2.getMistake()){
                return 1;
            }
        }else if (player2.getScore()-player1.getScore()==mineMap.getLeftMine()){
            if (player2.getMistake()< player1.getMistake()){
                return 2;
            }
        }
        if (mineMap.getLeftMine()==0){
            if (player1.getScore()> player2.getScore()){
                return 1;
            }else if (player2.getScore()> player1.getScore()){
                return 2;
            }else if (player1.getMistake()<player2.getMistake()){
                return 1;
            }else if (player2.getMistake()< player1.getMistake()){
                return 2;
            }else return 3;
        }
        return 0;
    }//检查是否有玩家获胜,没有则返回0，玩家1获胜则返回1，玩家2获胜则返回2，平局返回3
    public void displayResult(int result){
        if (result == 1) {
            System.out.printf("游戏结束！%n%s获胜！%n对局数据：%n", player1.getUserName());
        } else if (result == 2) {
            System.out.printf("游戏结束！%n%s获胜！%n对局数据：%n", player2.getUserName());
        } else if (result == 3) {
            System.out.printf("平局！%n对局数据：%n");
        }
        displayScore();
        System.out.println("最终地图：");
        mineMap.displayBlocks();
    }//显示对局结果及数据

    public void displayScore(){
        System.out.printf("%-15s:     得分:%3d     失误:%3d%n", player1.getUserName(),player1.getScore(),player1.getMistake());
        System.out.printf("%-15s:     得分:%3d     失误:%3d%n", player2.getUserName(),player2.getScore(),player2.getMistake());
    }
    public void displayTurn(){
        System.out.printf("现在是 %s的回合. 剩余次数:%d.%n", playerOnTurn.getUserName(),turnCounter);
    }
    public void displayCurrent(){
        mineMap.displayBlocks();
        displayScore();
        displayTurn();
        System.out.println("请输入要翻开的坐标和点击方式(row, col, type-----type为1代表左击，3代表右击):");
    }

    public boolean positionValidation(){
        return getCurrentRow()<=mineMap.getRowLength() && getCurrentCol()<=mineMap.getColLength() && mineMap.getBlockMap()[currentRow][currentCol].blockStatus==BlockStatus.COVERED;
    }//判断玩家输入的位置是否正确


    public void gameFlow(){
        do {
            displayCurrent();
            do {
                System.out.printf("请%s输入：", playerOnTurn.getUserName());
                setCurrentRow(input.nextInt());
                setCurrentCol(input.nextInt());
                setClickType(input.nextInt());
            }while (!positionValidation());//让玩家输入翻看哪个block,只有输入的参数有效时才往下运行，否则一直让玩家输入
            if (clickType==1){
                if (mineMap.leftUpdateBlocks(getCurrentRow(),getCurrentCol())){
                    playerOnTurn.addMistake();//左击触雷减分
                }//左击没触雷不加分
            }else {
                if (mineMap.rightUpdateBlock(getCurrentRow(),getCurrentCol())){
                    playerOnTurn.addScore();//右击触雷加分
                }else playerOnTurn.addMistake();//右击没触雷减分
            }
            if (checkTurn()){
                changeTurn();
            }

        }while (checkResult()==0);
        //代码运行到这里说明分出胜负了，下面显示结果
        displayResult(checkResult());
    }


    //todo:写一个判断玩家输入的坐标是否已被翻开的方法

    public void initializePlayer1(String name) { player1=new Player(name); }
    public void initializePlayer2(String name) { player2=new Player(name); }
    public void setPlayerOnTurn(Player playerOnTurn) { this.playerOnTurn = playerOnTurn; }
    public void setTurnNum(int turnNum) { this.turnNum = turnNum; }
    public void setTurnCounter(int turnCounter) { this.turnCounter = turnCounter; }
    public void setClickType(int clickType) { this.clickType = clickType; }
    public void setCurrentRow(int currentRow) { this.currentRow = currentRow; }
    public void setCurrentCol(int currentCol) { this.currentCol = currentCol; }


    public int getTurnNum() { return turnNum;}
    public int getClickType() { return clickType; }
    public int getCurrentRow() { return currentRow; }
    public int getCurrentCol() { return currentCol; }


    public static void main(String[] args) {
        //todo:加上try、catch等
        //首先让玩家输入必要的设置参数
        Scanner scanner = new Scanner(System.in);
        GameControl gameControl = new GameControl();

        System.out.print("请输入P1的名称：");
        gameControl.initializePlayer1(scanner.next());
        System.out.print("请输入P2的名称：");
        gameControl.initializePlayer2(scanner.next());
        gameControl.setPlayerOnTurn(gameControl.player1);

        for (;;) {
            System.out.println("请输入地图设置：\n参数格式：模式 行数 列数 雷数");
            System.out.println("模式：’0‘代表自定义，’1‘为初级，’2‘为中级，’3’为高级");
            System.out.println("行数，列数和雷数：若模式为初级，中级或高级，则可输入任意int；若模式为自定义，则行数不超过24，列数不超过30，雷数不超过总格数的一半");
            int mod=scanner.nextInt();
            int rowLength=scanner.nextInt();
            int colLength=scanner.nextInt();
            int mineNum=scanner.nextInt();
            if(gameControl.mapSettingValidation(mod,rowLength,colLength,mineNum)){
                gameControl.mineMap = new MineMap(mod, rowLength, colLength, mineNum);
                break;
            }
        }//输入地图参数

        System.out.print("请输入每回合玩家需要翻开的格数(范围：1-5)：");
        gameControl.setTurnNum(scanner.nextInt());
        gameControl.setTurnCounter(gameControl.getTurnNum());
        System.out.println("游戏开始！");
        System.out.println("输入格式：行数   列数   点击方式\n行数：范围从1到总行数\n列数：范围从1到总列数\n点击方式：1代表左键，3代表右键");


        do{
            System.out.printf("注意，第一次必须左击%n");
            System.out.printf("请%s输入初始位置：", gameControl.playerOnTurn.getUserName());
            gameControl.setCurrentRow(scanner.nextInt());
            gameControl.setCurrentCol(scanner.nextInt());
            gameControl.setClickType(scanner.nextInt());
        }while ((!(gameControl.getCurrentRow()<=gameControl.mineMap.getRowLength() && gameControl.getCurrentCol()<=gameControl.mineMap.getColLength())) || gameControl.getClickType()!=1);
        gameControl.mineMap.initializeMap2(gameControl.getCurrentRow(),gameControl.getCurrentCol());
        gameControl.mineMap.leftUpdateBlocks(gameControl.getCurrentRow(),gameControl.getCurrentCol());
        if (gameControl.checkTurn()){
            gameControl.changeTurn();
        }
        gameControl.gameFlow();
    }



}
