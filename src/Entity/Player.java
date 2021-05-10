package Entity;

import java.util.Random;

//这个类用来表示一个玩家，这里玩家类似于账号，后续会添加某些账号属性
public class Player {
    private String userName;
    private int score;
    private int mistake;
    private static Random random = new Random();

    public Player(String userName){
        this.userName=userName;
        score=0;
        mistake=0;
    }//主动设置用户名
    public Player(){ userName = "User#"+(random.nextInt(9000)+1000); }
    //随机设置用户名，不过好像会有重复的可能，估计不会真的使用。如果要用随机方法，记得写一个player名单用来检查

    public void addScore(){score++;}
    public void minusScore(){score--;}
    public void addMistake(){mistake++;}

    public String getUserName(){return userName;}
    public int getScore(){ return score; }
    public int getMistake(){ return mistake; }
}
