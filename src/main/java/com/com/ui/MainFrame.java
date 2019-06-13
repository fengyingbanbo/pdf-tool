package com.com.ui;
  
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import   com.pdf.util.PDFUtil;


public class MainFrame extends JFrame {  
  
    public MainFrame(){  
        init();  
    }  
    Object[] value;
    Object[] defaultValue;
    private void init() {
        FlowLayout layout= new FlowLayout();
        this.setLayout(layout);
//        value = new String[]{ "pdf拆分","多个pdf合成单个pdf" , "A4排版转A3" ,"多个A4pdf合成单个A3排版" };
//        defaultValue = new String[]{ "1"  ,"4","5" };
//        final JComboBox  mulit = new JComboBox(value);
//        mulit.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int i = mulit.getSelectedIndex();
//                if(i==0){
//                    PDFUtil.selectSplitPdf();
//                }else if(i==1){
//                    PDFUtil.selectMergePdf();
//                }else if(i==2){
//                    PDFUtil.selectPdfFileA4ToA3();
//                }else if(i==3){
//                    PDFUtil.selectMutiPdfFileToSingleA3();
//                }
//            }
//        });
//        this.add(mulit );


        JButton selectMutiPdfFileToSingleA3Button = new JButton("合并并且转A3排版");
        selectMutiPdfFileToSingleA3Button.setBackground(Color.CYAN);
        this.add(selectMutiPdfFileToSingleA3Button);
        selectMutiPdfFileToSingleA3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PDFUtil.selectMutiPdfFileToSingleA3();

            }
        });

        JButton splitButton = new JButton("拆分");
        splitButton.setBackground(Color.CYAN);

        this.add(splitButton);
        splitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                PDFUtil.selectSplitPdf();
			}
		});


        JButton mermgePdfButton = new JButton("合成");
        mermgePdfButton.setBackground(Color.CYAN);

        this.add(mermgePdfButton);
        mermgePdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PDFUtil.selectMergePdf();

            }
        });


        JButton selectPdfFileA4ToA3Button = new JButton("A4转A3排版");
        selectPdfFileA4ToA3Button.setBackground(Color.CYAN);

        this.add(selectPdfFileA4ToA3Button);
        selectPdfFileA4ToA3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PDFUtil.selectPdfFileA4ToA3();

            }
        });


    }  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
       /* try {  
           // UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");  
        } catch (ClassNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (InstantiationException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (UnsupportedLookAndFeelException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  */
          
          
        Window main = new MainFrame();
        main.setSize(195,154);
        main.setVisible(true);
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        main.setCursor(cursor);
        ((MainFrame) main).setTitle("pdf工具");
        ((MainFrame) main).setResizable(false);
  
    }  
  
}  
