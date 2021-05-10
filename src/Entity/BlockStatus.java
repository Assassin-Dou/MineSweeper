package Entity;

//这个类用来表示地雷格子可能有的所有状态
public enum BlockStatus {
    COVERED,//未点击
    CLICKED,//左击未触雷
    EXPLODED,//左击触雷
    FLAG,//正确标旗
    WRONG,//错误标旗
    QUESTION//问号，不过题目要求好像双人模式没有这个功能，如果有时间做单人再用吧
}
