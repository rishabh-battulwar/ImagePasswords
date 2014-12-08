import java.awt.*;
import java.util.*;
import java.io.*;
import javax.swing.border.*;
import java.security.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.event.*;
import java.awt.image.*;
public class Application extends JFrame implements ActionListener{
	//components
	private JButton grid,reset,off,restart,select;
	private static int MAX=15;
	private JTabbedPane tabPane;
	private JToolBar toolbar,author,imageToolbar;
	private JPanel pan;
	private ImageLabel image=null,uNameLbl,imgs[][];
	private JTextField uName;
	private JLabel lblMessage;
	private String defaultMessage="Message Will be Displayed Here ";
	private JPasswordField pass=new JPasswordField("");
	private JToggleButton create,unlock,command,admin;
	private JButton btnOk, imageButton[];
	private ImageIcon defaultIcon;
	private ImageIcon imageIcons[];
	private MouseAdapter adap;
	private int check=0;
	//user objects and variables
	private Vector<Password> pswd;
	private Password current;
	private int count,clicks=0;
	private int i;
	private String password="",imgPath="";
	private static int GRID_COUNT=3;
	public Application()	{
		init();

	}
	public Application(int gc)	{
			GRID_COUNT=gc;
			init();
	}
	public void loadData(){
		pswd=new Vector<Password>();
		try{
			FileInputStream fis = new FileInputStream("database.dta");
			ObjectInputStream ois = new ObjectInputStream(fis);
			pswd=(Vector<Password>)ois.readObject();
			//System.out.println(pswd.size());
			ois.close();
			dispose();
		}catch(Exception ex){
			File file=new File("database.dta");
		}
	}
	public void init(){
		loadData();
		imageButton=new JButton[MAX];
		imageIcons=new ImageIcon[MAX];
 		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
 		this.setUndecorated(true);
 		this.setResizable(false);
		imgs=new ImageLabel[GRID_COUNT][GRID_COUNT];
		setLayout(new BorderLayout());
		add(author=new JToolBar(),BorderLayout.NORTH);
		add(tabPane=new JTabbedPane(),BorderLayout.CENTER);
		add(imageToolbar =new JToolBar(),BorderLayout.EAST);
		add(toolbar=new JToolBar(),BorderLayout.SOUTH);
		author.setLayout(new GridLayout(1,3));
		imageToolbar.setOrientation(SwingConstants.VERTICAL);

		pan=new JPanel();
		defaultIcon = new ImageIcon("images/welcome.png");
		image=new ImageLabel(defaultIcon);
		pan.add(image);
		tabPane.addTab("Home",pan);
		for(int i=0;i<15;i++){
			imageIcons[i] =new ImageIcon("data/small/"+(i)+".jpg");
			//System.out.println(i);
			imageToolbar.add(imageButton[i] =new JButton("",imageIcons[i]));
			imageButton[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					imgPath=((JButton)ae.getSource()).getIcon().toString();
					imgPath=imgPath.replaceAll("small","large");
					//System.out.println(img);
					image.setIcon(new ImageIcon(imgPath));
					setSelections(false,false,false,false,false,true,false);
				}
			});

		}
		imageToolbar.setFloatable(false);

		toolbar.add(create=new JToggleButton("",new ImageIcon("images/create.png")));
		toolbar.add(unlock =new JToggleButton("",new ImageIcon("images/unlock.png")));

		ButtonGroup bg=new ButtonGroup();
		bg.add(create);
		bg.add(unlock);

		toolbar.addSeparator();

		toolbar.add(uNameLbl=new ImageLabel("User Name :",new ImageIcon("images/users.png"),0));
		toolbar.add(uName=new JTextField());

		toolbar.addSeparator();
		select=new JButton("",new ImageIcon("images/select.png"));
		toolbar.add(grid=new JButton("",new ImageIcon("images/grid.png")));

		toolbar.addSeparator();
		toolbar.add(command=new JToggleButton("",new ImageIcon("images/password.png")));
		toolbar.add(reset =new JButton("",new ImageIcon("images/reset.png")));
		toolbar.add(off=new JButton("",new ImageIcon("images/shutdown.png")));
		toolbar.add(restart=new JButton("",new ImageIcon("images/restart.png")));
		toolbar.setFloatable(false);
		pass.setEchoChar('?');
		author.add(lblMessage=new JLabel(" Message Will be displayed here"));
		author.add(btnOk=new JButton("",new ImageIcon("images/key.png")));
		author.add(pass);
		btnOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				lblMessage.setText(defaultMessage);
			}
		});
		pass.setEnabled(false);
		author.add(admin=new JToggleButton("",new ImageIcon("images/admin.png")));
		pass.setBounds(10,10,180,30);
		author.setFloatable(false);

        pack();
        setVisible(true);
        setFocusTraversalPolicyProvider(true);
        transferFocusDownCycle();
		setSelections(false,true,false,false,false,false,false);
		addWindowFocusListener(new WindowAdapter(){
			public void windowLostFocus(WindowEvent e){
				setFocusable(true);
			}
			public void windowGainedFocus(WindowEvent e){
				setFocusable(true);
			}
		});
		addEventHandlers();
		exit();
		lblMessage.setFont(new Font("Times New Roman",Font.BOLD,15));
	}
	public void addEventHandlers(){
		select.addActionListener(this);
		grid.addActionListener(this);
		create.addActionListener(this);
		unlock.addActionListener(this);
		command.addActionListener(this);
		reset.addActionListener(this);
		off.addActionListener(this);
		restart.addActionListener(this);
		admin.addActionListener(this);
		uName.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				//System.out.println(" KeyCode : - "+e.getKeyCode() +"\n Key : - "+e.getKeyText(e.getKeyCode()));
				if(e.getKeyCode()==10)
					enterKey();
				else if(e.getKeyCode() == 27 )
					setSelections(true,true,false,false,false,false,false);
			}
		});
		adap=new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				ImageLabel l=(ImageLabel)e.getSource();
				Color value =((EtchedBorder)l.getBorder()).getShadowColor();
				if(value == Color.RED){
					l.setBorder(new EtchedBorder(Color.GREEN,Color.GREEN));
					password+=l.getIndexX()+"|"+l.getIndexY()+";";
				}
				else if(value == Color.GREEN){
					password+=l.getIndexX()+"|"+l.getIndexY()+";";
					l.setBorder(new EtchedBorder(Color.BLUE,Color.BLUE));
				}
				else if(value == Color.BLUE){
					lblMessage.setForeground(Color.RED);
					lblMessage.setText("Please select another portion ! ");
				}
				else{
					password+=l.getIndexX()+"|"+l.getIndexY()+";";
					l.setBorder(new EtchedBorder(Color.RED,Color.RED));
				}
				clicks++;
			}
		};
	}
	private static String master="123456789";
	public void enterKey(){
		if(uName.getText().trim().equals("")){
			lblMessage.setForeground(Color.RED);
			lblMessage.setText("Please Provide the Username First..!");
		}else{
			if(unlock.isSelected()){
				for(int i=0;i<pswd.size();i++){
					current=(Password)pswd.get(i);
					//System.out.println("Password :"+current.getPassword());
				if(uName.getText().equals(current.getUsername())){
					JPanel tem=(JPanel)tabPane.getComponentAt(0);
					ImageLabel temp=(ImageLabel)tem.getComponent(0);
					temp.setIcon(current.getMap());
					break;
				}else{
					current=null;
				}
			}
			if(current!=null){
				setSelections(false,false,false,false,false,true,false);
			}else{
				lblMessage.setForeground(Color.RED);
				lblMessage.setText("Please check username !");
				uName.setText("");
			}
		}else if(create.isSelected()){
			int check=0;
			for(int i=0;i<pswd.size();i++){
				Password p=(Password) pswd.get(i);
				if(uName.getText().equals(p.getUsername())){
					check=1;
					break;
				}
			}
			if(check == 0){
				setSelections(false,false,false,false,true,false,false);
			}else{
				lblMessage.setForeground(Color.RED);
				lblMessage.setText("Username already exist..!");
			}
		}
	}}
	public void exit(){
				try{
					FileOutputStream fos = new FileOutputStream("database.dta",false);
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(pswd);
					oos.close();
				}catch(Exception ex){
					lblMessage.setText("App is geeting closed !");
				}
	}
	public void formGrid(){
		JPanel pan=(JPanel)tabPane.getComponentAt(0);
		ImageLabel l=(ImageLabel)pan.getComponent(0);
		l.setIcon(null);
		JPanel p=new JPanel();
		p.setLayout(new GridLayout(GRID_COUNT,GRID_COUNT));
		drawGrid(l,p);
		setSelections(false,false,false,false,false,false,true);
	}
	public void setImage(ImageLabel l){
		l.setIcon(new ImageIcon(imgPath));
	}
	public void drawGrid(ImageLabel l, JPanel p){
		try{
			Robot screen=new Robot();
			setImage(l);
			BufferedImage bImg=screen.createScreenCapture(new Rectangle(image.getX(),(tabPane.getY()+6 * image.getY()),l.getWidth(),l.getHeight()));
			int x=0,y=0,w=bImg.getWidth()/GRID_COUNT,h=bImg.getHeight()/GRID_COUNT;
			for(int i=0;i<GRID_COUNT;i++){
				for(int j=0;j<GRID_COUNT;j++){
					imgs[i][j]=new ImageLabel(new ImageIcon(bImg.getSubimage(x,y,w,h)));
					imgs[i][j].setIndex(i,j);
					imgs[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
					p.add(imgs[i][j]);
					x=x+w;
				}
				x=0;
				y=y+h;
			}
			tabPane.add("Select Password",new JPanel());
			JPanel temp=(JPanel)tabPane.getComponentAt(1);
			temp.add(p);
			tabPane.setSelectedIndex(1);
		}catch(Exception s){
			s.printStackTrace();
		}
	}
	public void imageClear(){
		JPanel panel=(JPanel)tabPane.getComponentAt(0);
		ImageLabel temp=(ImageLabel)panel.getComponent(0);
		temp.setIcon(defaultIcon);
		uName.setText("");
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()== select){
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File("data/"));
			chooser.setFileFilter(new FileNameExtensionFilter("Image File(*.jpg,*.png,*.gif)", "jpg", "png","gif"));
			int returnVal = chooser.showOpenDialog(Application.this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
			JPanel tem=(JPanel)tabPane.getComponentAt(0);
			ImageLabel temp=(ImageLabel)tem.getComponent(0);
			imgPath=chooser.getSelectedFile().getPath();
			if(imgPath!=null)
				setSelections(false,false,false,false,false,true,false);
			temp.setIcon(new ImageIcon(""+imgPath));
			}
		}else if(e.getSource() == grid ){
				formGrid();
		}else if(e.getSource() == create){
			loadData();
			count=0;
			clicks=0;
			password="";
			if(check==1)
				setSelections(false,false,true,true,false,false,false);
			else
				setSelections(false,true,false,false,false,false,false);
			if(tabPane.getTabCount()==2){
				tabPane.setSelectedIndex(0);
				tabPane.removeTabAt(1);
			}
		}else if(e.getSource()==unlock){
			loadData();
			count=0;
			clicks=0;
			if(check==1)
				setSelections(false,false,true,true,false,false,false);
			else
				setSelections(false,false,true,true,false,false,false);
		}else if(e.getSource()== reset){
			imageClear();
			if(check==1)
				setSelections(true,true,false,false,false,false,false);
			else
				setSelections(false,true,false,false,false,false,false);
			tabPane.setSelectedIndex(0);
			if(tabPane.getTabCount() > 1)
			tabPane.removeTabAt(1);
		}else if(e.getSource() == off){
			exit();
			Runtime run=Runtime.getRuntime();
			/*try{
				run.exec("shutdown -f -s ");
			}catch(IOException ie){
			}*/
			System.exit(0);
		}else if(e.getSource() == admin){
			if(admin.isSelected()){
				pass.setEnabled(true);
			}else{
				String str=pass.getText();
				if(str.equals(master)){
					create.setEnabled(true);
					check=1;
					lblMessage.setForeground(Color.BLUE);
					lblMessage.setText("Welcom Administrator");
					admin.setEnabled(false);
					pass.setText("");
					pass.setEnabled(false);
				}else{
					lblMessage.setForeground(Color.RED);
					lblMessage.setText("Please enter a correct password !");
					pass.setText("");
					pass.setEnabled(false);
				}
			}
		}else if(e.getSource()==restart){
			exit();
			/*Runtime run=Runtime.getRuntime();
			try{
				run.exec("shutdown -f -r ");
			}catch(IOException ie){
			}*/
			System.exit(0);
		}else if(e.getSource()==command){
			if(command.isSelected() == true ){
					for(int i=0;i<GRID_COUNT;i++)
						for(int j=0;j<GRID_COUNT;j++){
							imgs[i][j].addMouseListener(adap)	;
						}
			}else{
				for(int i=0;i<GRID_COUNT;i++)
					for(int j=0;j<GRID_COUNT;j++){
						imgs[i][j].removeMouseListener(adap);
					}
				clearGrid();
				if(clicks > 3){
					if(unlock.isSelected()){
						if(count < 2 ){
							byte[] pass=null;
							MessageDigest message;
							try{
								message=MessageDigest.getInstance("MD5");
								pass=message.digest(password.getBytes());
							}catch(NoSuchAlgorithmException ne){
							}
							if(current.compareTo(pass)){
								exit();
								lblMessage.setForeground(Color.BLUE);
								lblMessage.setText("Welcom "+current.getUsername()+" !");
								setVisible(false);
								Runtime run=Runtime.getRuntime();
								try{
									run.exec("notepad.exe");
								}catch(IOException ie){
								}
								System.exit(0);
							}else{
								lblMessage.setForeground(Color.RED);
								lblMessage.setText("Please Try Again ! : Login UnSuccessful..!");
							}
								password="";
						}else{
							lblMessage.setForeground(Color.RED);
							lblMessage.setText("Unauthorized access !");
							if(check==1)
								setSelections(true,true,false,false,false,false,false);
							else
								setSelections(false,true,false,false,false,false,false);
							tabPane.setSelectedIndex(0);
							tabPane.removeTabAt(1);
							imageClear();
							uName.setText("");
							password="";
						}
						count++;
					}else{
						Password p=new Password(uName.getText(),new ImageIcon(imgPath),password);
						pswd.add(p);
						password="";
						//System.out.println(pswd.size());
						lblMessage.setForeground(Color.BLUE);
						lblMessage.setText("New Account Created..!");
						setFocusable(true);
						uName.setText("");
							if(check==1)
								setSelections(true,true,false,false,false,false,false);
							else
								setSelections(false,true,false,false,false,false,false);
						tabPane.setSelectedIndex(0);
						if(tabPane.getTabCount() > 1)
							tabPane.removeTabAt(1);
						exit();
						imageClear();
					}
				}
			}
		}
	}
	public void clearGrid(){
		for(int i=0;i<GRID_COUNT;i++)
			for(int j=0;j<GRID_COUNT;j++)
				imgs[i][j].setBorder(new EtchedBorder(EtchedBorder.RAISED));
	}
	public void setSelections(boolean c,boolean u,boolean l,boolean t,boolean s,boolean g,boolean ss){
			GraphicsEnvironment.getLocalGraphicsEnvironment().
			getDefaultScreenDevice().setFullScreenWindow(Application.this);
			create.setEnabled(c);
			unlock.setEnabled(u);
			select.setEnabled(s);
			Component components[]=imageToolbar.getComponents();
			for(int i=0;i<components.length;i++)
				components[i].setEnabled(s);
			grid.setEnabled(g);
			command.setEnabled(ss);
			uNameLbl.setEnabled(l);
			uName.setEnabled(t);
	}
	public static void main(String args[]){
            try{
            	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName() );
            	Application app = new Application(10);
			}catch(ClassNotFoundException ce){
			}catch(InstantiationException ie){
			}catch(IllegalAccessException ie){
			}catch(UnsupportedLookAndFeelException ue){
			}
	}
}
