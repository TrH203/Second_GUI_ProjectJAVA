package project_app_dau_tien;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
public class My_NOTE extends Thread{

    JFrame f = new JFrame("My Note");
    JMenuBar menuBar;
    JPopupMenu pp = new JPopupMenu("Edit");
    JTextArea textA = new JTextArea("Xin chào, đây là app Mynote đầu tiên của tôi, "
                                   +"Tôi vẫn đang hoàn thiện.\n"
                                   +"Còn nhiều phím và chức năng chưa bấm được,"
                                   + "ví dụ như undo và redo.\n"
                                   + "Hết!!!!");
    JTextField textB;
    ArrayList<String> undoText;
    int last=0;
    void myFrame() {
        undoText = new ArrayList<String>();
        undoText.add(textA.getText());
        textA.addKeyListener(new myLis_undoText());
        f.setLayout(new BorderLayout());
        menuBar = new JMenuBar();
        JMenuItem cut = new JMenuItem("Cut");
        JMenuItem copy = new JMenuItem("Copy");
        JMenuItem paste = new JMenuItem("Paste");
        JMenuItem del = new JMenuItem("Delete");
        del.addActionListener(new popup_Tool());
        cut.addActionListener(new popup_Tool());
        paste.addActionListener(new popup_Tool());
        copy.addActionListener(new popup_Tool());
        pp.add(cut);
        pp.add(copy);
        pp.add(paste);
        pp.add(del);
        JMenu f_menu = new JMenu("File");
        JMenuItem newMenu = new JMenuItem("New");
        newMenu.addActionListener(new myLis_tool_New());
        f_menu.add(newMenu);
        JMenuItem openMenu = new JMenuItem("Open");
        openMenu.addActionListener(new myLis_tool_Open());
        f_menu.add(openMenu);
        f_menu.addSeparator();
        JMenuItem saveMenu = new JMenuItem("Save");
        saveMenu.addActionListener(new myLis_tool_Save());
        f_menu.add(saveMenu);
        JMenuItem exitMenu = new JMenuItem("Exit");
        exitMenu.addActionListener(new myLis_Exit());
        f_menu.add(exitMenu);
        JMenu s_menu = new JMenu("Help");
        JMenu web_help = new JMenu("Visit Web");
        web_help.add(new JMenuItem("Hien.com"));
        web_help.add(new JMenuItem("help.com.vn"));
        s_menu.add(web_help);
        s_menu.add(new JMenuItem("Create Report"));
        JMenu e_menu = new JMenu("Edit");
        e_menu.add(new JMenuItem("Copy"));
        e_menu.add(new JMenuItem("Paste"));
        e_menu.add(new JMenuItem("Delete"));
        JToolBar tool = new JToolBar();
        JButton b1 = button_tool("image/open2.png", "Open","Open");
        b1.addActionListener(new myLis_tool_Open());
        JButton b2 = button_tool("image/save.png.jpeg", "Save","Save");
        b2.addActionListener(new myLis_tool_Save());
        JButton b3 = button_tool("image/save2.png", "Save as","Save as");
        b3.addActionListener(new myLis_tool_Save());
        JButton b4 = button_tool("image/new.png", "New", "New");
        b4.addActionListener(new myLis_tool_New());       
        JButton b6 = button_tool("image/undo.png", "Undo", "Undo"); 
        b6.addActionListener(new myLis_Undo_Button());              
        textB = new JTextField();
        JButton b7 = button_tool("image/search2.png","Search","Search");
        b7.addActionListener(new myLis_search());
        JButton b8 = button_tool("image/exit.png","Exit","Exit");
        b8.addActionListener(new myLis_Exit());
        tool.add(b1);
        tool.add(b2);
        tool.add(b3);
        tool.add(b4);       
        tool.add(b6);     
        tool.add(textB);
        tool.add(b7);
        tool.add(b8);
        menuBar.add(f_menu);
        menuBar.add(e_menu);
        menuBar.add(s_menu);
        JPanel p1 = new JPanel(new GridLayout(2, 2));
        p1.add(menuBar);
        p1.add(tool);
        f.add(p1, BorderLayout.NORTH);
        textA.addMouseListener(new myLis_textA());            
        JScrollPane scp = new JScrollPane(textA);
        f.add(scp, BorderLayout.CENTER);        
        f.setSize(550, 700);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    static JButton button_tool(String s, String s2,String s3) {
        ImageIcon ic = new ImageIcon(s);
        Image ic_1 = ic.getImage();
        Image ic_2 = ic_1.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ic = new ImageIcon(ic_2);
        JButton b1 = new JButton(ic);
        b1.setToolTipText(s2);
        b1.setActionCommand(s3);
        return b1;
    }
    String saveB="";
    File file;
    class myLis_tool_Open implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(f);         
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                try {                  
                    textA.setText(Read_openFile(file));
                } catch (FileNotFoundException ex) {
                }
            }
        }
    }
    class myLis_tool_New implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            textA.setText("");
            textB.setText("");         
            file = null;
            undoText.removeAll(undoText);
        }       
    }
    void save_as(){
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(f);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                try {              
                    saveFile(file.getAbsolutePath());
                } catch (IOException ex) {}               
            }
    }
    class myLis_tool_Save implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Save as")){
                save_as();               
            }
            if (e.getActionCommand().equals("Save")){
                if(file == null)
                    save_as();
                else
                    try {
                        saveFile(file.getAbsolutePath());
                } catch (IOException ex) {}                
            }
            
        }
    }
    class myLis_search implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            search();
        }
        
    }
    static String Read_openFile(File f) throws FileNotFoundException {
        Scanner read = new Scanner(f);
        String result = "";
        while (read.hasNext()) {
            Scanner line = new Scanner(read.nextLine());
            String s = line.nextLine();
            result += s + '\n';
        }
        return result;
    }

    void saveFile(String s) throws IOException {
        FileWriter fw = new FileWriter(s);
        fw.write(textA.getText());
        fw.close();
    }
    
    class myLis_textA extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == 3) {
                pp.show(textA, e.getX(), e.getY());
            }
        }
    }
    class myLis_Exit implements ActionListener{

        @Override
        
        public void actionPerformed(ActionEvent e) {
            
            if (file == null){
                int n1 = JOptionPane.showConfirmDialog(f,"Bạn chưa lưu file, Bạn có thật sự muốn thoát?","Exit Message",
            JOptionPane.YES_NO_OPTION);
                if (n1 == 0) System.exit(0);
            }else{
                int n = JOptionPane.showConfirmDialog(f,"Bạn có thật sự muốn thoát","Exit Message",
            JOptionPane.YES_NO_OPTION);
                if (n == 0) System.exit(0);
                
            }
        } 
    }
    void search(){
        String s = textB.getText();
        String sA = textA.getText();
        int lenS = s.length();
        int lenSA = sA.length();
        Highlighter hl = textA.getHighlighter();
        HighlightPainter painter = new DefaultHighlightPainter(Color.YELLOW);
        hl.removeAllHighlights();
        if (!s.equals("")){
            for (int i=0;i<(lenSA-lenS);i++){
                if (sA.substring(i, i+lenS).equals(s))
                    try {
                        hl.addHighlight(i, i+lenS, painter);
                    } catch (BadLocationException ex) {}              
            }
            textB.setText("");
        }
        else{
            hl.removeAllHighlights();
        }         
    } 
    String temp="";
    class popup_Tool implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {         
            switch(e.getActionCommand()){
                case "Delete":
                    textA.replaceSelection("");
                    break;
                case "Cut":
                    temp = textA.getSelectedText();
                    textA.replaceSelection("");
                    break;
                case "Paste":
                    textA.replaceSelection("");
                    textA.insert(temp, textA.getSelectionStart());
                    break;
                case "Copy":
                    temp = textA.getSelectedText();
            }
        }
    }
    class myLis_undoText extends KeyAdapter{
        @Override
        public void keyTyped(KeyEvent e){
            try{
            undoText.add(textA.getText());
            last = undoText.size() -1;
            }catch(Exception e1){};
            
        }
    }
    class myLis_Undo_Button implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            if (!undoText.isEmpty()){
                last--;           
                textA.setText(undoText.get(last));
            }else{                
            }
            }catch(Exception e1){}
            
        }      
    }
    public void run(){
        //myFrame();
        while(true){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {}              
            if (file == null){               
            }
            else{
                try {
                    saveFile(file.getAbsolutePath());
                } catch (IOException ex) {}                
            }
        }
    }
    public static void main(String[] args) {
        My_NOTE x = new My_NOTE();
        x.myFrame();
        x.start();
        
        
    }

}
