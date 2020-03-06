package com.example.abaproject;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Network extends AsyncTask<String, String, String> implements Serializable {
    protected boolean finish;
    protected boolean Server_Response;
    protected String User_name;
    protected String data;
    private String url;
    private String link;
    private String[] arr_String;//0 = php, 1 = data
    private ArrayList<String> tmp_String;

    private HttpURLConnection httpURLConnection;
    private OutputStream wr;
    private InputStream inputStream;

    Network() {
        finish = false;
        Server_Response = false;
        User_name = null;
        data = null;
        url = null;
        link = null;
        arr_String = null;
        tmp_String = null;
    }

    public void Input_data(String... _param) {
        arr_String = new String[_param.length];
        arr_String = _param;
        if (arr_String.length > 1) {///////위험
            data = arr_String[1];
        }
        for (int i = 0; i < _param.length; i++) {
            System.out.println("Network input : " + arr_String[i]);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.url = "http://sonjuhy.iptime.org/" + arr_String[0] + ".php";
        System.out.println("onPre Success : " + this.url);
    }

    @Override
    protected String doInBackground(String... strings) {
        int count;
        long FileSize = -1;
        InputStream input = null;
        OutputStream output = null;


        try {
            URL url = new URL(this.url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDefaultUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            System.out.println("Network class data : " + data);

            if (data != null) {
                System.out.println("Working here");
                wr = httpURLConnection.getOutputStream();
                wr.write(data.getBytes("UTF-8"));
                wr.flush();
                wr.close();
            }

            int responseStatusCode = httpURLConnection.getResponseCode();

            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                String set = "string";
                switch (set) {/////////strings[0]

                    case "string":
                        inputStream = httpURLConnection.getInputStream();
                        Server_Response = true;
                        break;

                    case "video":
                        FileSize = httpURLConnection.getContentLength();

                        //URL 주소로부터 파일다운로드하기 위한 input stream
                        input = new BufferedInputStream(url.openStream(), 8192);

                    //    path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                     //   outputFile= new File(path, "Alight.avi"); //파일명까지 포함함 경로의 File 객체 생성

                        // SD카드에 저장하기 위한 Output stream
                      //  output = new FileOutputStream(outputFile);


                        byte data[] = new byte[1024];
                        long downloadedSize = 0;
                        while ((count = input.read(data)) != -1) {
                            //사용자가 BACK 버튼 누르면 취소가능
                            if (isCancelled()) {
                                input.close();
                             //   return Long.valueOf(-1);
                            }

                            downloadedSize += count;

                            if (FileSize > 0) {
                                float per = ((float)downloadedSize/FileSize) * 100;
                                String str = "Downloaded " + downloadedSize + "KB / " + FileSize + "KB (" + (int)per + "%)";
                                publishProgress("" + (int) ((downloadedSize * 100) / FileSize), str);

                            }

                            //파일에 데이터를 기록합니다.
                            output.write(data, 0, count);
                        }
                        // Flush output
                        output.flush();

                        // Close streams
                        output.close();
                        input.close();
                        break;

                     default:
                        break;
                }

                System.out.println("Response OK");
            }


            else {
                inputStream = httpURLConnection.getErrorStream();
                System.out.println("Response Failed");
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            httpURLConnection.disconnect();
            System.out.println("here : " + stringBuilder.toString());
            finish = true;
            return stringBuilder.toString();//go to onPostExecute
        } catch (IOException e) {
            System.out.println("IO Error");
        }
        return null;


    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println("onPost : " + s);
    }
}

class Network_Access implements Serializable {
    public boolean Server_Response;

    public String Network_Access(String Action, String Network_data) {
        Network n = new Network();//for Using Network without AsyncTask error
        n.Input_data(Action, Network_data);//Sending Data & kind of command to Network Class
        try {
            Network_data = n.execute().get(); //execute Network and take return value to Network_data
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.Server_Response = n.Server_Response;
        System.out.println("Network Access n.Response : " + n.Server_Response);
        System.out.println("Network Access na.Response : " + this.Server_Response);
        while (true) {
            if (n.finish == true) {  //when Network doInBackground is End
                System.out.println("Asyn finish");
                break;
            }
        }
        return Network_data;
    }


}

