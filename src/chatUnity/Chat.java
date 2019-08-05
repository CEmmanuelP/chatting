package chatUnity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class Chat implements ActionListener{
    private Socket socket;//원통
    private ObjectInputStream ois;//받는용
    private ObjectOutputStream oos;//출력용
    private JFrame jframe;
    private JTextField jtf;
    private JLabel jlb1, jlb2;
    private JPanel jp1, jp2;
    private String ip;
    private String id;
    private Font font;
    private Color color;
    private JButton jbtn, jb1,jb2;
    private JTextPane jtp;
    private JFrame votef;
    private JButton btagree, btdisagree;


//생성자 ? IP(연결하려는 서버IP) 와 ID를 인자로 받는다.

    public Chat(String ip, String id,Font font,Color color) throws IOException{
        this.ip = ip;
        this.id = id;
        this.font = font;
        this.color = color;
        //////////////////////////////////////////////////////////////////////

        socket = new Socket(ip, 1771);   //서버에 연결하는 소켓 객체 생성
        System.out.println("connected..");
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

//MultiClientThread 객체를 생성하면서 자신(MultiClient)을 인자로 넘긴다.
        MultiClientThread ct = new MultiClientThread(this);
//MultiClientThread 스레드를 시작한다.
        Thread t = new Thread(ct);
        t.start();


        ///////////////////////////////////////////////System.out.println(1);

        jframe = new JFrame("Multi Chatting");
        jtf = new JTextField(30);
        jtp = new JTextPane();
        jtp.setPreferredSize(new Dimension(100, 500));
        ///////////////////////////////////////////////System.out.println(3);
        jb1 = new JButton(new ImageIcon("left-arrow.png"));
        jb2 = new JButton(new ImageIcon("group.png"));
        jlb1 = new JLabel("          Multi Chat");
        jlb1.setFont(new Font("본고딕",Font.BOLD,25));
        jbtn = new JButton("send");
        /////////////////////////////////////////////System.out.println(5);
        jp1 = new JPanel();
        jp2 = new JPanel();
        jtp.setBackground(Color.pink);

        jp1.setLayout(new BorderLayout());
        jp2.setLayout(new BorderLayout());

        jp1.add(jbtn, BorderLayout.EAST);
        jp1.add(jtf, BorderLayout.CENTER);

        jp2.add(jb1, BorderLayout.WEST);
        jp2.add(jlb1,BorderLayout.CENTER);
        jp2.add(jb2, BorderLayout.EAST);
        //////////////////////////////////////////////////System.out.println(9);
        jframe.add(jp1, BorderLayout.SOUTH);
        jframe.add(jp2, BorderLayout.NORTH);
        /////////////////////////////////////////////System.out.println(10);

        JScrollPane jsp = new JScrollPane(jtp, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jframe.add(jsp, BorderLayout.CENTER);
        //////////////////////////////////////////System.out.println(2);

        jtf.addActionListener(this);     //JTextField에 ActionListener연결
        jbtn.addActionListener(this);  //JButton에 ActionListener 연결
//JFrame에 WindowListener를 연결하는데, WindowsAdapter 익명클래스를 생성하면서 필요한 메서드들(windowClosing(), windowOpened())을 오버라이딩 했다.

        jframe.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                try{
//윈도우를 닫을 때 사용자가 종료 메세지를 보낸 것과 동일하게 문자열을 만들어 소켓에 기록한다.
                    oos.writeObject(id+"#exit");
                }catch(IOException ee){
                    ee.printStackTrace();
                }
                System.exit(0);   //어플리케이션 종료
            }
            public void windowOpened(WindowEvent e){
//윈도우가 열리면 JTextField에 커서를 둔다.
                jtf.requestFocus();//키 이벤트를 받을 컴포넌트를 강제로 설정(창이열리면 글쓰는공간에 포커스맞춰짐)
            }
        });
        //jta.setEditable(false);   //JTextArea 를 읽기전용으로
        jtp.setEditable(false);	  //JTextpane을 읽기전용으로
//스크린의 사이즈를 얻어오기 위해서 Toolkit 객체를 생성한다.
        Toolkit tk = Toolkit.getDefaultToolkit();
