package org.xsnake.cloud.xflow.core.activity;

import java.io.IOException;
import java.util.List;

import org.dom4j.Element;
import org.xsnake.cloud.xflow.core.AutomaticActivity;
import org.xsnake.cloud.xflow.core.Transition;
import org.xsnake.cloud.xflow.core.context.ApplicationContext;
import org.xsnake.cloud.xflow.core.context.ProcessInstanceContext;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;

/**
 使用jenkins的远程执行ssh的第三方组件
<dependency>
	<groupId>org.jenkins-ci</groupId>
	<artifactId>trilead-ssh2</artifactId>
	<version>build-217-jenkins-11</version>
</dependency>
*/
public class RemoteSSHActivity extends AutomaticActivity {

	public RemoteSSHActivity(ApplicationContext context , Element activityElement) {
		super(context,activityElement);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public List<Transition> doWork(ProcessInstanceContext context){
		return null;
	}

	@Override
	public void definitionValidate(ApplicationContext context) {
		
	}

	public static void main(String[] args) {
		Connection conn = null;
		Session session = null;
        try {
        	conn = new Connection("192.168.6.70");
        	conn.connect();
            conn.authenticateWithPassword("root", "Streamax@2017");
            session = conn.openSession();
            session.execCommand("mkdir /home/soft/123");
            
//            InputStream inp = session.getStdout();
//            InputStreamReader reader = new InputStreamReader(inp);
//            BufferedReader br = new BufferedReader(reader);
//            String line = br.readLine();
//            System.out.println(line);
//            
//            inp.close();
//            reader.close();
            
            SCPClient scpClient = conn.createSCPClient();
            System.out.println("开始拷贝文件...");
            scpClient.put("D:\\som.sql", "/home/soft/123/");
            System.out.println("拷贝文件完成！");
            
//            SFTPv3Client client = new SFTPv3Client(conn);
//            Vector<SFTPv3DirectoryEntry> files = client.ls("/apps/123");
//            for(SFTPv3DirectoryEntry item : files){
//                System.out.println("文件名称: " + item.filename);
//            }
            
            /*
            SFTPv3FileHandle handle = client.createFile("/apps/123/Nero.exe");
            File localFile = new File("e:\\tmp\\sendFiles\\Nero.exe");
            FileInputStream fis = new FileInputStream(localFile);
            byte[] arr = new byte[(int) localFile.length()];
            fis.read(arr);
            fis.close();
            client.write(handle, 0, arr, 0, arr.length);
            client.closeFile(handle);
            */
           
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	session.close();
        	conn.close();
        }
	}
	
}
