package com.example.abaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SSH extends AsyncTask<String, Void, String> {
    private String hostname;
    private String username;
    private String password;

    private JSch jsch;
    private Session session;
    private Channel channel;
    private ChannelExec channelExec;
    private FileOutputStream fileOutputStream;
    private ChannelSftp channelSftp = null;
    private InputStream inputStream = null;
    private StringBuilder stringBuilder;

    public SSH(String hostname, String username, String password){
        this.hostname = hostname;
        this.username = username;
        this.password = password;
    }
    @Override
    protected String doInBackground(String... strings) {
        /*
        * strings[0] = mode
        * strings[1](SSH) = command
        * strings[1](SFTP) = Server File Root
        * strings[2](SFTP) = device folder route & File name
        * */
        jsch = new JSch();
        try {
            session = jsch.getSession(username, hostname, 22);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking","no");
            session.setConfig(config);
            session.connect();

            switch (strings[0]){
                case "SSH":
                    channel = session.openChannel("exec");
                    channelExec = (ChannelExec)channel;
                    channelExec.setPty(true);
                    channelExec.setCommand(strings[1]);//command

                    stringBuilder = new StringBuilder();
                    inputStream = channel.getInputStream();
                    ((ChannelExec)channel).getErrStream();

                    channel.connect();

                    if (channel.isClosed()) {
                        System.out.println("결과");
                        System.out.println(stringBuilder.toString());
                        channel.disconnect();
                    }
                    /*byte[] tmp = new byte[1024];
                    while (true) {
                        while (inputStream.available() > 0) {
                            int i = inputStream.read(tmp, 0, 1024);
                            stringBuilder.append(new String(tmp, 0, i));
                            if (i < 0) break;
                        }
                        if (channel.isClosed()) {
                            System.out.println("결과");
                            System.out.println(stringBuilder.toString());
                            channel.disconnect();
                        }
                    }*/
                    break;
                case "SFTP":
                    channel = session.openChannel("sftp");
                    channel.connect();
                    channelSftp = (ChannelSftp)channel;
                    channelSftp.cd(strings[1]);//"/var/www/ABA/g5/data/file/free/"
                    inputStream = channelSftp.get(strings[2]);

                    fileOutputStream = new FileOutputStream(new File(strings[1]));
                    int i, downsize = 0;
                    while((i = inputStream.read()) != -1){
                        fileOutputStream.write(i);
                        //downsize += i;
                        //System.out.println("DownLoaded : " + downsize);
                    }
                    break;
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("finish");

        return null;
    }
}
