package com.pdf.util;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class PDFUtil {
	public static void main(String[] args) {
		String[] xmm = {"北北", "上北", "广北", "广北12", "广北121"};

		JList jl = new JList(xmm);

		jl.setVisibleRowCount(3);

		JScrollPane xm = new JScrollPane(jl);
		selectMutiPdfFileToSingleA3();
		selectPdfFileA4ToA3();
	}
	
	public static void selectPdfFileA4ToA3() {
		String pdfSrc, pdfA3;
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			pdfSrc = chooser.getSelectedFile().getAbsolutePath();
			pdfA3 = pdfSrc.replace(".pdf", "_a3.pdf");
			System.out.println(pdfSrc);
			System.out.println(pdfA3);
			try {
				manipulatePdfA3ToA4(pdfSrc, pdfA3);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("没有找到文件！");

			System.out.println("程序非正常结束...");
			return;
		}

	}

	public static String selectMergePdf() {
		int result = 0;
		String path = null;
		JFileChooser fileChooser = new JFileChooser();
		FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
		System.out.println(fsv.getHomeDirectory());                //得到桌面路径
		fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
		fileChooser.setDialogTitle("请选择要上传的文件...");
		fileChooser.setApproveButtonText("确定");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		result = fileChooser.showOpenDialog(null);
		if (JFileChooser.APPROVE_OPTION == result) {
			path = fileChooser.getSelectedFile().getPath();
			System.out.println("path: " + path);
			File pdfFolder = new File(path);
			File[] listfile = pdfFolder.listFiles();
			List<File> pdfs = Arrays.asList(listfile);
			Collections.sort(pdfs, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});

			String[] pdfPaths = new String[pdfs.size()];
			for (int i = 0; i < pdfs.size(); i++) {
				pdfPaths[i] = pdfs.get(i).getAbsolutePath();
				System.out.println(pdfPaths[i]);
			}
			String A4 = "A4.pdf";
			mergePdfFiles(pdfPaths, path + "/" + A4);

		}
		return path ;
	}
	public static void selectMutiPdfFileToSingleA3(){
			String path=selectMergePdf();
			try {
				manipulatePdfA3ToA4(path+"/"+"A4.pdf",path+"/"+"A3.pdf");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}

	/**
	 *  A4-pdf文件生成A3-pdf文件
	 * @param a3PdfPath
	 * @param a4PdfPath
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void manipulatePdfA3ToA4(String a4PdfPath,String a3PdfPath) throws IOException, DocumentException {
		File file = new File(a3PdfPath);
		if(!file.exists()) {
			file.createNewFile();
		}
		PdfReader reader1 = null;
		reader1 = new PdfReader(a4PdfPath);
		Rectangle pagesize = new Rectangle(
				PageSize.A4.getWidth()*2,
				PageSize.A4.getHeight() );
		Document document = new Document(pagesize);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(a3PdfPath));
		document.open();
		PdfContentByte canvas = writer.getDirectContent();
		float a4_width = PageSize.A4.getWidth();
		float a4_height = PageSize.A4.getHeight();

		int n = reader1.getNumberOfPages();
		int p = 1;
		PdfImportedPage page;
		for(;p<=n;) {
			addPDFPage(canvas, reader1, p, 0);
			addPDFPage(canvas, reader1, p+1, a4_width);
			AffineTransform at = AffineTransform.getRotateInstance(-Math.PI);
			at.concatenate(AffineTransform.getTranslateInstance(0, -a4_height));
			canvas.saveState();
			canvas.concatCTM(at);
			canvas.restoreState();
			document.newPage();
			p=p+2;
		}
		document.close();
		reader1.close();
	}
	public static void addPDFPage(PdfContentByte canvas,
								  PdfReader reader, int p, float x) {
		if (p > reader.getNumberOfPages())
		{return;}
		PdfImportedPage page = canvas.getPdfWriter().getImportedPage(reader, p);
		canvas.addTemplate(page, x, 0);
	}

	public static boolean mergePdfFiles(String[] files, String newfile) {
		boolean retValue = false;
		Document document = null;
		try {
			PdfReader pdfReader =new PdfReader(files[0]);
			Rectangle pageSize =pdfReader.getPageSize(1);
			document = new Document(pageSize);
			PdfCopy copy = new PdfCopy(document, new FileOutputStream(newfile));
			document.open();
			for (int i = 0; i < files.length; i++) {
				PdfReader reader = new PdfReader(files[i]);
				int n = reader.getNumberOfPages();
				for (int j = 1; j <= n; j++) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
				reader.close();
			}
			document.close();
			retValue = true;
			pdfReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			document.close();
		}
		return retValue;
	}

	public static String splitPdf(String pdfFile,Integer from,Integer end){
		Document document = null;
		PdfCopy copy = null;
		try {
			PdfReader reader = new PdfReader(pdfFile);
			int n = reader.getNumberOfPages();
			if(end==0){
				end = n;
			}
			List<String> savepaths = new ArrayList<String>();
			int a = pdfFile.lastIndexOf(".pdf");
			String staticpath = pdfFile.substring(0, a);
			String savepath = staticpath+ "_from_"+from+"_to_"+end+"_.pdf";
			savepaths.add(savepath);
			document = new Document(reader.getPageSize(1));
			copy = new PdfCopy(document, new FileOutputStream(savepaths.get(0)));
			document.open();
			for(int j=from; j<=end; j++) {
				document.newPage();
				PdfImportedPage page = copy.getImportedPage(reader, j);
				copy.addPage(page);
			}
			document.close();
			return new File(savepath).getName();
		} catch (IOException e) {
 			return null;
		} catch(DocumentException e) {
 			return null;
		}
	}
	public static void selectSplitPdf() {
		String pdfSrc, pdfA3;
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			pdfSrc = chooser.getSelectedFile().getAbsolutePath();
			pdfA3 = pdfSrc.replace(".pdf", "_a3.pdf");
			partitionPdfFile(pdfSrc,0);

		} else {
			System.out.println("没有找到文件！");

			System.out.println("程序非正常结束...");
			return;
		}
	}
	/**
	 * 单个Pdf文件分割成N个文件
	 *
	 * @param filepath
	 * @param N
	 */
	public static void partitionPdfFile(String filepath, int N) {
		Document document = null;
		PdfCopy copy = null;

		try {
			PdfReader reader = new PdfReader(filepath);
			int n = reader.getNumberOfPages();
			N=n;
			if (n < N) {
				System.out.println("The document does not have " + N
						+ " pages to partition !");
				return;
			}
			int size = n / N;
			String staticpath = filepath.substring(0,
					filepath.lastIndexOf("\\") + 1);
			String savepath = null;
			List<String> savepaths = new ArrayList<String>();
			for (int i = 1; i <= N; i++) {
				if (i < 10) {
					savepath = filepath.substring(
							filepath.lastIndexOf("\\") + 1,
							filepath.length() - 4);
					savepath = staticpath + savepath + "0" + i + ".pdf";
					savepaths.add(savepath);
				} else {
					savepath = filepath.substring(
							filepath.lastIndexOf("\\") + 1,
							filepath.length() - 4);
					savepath = staticpath + savepath + "_"+i + ".pdf";
					savepaths.add(savepath);
				}
			}

			for (int i = 0; i < N - 1; i++) {
				document = new Document(reader.getPageSize(1));
				copy = new PdfCopy(document, new FileOutputStream(
						savepaths.get(i)));
				document.open();
				for (int j = size * i + 1; j <= size * (i + 1); j++) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
				document.close();
			}

			document = new Document(reader.getPageSize(1));
			copy = new PdfCopy(document, new FileOutputStream(
					savepaths.get(N - 1)));
			document.open();
			for (int j = size * (N - 1) + 1; j <= n; j++) {
				document.newPage();
				PdfImportedPage page = copy.getImportedPage(reader, j);
				copy.addPage(page);
			}
			document.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
