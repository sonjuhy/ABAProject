package com.example.abaproject;

import android.content.Context;
import android.os.AsyncTask;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SSH extends AsyncTask<String, Void, String> {
    private String hostname;
    private String username;
    private String password;

    private ArrayList<Ad_Information> adInformations;

    private JSch jsch;
    private Session session;
    private Channel channel;
    private ChannelExec channelExec;
    private FileOutputStream fileOutputStream;
    private ChannelSftp channelSftp = null;
    private InputStream inputStream = null;
    private FileInputStream OutputStream = null;
    private StringBuilder stringBuilder;
    private Context context;

    public SSH(String hostname, String username, String password, ArrayList<Ad_Information> List, Context context){
        this.hostname = hostname;
        this.username = username;
        this.password = password;
        this.adInformations = List;
        this.context = context;
    }
    @Override
    protected String doInBackground(String... strings) {
        /*
        * strings[0] = mode
        * strings[1](SSH) = command
        * strings[1](SFTP) = Server File Root
        * strings[2](SFTP) = device folder route & File name
        * SFTP_DownLoad & SFTP_UpLoad is same value
        * FileList = List of file name(download or put to LCD device)
        * */
        jsch = new JSch();
        try {
            session = jsch.getSession(username, hostname, 22);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();
            for(int count = 0; count < adInformations.size(); count++) {
                switch (strings[0]) {
                    case "SSH":
                        channel = session.openChannel("exec");
                        channelExec = (ChannelExec) channel;
                        channelExec.setPty(true);
                        if(count == 0) {
                            channelExec.setCommand("export DISPLAY=:0 && " + strings[1]+ " " + adInformations.get(count).getFileName());//command
                        }
                        else{
                            channelExec.setCommand(strings[1]+ " " + adInformations.get(count).getFileName());//command
                        }

                        stringBuilder = new StringBuilder();
                        inputStream = channel.getInputStream();
                        ((ChannelExec) channel).getErrStream();

                        channel.connect();

                        byte[] tmp = new byte[1024];
                        while(true){
                            while (inputStream.available() > 0) {
                                int i = inputStream.read(tmp, 0, 1024);
                                if(i < 0){
                                    break;
                                }
                                System.out.println(new String(tmp, 0, i));
                            }
                            if(channel.isClosed()){
                                if(inputStream.available() > 0){
                                    continue;
                                }
                                System.out.println("exit-status : "+((ChannelExec) channel).getExitStatus());
                                break;
                            }
                            try{
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        inputStream.close();
                        break;
                    case "SFTP_DownLoad":
                        System.out.println(adInformations.get(count).getFileName()+"~~~~~~~~~~~~~~~~~");
                        channel = session.openChannel("sftp");
                        channel.connect();
                        channelSftp = (ChannelSftp) channel;
                        channelSftp.cd(strings[1]);//"/var/www/ABA/g5/data/file/free/"
                        inputStream = channelSftp.get(adInformations.get(count).getFileName());//filename from server

                        File file = new File(context.getFilesDir(),adInformations.get(count).getFileName());
                        if(!file.exists())
                        {
                            file.createNewFile();
                        }
                        fileOutputStream = new FileOutputStream(file);//ABAProject with filename
                        int i;
                        while ((i = inputStream.read()) != -1) {
                            fileOutputStream.write(i);
                           // System.out.println("DownLoaded : " + downsize);
                        }

                        break;
                    case "SFTP_UpLoad" :
                        channel = session.openChannel("sftp");
                        channel.connect();
                        channelSftp = (ChannelSftp) channel;

                        channelSftp.put(strings[2], strings[1], new SftpProgressMonitor() {
                            //strings[2] = device route and file name (ex : storage/0/ABAProject/video.mp4)
                            //strings[1] = server route and file name (ex : /var/www/ABA/g5/Allow_AD/video.mp4)
                            private long max = 0;
                            private long count = 0;
                            private long percent = 0;
                            @Override
                            public void init(int i, String s, String s1, long l) {
                                this.max = l;
                            }

                            @Override
                            public boolean count(long bytes) {
                                this.count += bytes;
                                long percentNow = this.count*100/max;
                                if(percentNow>this.percent){
                                    this.percent = percentNow;
                                    System.out.println("progress : " + this.percent); // 프로그래스
                                }
                                return true;
                            }

                            @Override
                            public void end() {
                                System.out.println("Upload End");
                            }
                        });
                        break;
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fileOutputStream.close();
                inputStream.close();
                if("SFTP_UpLoad".equals(strings[1])){
                    OutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("finish");

        return null;
    }
}