//Toolkit의 getScreenSize() 메서드를 사용해서 스크린사이즈를 담은 Dimension 객체를 리턴받는다.
        Dimension d = tk.getScreenSize();
        int screenHeight = d.height;
        int screenWidth = d.width;
        jframe.pack();   //JFrame의 사이즈를 서브콤포넌트들에 맞게 자동으로 조정해준다.
        jframe.setLocation((screenWidth - jframe.getWidth())/2, (screenHeight - jframe.getHeight())/2);
        jframe.setResizable(false);
        jframe.setVisible(true);
        ////////////////////////////////////////////////////디자인끝~~~~~~~~~~///////////////////////////////////////////////////////////////////
    }




    //ActionListener 인터페이스가 강제하는 메서드
    public void actionPerformed(ActionEvent e){
//ActionEvent의 getSource() 메서드는 이벤트를 발생시킨 객체 자체를 리턴시켜 준다.
        Object obj = e.getSource();
        String msg = jtf.getText();  //JTextField에서 값을 읽어오는 getText() 메서드
//ActionListener에 연결한 객체가 두 개 이상이므로 IF문을 사용해 구분하였다.
//JTextField의 요청일 경우
        if(obj == jtf || obj == jbtn){
            if(msg == null || msg.length()==0){   //메세지 내용이 없을 경우
//Alert창 : JOptionPane.showMessageDialog(소속되는 상위객체, 메세지객체, 메세지창제목, 메세지창종류)
                JOptionPane.showMessageDialog(jframe, "글을 쓰세요", "경고", JOptionPane.WARNING_MESSAGE);
            }else{   //메세지 내용이 있을 경우
                try{
                    oos.writeObject(id+"#"+msg);   //메세지를 직렬화 하여 소켓에 기록		//id#msg
                }catch(IOException ee){
                    ee.printStackTrace();
                }

                jtf.setText("");   //기록한 후에는 입력창을 지우고 다시 입력받을 준비를 한다.
            }
//JButton의 요청일 경우 (종료버튼)
        }else if(obj == jb1){

        }else if(obj == jb2){

        }
    }

    public void exit(){
        System.exit(0);
    }


    public ObjectInputStream getOis(){
        return ois;
    }

    public JTextPane getJtp() {
        return jtp;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public String getId(){
        return id;
    }

    public void append(String s) {
        try {
            Document doc = jtp.getDocument();
            doc.insertString(doc.getLength(), s, null);
        }catch(BadLocationException exc) {
            exc.printStackTrace();
        }
    }



    public void vote() {
        votef = new JFrame("강퇴투표");
        votef.setBounds(900, 500, 130, 100);
        btagree = new JButton("찬성");
        btdisagree = new JButton("반대");
        votef.add(btagree, BorderLayout.WEST);
        votef.add(btdisagree, BorderLayout.EAST);
        votef.setVisible(true);
        btagree.addActionListener(this);
        btdisagree.addActionListener(this);


    }

    public static void main(String[] args) {

    }


}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////밑에는 클라이언트쓰레드
class MultiClientThread extends Thread{
    private Chat c;

    public MultiClientThread(Chat c) {
        this.c = c;
    }


    public void run() {
        String message = null;
        String[] receivedMsg = null;
        boolean isStop = false;

        while(!isStop) {
            try {
                message = (String)c.getOis().readObject();
                receivedMsg = message.split("#");//eugene#/vote you
                //  wmessage = receivedMsg[1].split(" ");// /vote you
            }catch(Exception e) {
                e.printStackTrace();
                isStop = true;
            }

            System.out.println(receivedMsg[0] + " : " + receivedMsg[1]);//eugene : hi

            if(receivedMsg[1].equals("exit")) {
                if(receivedMsg[0].equals(c.getId())) {
                    c.exit();
                }else {
                    c.append(receivedMsg[0] + "님이 종료하셨습니다." + System.getProperty("line.separator"));//줄바꿈(OS상관없이)
                    c.getJtp().setCaretPosition(c.getJtp().getDocument().getLength());
                }
            }else{
                c.append(receivedMsg[0] + " : " + receivedMsg[1] + System.getProperty("line.separator"));
                c.getJtp().setCaretPosition(c.getJtp().getDocument().getLength());
            }//if - end
        }//while - end
    }
}

