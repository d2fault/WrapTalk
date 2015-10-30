package com.wraptalk.wraptalk;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpActivity extends AppCompatActivity {

    SendPostSignUp sendPostSignUp;
    EditText editText_email, editText_password1, editText_password2;
    Button button_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Init();
        listener();
    }

    public void Init() {
        sendPostSignUp = new SendPostSignUp();

        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password1 = (EditText) findViewById(R.id.editText_password1);
        editText_password2 = (EditText) findViewById(R.id.editText_password2);

        button_signUp = (Button) findViewById(R.id.button_join);
    }

    public void listener() {

        button_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //editText_email.getText().toString();
                sendPostSignUp.execute();
            }
        });
    }

    private class SendPostSignUp extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... unused) {
            String content = null;
            try {
                content = executeClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }

        protected void onPostExecute(String result) {
            // 모두 작업을 마치고 실행할 일 (메소드 등등)

        }

        // 실제 전송하는 부분
        public String executeClient() throws IOException {

            String body = "user_id=abcde@b.com&user_pw=123456&device_id=temp&gcm_id=temp";

            URL url = new URL("http://133.130.113.101:7010/user/join?" + body);

            // 전송하는 부분

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            conn.setDoOutput(true);
            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            conn.setDoInput(true);



            try (OutputStream os = conn.getOutputStream();) {
                os.write(body.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try(
                    InputStream is = conn.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(40960);
            ) {

                // 데이터 받는 부분

                byte tmpRead[] = new byte[10240];

                while(true) {
                    int nRead = is.read(tmpRead);
                    if(nRead == -1)
                        break;
                    baos.write(tmpRead, 0, nRead);
                }

                byte readData[] = baos.toByteArray();
                String strData = new String(readData);

                Log.e("서버에서 받은 내용", strData);

//                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
//                osw.write(body);
//                osw.flush();
//
//                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
//                BufferedReader reader = new BufferedReader(tmp);
//                StringBuilder builder = new StringBuilder();
//                String str;
//
//                while ((str = reader.readLine()) != null) {
//                    builder.append(str);
//                }
//                sResult = builder.toString();


            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }
    }
}