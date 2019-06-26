package com.example.yanglin.ftpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {


    FTPClient ftpClient;
    private static final String TAG = "MainActivity";
    private final String IP_SERVER = "140.206.187.251";
    private final int PORT = 50015;
    private final String USER_NAME = "hunmcctest";
    private final String PASSWORD = "Hun123!@#";

/*    private final String IP_SERVER = "121.28.85.142";
    private final int PORT = 21;
    private final String USER_NAME = "SJZwangyou";
    private final String PASSWORD = "SJZwangyou123";*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ftpTest();
            }
        }).start();


    }

    private void ftpTest() {
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(IP_SERVER,PORT);
            Log.i(TAG, "onCreate: 开始连接服务器");
            int replyCode = ftpClient.getReplyCode();
            Log.i(TAG, "onCreate: 返回码为："+replyCode);

            if(!FTPReply.isPositiveCompletion(replyCode)) {
                ftpClient.disconnect();
                Log.e(TAG, "onCreate:服务器拒绝连接 " );
                return;
            }

            if (ftpClient.login(USER_NAME,PASSWORD)){
                Log.i(TAG, "onCreate:登录成功" );
            }else {
                Log.e(TAG, "onCreate:登录失败" );
                return;
            }
            ftpClient.enterLocalPassiveMode();
            FTPFile[] ftpFiles = ftpClient.listFiles();
            Log.i(TAG, "ftpTest: FTP文件信息："+ftpFiles == null ? "ftpFiles为空": "服务器文件数量为："+ftpFiles.length);
            for (FTPFile ftpFile : ftpFiles) {

                if (ftpFile.getName().contains("upload")){

                    if (ftpFile.isDirectory()){
                        ftpClient.changeWorkingDirectory("/"+ftpFile.getName());
                        FTPFile[] ftpFiles1 = ftpClient.listFiles();
                        for (FTPFile file : ftpFiles1) {
                            Log.i(TAG, "ftpTest: 子文件名："+file.getName());
                        }

                        ftpClient.changeToParentDirectory();//切换到父路径

                        FTPFile[] ftpFiles2 = ftpClient.listFiles();

                        for (FTPFile file : ftpFiles2) {
                            Log.i(TAG, "ftpTest: 父文件夹为："+file.getName());
                        }
                    }

                    Log.i(TAG, "onCreate: FTP服务器文件："+new String(ftpFile.getName().getBytes("iso-8859-1"), "UTF-8"));

                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
