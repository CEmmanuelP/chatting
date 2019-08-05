package chatUnity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//오브젝트인풋스트림으로 서버로부터 인원수 받아서 출력해줘야함 //////////////////////////////////////////////////////////////
class Home2 extends JFrame implements ActionListener{
    String ip;
    String id;
    Font font;
    Color color;

    private JLabel jl1, jl2, jl3, jl4;
    ImageIcon img;

    private JButton jbLogout;
    private JButton jb2;
    private JButton jb3;


    private JPanel jp1 = new JPanel();



    public Home2(String ip,String id,Font font,Color color) {
        this.ip = ip;
        this.id = id;
        this.font = font;
        this.color = color;

        draw();

        jl1 = new JLabel("Multi Chat");
        jl2 = new JLabel("\'닉네임\' 님 로그인하셨습니다.");
        jl3 = new JLabel("현재 접속중인 인원은 \'list.getSize()\' 명 입니다.");


        jbLogout = new JButton("Logout");
        jbLogout.addActionListener(this);

        jb2 = new JButton("chat");
        jb3 = new JButton("Memo");

        jl1.setSize(330,70);
        jl1.setLocation(100,30);
        jl1.setFont(new Font("버다나",Font.BOLD,30));

        jl2.setSize(320,30);
        jl2.setLocation(20,380);
        jl2.setFont(new Font("본고딕",Font.BOLD,22));

        jl3.setSize(320,30);
        jl3.setLocation(20,430);
        jl3.setFont(new Font("본고딕",Font.BOLD,15));

        jl4.setSize(300,300);
        jl4.setLocation(20, 100);

        jb2.setSize(160,40);
        jb2.setLocation(10,515);
        jb2.setFont(new Font("본고딕",Font.BOLD,15));
        jb2.addActionListener(this);

        jb3.setSize(160,40);
        jb3.setLocation(175,515);
        jb3.setFont(new Font("본고딕",Font.BOLD,15));
        jb3.addActionListener(this);

        jbLogout.setSize(80,30);
        jbLogout.setLocation(250, 0);


        this.add(jl1);
        this.add(jbLogout);
        this.add(jb2);
        this.add(jb3);


        jp1.add(jl2);
        jp1.add(jl3);


        setLayout(null);
        setSize(360,600);
        setVisible(true);

        jp1.setSize(340,120);
        //	jp1.setBackground(Color.BLUE);
        jp1.setLocation(0,410);
        jp1.setVisible(true);
        this.add(jp1,"panel1");
        jp1.setLayout(new FlowLayout());

        Toolkit tk = Toolkit.getDefaultToolkit();
        //Toolkit의 getScreenSize() 메서드를 사용해서 스크린사이즈를 담은 Dimension 객체를 리턴받는다.
        Dimension d = tk.getScreenSize();
        int screenHeight = d.height;
        int screenWidth = d.width;
        this.setLocation((screenWidth - this.getWidth())/2, (screenHeight - this.getHeight())/2);
        this.setResizable(false);
        this.setVisible(true);

    }



    private void draw() {
        // TODO Auto-generated method stub

        img = new ImageIcon("chat.png");

        jl4 = new JLabel(null, img, JLabel.CENTER);
        jl4.setVerticalTextPosition(JLabel.CENTER);
        jl4.setHorizontalTextPosition(JLabel.RIGHT);

        this.add(jl4);



    }



    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        if(e.getSource()==jbLogout) {

            int respon = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?",  "로그아웃", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if(respon == 0) {
                this.setVisible(false);
                new Screen();}
            else if (respon ==1){
                //	JOptionPane.showMessageDialog(null, "취소되었습니다.");
            }
        }

        if(e.getSource() == jb2){
            this.setVisible(false);
            try {
                new Chat(ip,id,font,color);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        if(e.getSource() == jb3){
            this.setVisible(true);
            new Memo(ip,id,font,color);
        }

    }

}

public class Home {

    public static void main(String[] args) {


    }

}