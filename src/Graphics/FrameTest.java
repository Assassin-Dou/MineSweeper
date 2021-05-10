package Graphics;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FrameTest {
    public static void main(String[] args) {
        Frame frame = new Frame();
        frame.setBackground(new Color(73, 232, 184, 255));
        frame.setVisible(true);
        frame.setBounds(400,300,600,600);

        //关闭窗口的监听器
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                //点击关闭时要做的事情
                System.exit(0);//正确关闭
            }
        });

        //尝试布局
        Button button1=new Button("button1");
        Button button2=new Button("button2");
        Button button3=new Button("button3");
        Button button4=new Button("button4");
        Button button5=new Button("button5");
        Button button6=new Button("button6");
        Button button7=new Button("button7");
        Button button8=new Button("button8");
        Button button9=new Button("button9");
        Button button10=new Button("button10");

        frame.add(button1,BorderLayout.CENTER);
        frame.add(button2,BorderLayout.NORTH);

        //frame.setLayout(new BorderLayout(BorderLayout.EAST));
    }
}
