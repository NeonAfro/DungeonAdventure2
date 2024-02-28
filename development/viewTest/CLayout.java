package viewTest;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class CLayout {
	
	JFrame frame = new JFrame("CardLayout Demo");
	JPanel panelCont = new JPanel();
	JPanel panelFirst = new JPanel();
	JPanel panelSecond = new JPanel();
	JButton buttonOne = new JButton("switch to second panel/workspace");
	JButton buttonSecond = new JButton("switch to first panel/workspace");
	CardLayout cl = new CardLayout();
	
	public CLayout() {
		panelCont.setLayout(cl);
		
		panelFirst.add(buttonOne);
		panelSecond.add(buttonSecond);
		
		panelFirst.setBackground(Color.blue);
		panelSecond.setBackground(Color.green);
		
		panelCont.add(panelFirst, "1");
		panelCont.add(panelSecond, "2");
		cl.show(panelCont, "1"); // shows "1".
		
		buttonOne.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cl.show(panelCont, "2");
				}
			});
		buttonSecond.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cl.show(panelCont, "1");
				}
			});
		
		frame.add(panelCont);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new CLayout();
			}
		});
	}
}
