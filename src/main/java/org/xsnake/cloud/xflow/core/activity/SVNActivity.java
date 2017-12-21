package org.xsnake.cloud.xflow.core.activity;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNActivity {

	public static void main(String[] args) throws SVNException {
	     
//		DefaultSVNOptions myOptions = new DefaultSVNOptions();
//	     
//	    SVNClientManager clientManager = SVNClientManager.newInstance(myOptions, "name", "passw");
//	    
//	    SVNURL url = SVNURL.create("svn", "software", "192.168.6.99", -1, "/OracleERP", true);
//	    
//	    File dstPath = new File("D:\\upload");
//	    
//	    clientManager.getUpdateClient().doCheckout(url, dstPath, SVNRevision.create(new Date()), SVNRevision.create(new Date()), depth, allowUnversionedObstructions);
		
		String url = "svn://192.168.6.99/OracleERP/70%E5%AE%A2%E6%88%B7%E5%8C%96%E5%BC%80%E5%8F%91/%E4%BB%A3%E7%A0%81/configs";
		checkOut(url,"D:\\upload");
		
	}
	
	 public static void checkOut(String url,String desPath){
	        SVNClientManager ourClientManager;
	        //初始化支持svn://协议的库。 必须先执行此操作。         
	        SVNRepositoryFactoryImpl.setup();
	        //相关变量赋值
	        SVNURL repositoryURL = null;
	        try {
	            repositoryURL = SVNURL.parseURIEncoded(url);
	        } catch (SVNException e) {
	            System.out.println("初始化svn地址失败"+url);
	            System.out.println(e);
	            System.exit(1);
	        }        
	        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
	        //实例化客户端管理类
	        ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, "software", "software12.0");
	        //要把版本库的内容check out到的目录
	        File desFile = new File(desPath);
	        //通过客户端管理类获得updateClient类的实例。
	        SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
	        updateClient.setIgnoreExternals(false);
	        try {
	            //返回工作版本号
	            long workingVersion = updateClient.doCheckout(repositoryURL, desFile, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY,true);
	            System.out.println("把版本："+workingVersion+" check out 到目录："+desPath+"中");
	        } catch (SVNException e) {
	            System.out.println("检出代码出错"+e);
	            System.exit(1);
	        }
	    }
	
}
