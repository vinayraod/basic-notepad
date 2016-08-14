package proj;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SpellChecker extends JFrame implements ActionListener,KeyListener,ListSelectionListener {
	/**
	 * 
	 */
	private int pos=0,pos2;
        private String pop;
	private static final long serialVersionUID = 1L;
	private JTextArea textArea = new JTextArea();
	private JMenuBar menuBar = new JMenuBar(); 
	
	private JMenu file = new JMenu();
	private JMenu edit = new JMenu();
	
	private JMenuItem newFile = new JMenuItem();
	private JMenuItem openFile = new JMenuItem();  
	private JMenuItem saveFile = new JMenuItem(); 
	private JMenuItem close = new JMenuItem(); 
	
	private JMenuItem cut = new JMenuItem(new DefaultEditorKit.CutAction());  
	private JMenuItem copy = new JMenuItem(new DefaultEditorKit.CopyAction()); 
	private JMenuItem paste = new JMenuItem(new DefaultEditorKit.PasteAction());
	private JMenuItem bold = new JMenuItem();
	private JMenuItem italic = new JMenuItem();
	
	private JList list = new JList();
	private DefaultListModel model;
	private DefaultListSelectionModel lsm;
	
	private JLabel label = new JLabel();
        Corrector corrector;
        private HashSet<String> dictionary;
        private ArrayList<String> words;
	
	
	public SpellChecker() {
		this.setSize(1000,650); 
		this.setTitle("SpellCheck"); // set the title of the window
		
		setDefaultCloseOperation(EXIT_ON_CLOSE); // set the default close operation (exit when it gets closed)
		
		this.getContentPane().setLayout(null); 
		
		this.textArea.setFont(new Font("Times New Roman", Font.BOLD, 16)); // set a default font for the TextArea
		this.textArea.setEditable(true);
		this.textArea.addKeyListener(this);
                JScrollPane scroll = new JScrollPane (textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.getContentPane().add(scroll).setBounds(0,0,700,600);
		
		this.getContentPane().add(list).setBounds(710,60,263,400);
		
		this.label.setText("SUGGESTIONS [ click to replace ]");
		this.label.setFont(new Font("Times New Roman", Font.BOLD, 14));
		this.getContentPane().add(label).setBounds(725,25,300,20);
		
		//this.getContentPane().setBackground(Color.black);
		
		this.setIconImage(new ImageIcon("icon.png").getImage());
		
		this.setJMenuBar(this.menuBar);
		
		this.menuBar.add(this.file); 
		this.menuBar.add(this.edit);
		
		this.file.setText("File");
		
		this.edit.setText("Edit");
		
		this.newFile.setText("New");
		this.newFile.addActionListener(this);
		this.newFile.setMnemonic(KeyEvent.VK_N);
		this.newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.CTRL_MASK));
		this.file.add(this.newFile);
		
		this.openFile.setText("Open");
		this.openFile.addActionListener(this);
		this.openFile.setMnemonic(KeyEvent.VK_O);
		this.openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,Event.CTRL_MASK));
		this.file.add(this.openFile);
		
		this.saveFile.setText("Save");
		this.saveFile.addActionListener(this);
		this.saveFile.setMnemonic(KeyEvent.VK_S);
		this.saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,Event.CTRL_MASK));
		this.file.add(this.saveFile);
		
		this.close.setText("Close");
		this.close.setMnemonic(KeyEvent.VK_F4);
		this.close.addActionListener(this);
		this.close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,Event.CTRL_MASK));
		this.file.add(this.close);
		
		this.cut.setText("Cut");
		this.cut.addActionListener(this);
		this.cut.setMnemonic(KeyEvent.VK_X);
		this.cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,Event.CTRL_MASK));
		this.edit.add(this.cut);
		
		this.copy.setText("Copy");
		this.copy.addActionListener(this);
		this.copy.setMnemonic(KeyEvent.VK_C);
		this.copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,Event.CTRL_MASK));
		this.edit.add(this.copy);
		
		this.paste.setText("Paste");
		this.paste.addActionListener(this);
		this.paste.setMnemonic(KeyEvent.VK_V);
		this.paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,Event.CTRL_MASK));
		this.edit.add(this.paste);
		
		this.edit.addSeparator();
		
		this.bold.setText("Bold");
		this.bold.addActionListener(this);
		this.edit.add(this.bold);
		
		this.italic.setText("Italic");
		this.italic.addActionListener(this);
		this.edit.add(this.italic);
		
		lsm=(DefaultListSelectionModel) list.getSelectionModel();
                lsm.addListSelectionListener(this);
                
                Dictionary.setDictionary();
                dictionary=Dictionary.getDictionary();
                words=Dictionary.getDictionary2();
	}
	
	public void keyPressed(KeyEvent e) {
        
    }
	public void actionPerformed (ActionEvent e) {
		
		if(e.getSource() == this.newFile)
			this.textArea.setText("");
		
			
		else if (e.getSource() == this.openFile) {
			JFileChooser open = new JFileChooser(); 
			int option = open.showOpenDialog(this); 
			if (option == JFileChooser.APPROVE_OPTION) {
				this.textArea.setText(""); 
				try {
					
					Scanner scan = new Scanner(new FileReader(open.getSelectedFile().getPath()));
					while (scan.hasNext()) 
						this.textArea.append(scan.nextLine() + "\n"); 
				} catch (Exception ex) { 
					System.out.println(ex.getMessage());
				}
			}
		}
		
		else if (e.getSource() == this.saveFile) {
			JFileChooser save = new JFileChooser();
			int option = save.showSaveDialog(this); 
			if (option == JFileChooser.APPROVE_OPTION) {
				try {
					
					BufferedWriter out = new BufferedWriter(new FileWriter(save.getSelectedFile().getPath()));
					out.write(this.textArea.getText()); 
					out.close(); 
				} catch (Exception ex) { 
					
					System.out.println(ex.getMessage());
				}
			}
		}
		
		else if (e.getSource() == this.close)
			this.dispose(); 

		else if (e.getSource() == this.bold)
			this.textArea.setFont(new Font("Times New Roman", Font.BOLD, 16));
		
		else if (e.getSource() == this.italic)
			this.textArea.setFont(new Font("Times New Roman", Font.ITALIC, 16));
			
	}
    
    public static void main(String args[])  {
        SpellChecker app = new SpellChecker();
        try {
            // Set System L&F
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
    } 
    catch (UnsupportedLookAndFeelException e) {
       // handle exception
    }
    catch (ClassNotFoundException e) {
       // handle exception
    }
    catch (InstantiationException e) {
       // handle exception
    }
    catch (IllegalAccessException e) {
       // handle exception
    }

        app.setVisible(true);
}
	@Override
	public void keyReleased(KeyEvent evt) {
		// TODO Auto-generated method stub
            pos2=this.textArea.getCaretPosition();
            if(pos2==0)
                pos=0;
            int keyCode = evt.getKeyCode();
            String input = this.textArea.getText();
            model=new DefaultListModel();
            list.setModel(model);
            Stack<String> st = new Stack<String>();
            HashSet<String> cor=new HashSet<String>();
            corrector=new Corrector();
            if(keyCode==KeyEvent.VK_SPACE||keyCode==KeyEvent.VK_ENTER){
                    pos=this.textArea.getCaretPosition();
                    model.clear();
                    Pattern p = Pattern.compile("[\\w']+");
                    Matcher m = p.matcher(input);
        
                    while (m.find()) {
        		st.push(input.substring(m.start(), m.end()));
        		   	}
                    try {
                        cor.addAll(corrector.listCorrections(st.peek()));
                        String prefix=st.peek().toLowerCase();
                        pop=st.peek();
                        //System.out.println(prefix);
                        for(String str : cor){
                            model.addElement(str);
                        }  
                        list.setModel(model);
			
        	} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	//System.out.println(st.peek());
        	st.clear();
    }
	else{
            pos2=this.textArea.getCaretPosition();
            System.out.println(pos2);
            if((pos2-pos)>=3){
                Pattern p = Pattern.compile("[\\w']+");
                Matcher m = p.matcher(input);
                while (m.find()) {
                     st.push(input.substring(m.start(), m.end()));
        		}
				String prefix=st.peek().toLowerCase();
				pop=st.peek();
				//System.out.println(prefix);
				for(String str : words){
					if(str.startsWith(prefix)){
						System.out.println(str);
						model.addElement(str);
					}
				}
                        
				list.setModel(model);
				st.clear();
            }
	}
   }
	@Override
	 public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

    @Override
   	public void valueChanged(ListSelectionEvent e) {
        if(!e.getValueIsAdjusting()){
            lsm.setSelectionMode(SINGLE_SELECTION);
            String x=this.textArea.getText();
            Pattern p = Pattern.compile("[\\w']+");
            Matcher m = p.matcher(x);
            Stack<String> st = new Stack<String>();
        
        	while (m.find()) {
        		st.push(x.substring(m.start(), m.end()));
        		   	}
        	try {
                x=x.replace(st.peek(),(String)list.getSelectedValue());
				this.textArea.setText(x);
			
        	} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");            
            this.textArea.requestFocusInWindow();
            this.textArea.setCaretPosition(this.textArea.getText().length());
        }
    }
}
