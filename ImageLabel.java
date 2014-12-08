import javax.swing.*;
import java.awt.*;
class ImageLabel extends JLabel{
	int x,y;
	public ImageLabel(String data){
		super(data);
	}
	public ImageLabel(String data,Icon img,int pos){
		super(data,img,pos);
	}
	public ImageLabel(Icon img){
		super(img);
	}
	public void setIndex(int x,int y){
		this.x=x;
		this.y=y;
	}
	public int getIndexX(){
		return x;
	}
	public int getIndexY(){
		return y;
	}
}

/*	Component component[] =this.getComponents();
		for(int i=0;i<component.length;i++){
			if(component[i] instanceof JTextField)
				continue;
			else{
				component[i].addKeyListener(new KeyAdapter(){
					public void keyPressed(KeyEvent e){
						switch(e.getKeyCode()){
							case 10:
							case 17:
							case 524:
							case 18:
							case 525:
							case 16:
							case 27:
							case 192:
							case 32:
							case 112 :
								System.out.println("\n Nothing");
								break;
						}
					}
				});
			}
		}*/