package wordseg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.ansj.splitWord.analysis.*;
import org.ansj.util.FilterModifWord;
import org.ansj.util.MyStaticValue;
import org.ansj.domain.Term;

public class Text2Word {
	 public static final String TAG_START_CONTENT = "<content>";  
	 public static final String TAG_END_CONTENT = "</content>";  

	/**
	 * @param args
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ParseException, IOException {
		// TODO Auto-generated method stub
		String outPath = "./result.txt";
		Options opts = new Options();
		opts.addOption("l", true, "设置辞典路径");
		opts.addOption("s", true, "设置停用词文件路径");
		opts.addOption("i", true, "分词文件路径");
		opts.addOption("o", true, "输出结果文件路径");
		// 解析参数
		CommandLineParser parser = new DefaultParser();
		CommandLine cl = parser.parse(opts, args);

		if (cl.hasOption("o")) {
			outPath = cl.getOptionValue("o");
		}
		System.out.println("输出结果文件:" + outPath);

		if (cl.hasOption("l")) {
			if (!setUserLibrary(cl.getOptionValue("l"))) {
				return;
			}
		} else {
			System.out.println("没有设置辞典");
		}

		if (cl.hasOption("s")) {
			if (!setStopWord(cl.getOptionValue("s"))) {
				return;
			}
		} else {
			System.out.println("没有设置停用词文件");
		}

		if (cl.hasOption("i")) {
			String inputPath = cl.getOptionValue("i");
			File file = new File(inputPath);
			if (file.exists() && file.isFile()) {
				BufferedReader br = new BufferedReader(new FileReader(file)); // 建立一个对象，它把文件内容转成计算机能读懂的语言
				PrintWriter pw = new PrintWriter(outPath); 
				long start = System.currentTimeMillis()  ;  
	            int allCount =0 ;  
	            int termcnt = 0;  
	            Set<String> set = new HashSet<String>(); 
				try {
					String line = null;
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith(TAG_START_CONTENT)) {
							int end = line.indexOf(TAG_END_CONTENT);  
							String content = line.substring(TAG_START_CONTENT.length(), end);  
		                    //System.out.println(content);  
		                    if (content.length() > 0) {  
		                        allCount += content.length() ;  
		                        List<Term> result = ToAnalysis.parse(content);  
								//List<Term> parse = FilterModifWord.modifResult(NlpAnalysis.parse(str));
								//List<Term> parse = FilterModifWord.modifResult(ToAnalysis.parse(str));
		                        for (Term term: result) {  
		                            String item = term.getName().trim();  
		                            if (item.length() > 0) {  
		                                termcnt++;  
		                                pw.print(item.trim() + " ");  
		                                set.add(item);  
		                            }  
		                        }  
		                        pw.println();  
		                    }   
						}						
					}
					long end = System.currentTimeMillis() ;  
		            System.out.println("共" + termcnt + "个term，" + set.size() + "个不同的词，共 "  
		                    +allCount+" 个字符，每秒处理了:"+(allCount*1000.0/(end-start)));  
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					pw.close();
					br.close();
				}
			} else {
				System.out.println("文件:" + inputPath + "不存在！");
				return;
			}
		} else {
			System.out.println("请输入分词文件");
			return;
		}
	}

	private static boolean setUserLibrary(final String path) {
		File file = new File(path);
		if (file.exists()) {
			System.out.println("辞典路径:" + path);
			MyStaticValue.userLibrary = path;
		} else {
			System.out.println("辞典路径:" + path + "不存在！");
			return false;
		}
		return true;
	}

	private static boolean setStopWord(final String path) throws IOException {
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			BufferedReader br = new BufferedReader(new FileReader(file)); // 建立一个对象，它把文件内容转成计算机能读懂的语言
			try {
				String line = br.readLine();
				while (line != null) {
					FilterModifWord.insertStopWord(line);
					line = br.readLine(); // 一次读入一行数据
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} finally {
				br.close();
			}

		} else {
			System.out.println("停用词文件:" + path + "不存在！");
			return false;
		}
		return true;
	}

}

