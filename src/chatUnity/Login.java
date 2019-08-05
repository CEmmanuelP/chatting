package chatUnity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;
import java.awt.*;

//소켓만들어서 서버로 입력한 정보들 보내서 저장시켜야함 오브젝트 아웃풋스트림 (다른사람들한테도 내 아이디 설정적용된걸로 나오게 하기 위함)//////////////////////////////////////////////
class Screen extends JFrame implements ActionListener, ItemListener, Runnable{
    private JLabel jl1, jl2, jl3, jl4, jl5;
    private JComboBox jcb1;
    private JButton jb1, jbLogin;

    public static JTextField jtf1= new JTextField("닉네임 입력");
    static Font font1;
    static Color color1;

    Home link1;
    String ip, id;

    String[] font = {"글꼴을 고르세요", "굴림", "궁서", "바탕", "돋움", "맑은 고딕"};

    JPanel jp1 = new JPanel();

    public Screen(){

        jl1 = new JLabel("Multi Chat");
        jl2 = new JLabel("Nickname : ");
        jl3 = new JLabel("Font : ");
        jl4 = new JLabel("Color : ");

        jl5 = new JLabel(jtf1.getText());
        jl5.setFont(new Font("굴림", Font.BOLD, 25));
        jcb1 = new JComboBox(font);
        jcb1.addItemListener(this);
        jb1 = new JButton("색을 골라주세요");
        jbLogin = new JButton("LogIn");

        jb1.addActionListener(this);
        jbLogin.addActionListener(this);

        jl1.setSize(330, 70);
        jl1.setLocation(100, 30);
        jl1.setFont(new Font("버다나", Font.BOLD, 30));

        jl2.setSize(120, 30);
        jl2.setLocation(30, 180);
        jl2.setFont(new Font("본고딕", Font.BOLD, 19));

        jl3.setSize(120, 30);
        jl3.setLocation(30, 250);
        jl3.setFont(new Font("본고딕", Font.BOLD, 19));

        jl4.setSize(120, 30);
        jl4.setLocation(30, 315);
        jl4.setFont(new Font("본고딕", Font.BOLD, 19));

        jtf1.setSize(154, 40);
        jtf1.setLocation(150, 175);
        jtf1.setFont(new Font("굴림", Font.BOLD, 15));

        jcb1.setSize(150, 40);
        jcb1.setLocation(150,245);
        jcb1.setFont(new Font("본고딕", Font.BOLD, 15));

        jb1.setSize(150, 40);
        jb1.setLocation(150,315);
        jb1.setFont(new Font("본고딕", Font.BOLD, 15));

        jbLogin.setSize(320, 40);
        jbLogin.setLocation(10,515);
        jbLogin.setFont(new Font("본고딕", Font.BOLD, 15));


        this.add(jl1);
        this.add(jl2);
        this.add(jl3);
        this.add(jl4);
        this.add(jtf1);
        this.add(jcb1);
        this.add(jb1);
        this.add(jbLogin);

        setLayout(null);
        setSize(360, 600);
        setVisible(true);

        jp1.setSize(340, 60);
        jp1.setLocation(0, 420);
        jp1.setVisible(true);
        this.add(jp1,"panel1");
        jp1.setLayout(new FlowLayout());
        jp1.add(jl5);

        Toolkit tk = Toolkit.getDefaultToolkit();
        //Toolkit의 getScreenSize() 메서드를 사용해서 스크린사이즈를 담은 Dimension 객체를 리턴받는다.
        Dimension d = tk.getScreenSize();
        int screenHeight = d.height;
        int screenWidth = d.width;
        this.setLocation((screenWidth - this.getWidth())/2, (screenHeight - this.getHeight())/2);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {


        if(e.getSource() == jb1){
            color1 = JColorChooser.showDialog(null, "나를 표현할 색을 골라보세요.", Color.CYAN);
            if(color1 != null){
                jl5.setForeground(color1);
            }
        }

        if(e.getSource() ==jbLogin) {

            if(jl5.getText().length() == 0){
                JOptionPane.showMessageDialog(jtf1, "닉네임을 입력하세요.");
                jtf1.setText("닉네임 입력");
            }
            else {
                int respon  = JOptionPane.showConfirmDialog(jbLogin, "위와 같은 설정내용으로 \n  채탱방에 입장하시겠습니까?", "로그인", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(respon == 0) {
                    this.setVisible(false);
                    new Home("localhost",jtf1.getText(),font1,color1);
                } else if (respon == 1) {
                    //	JOptionPane.showMessageDialog(jbLogin, "취소되었습니다.");
                }
            }
        }
    }

    public void itemStateChanged(ItemEvent e){
        if(e.getItem().equals("궁서")){
            jl5.setFont(new Font("궁서", Font.BOLD, 25));
            font1 = new Font("궁서", Font.BOLD, 25);
        };

        if(e.getItem().equals("바탕")){
            jl5.setFont(new Font("바탕", Font.BOLD, 25));
            font1 = new Font("바탕", Font.BOLD, 25);
        }

        if(e.getItem().equals("굴림")){
            jl5.setFont(new Font("굴림", Font.BOLD, 25));
            font1 = new Font("굴림", Font.BOLD, 25);
        }

        if(e.getItem().equals("돋움")){
            jl5.setFont(new Font("돋움", Font.BOLD, 25));
            font1 = new Font("돋움", Font.BOLD, 25);
        }
        if(e.getItem().equals("맑은 고딕")){
            jl5.setFont(new Font("맑은 고딕", Font.BOLD, 25));
            font1 = new Font("맑은 고딕", Font.BOLD, 25);
        }

    }

    public void run(){
        while(jl5 != null){
            jl5.setText(jtf1.getText());
            String nick = jl5.getText();
            char[] name = new char[10];
            name = nick.toCharArray();

            if(name.length > 7){
                JOptionPane.showMessageDialog(jtf1, "닉네임은 8글자 이하로 설정 가능합니다");
                jtf1.setText("닉네임 입력");
            }

        }
    }
}




public class Login {
    public static void main(String[] args){
        Screen sc = new Screen();
        Thread th1 = new Thread(sc);
        th1.start();


    }
}