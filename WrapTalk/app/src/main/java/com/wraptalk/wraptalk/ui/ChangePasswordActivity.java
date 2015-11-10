package com.wraptalk.wraptalk.ui;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


/*
    // UI references.
    private EditText editText_nowPassword;
    private EditText editText_changePassword1;
    private EditText editText_changePassword2;
    private View progress_changePassword;
    private View change_password_form;
    private Button button_changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Set up the signup form.

        Init();

        populateAutoComplete();

        editText_nowPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.signup || id == EditorInfo.IME_NULL) {
                    attemptSignup();
                    return true;
                }
                return false;
            }
        });

        editText_changePassword1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.signup || id == EditorInfo.IME_NULL) {
                    attemptSignup();
                    return true;
                }
                return false;
            }
        });

        editText_password2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.signup || id == EditorInfo.IME_NULL) {
                    attemptSignup();
                    return true;
                }
                return false;
            }
        });

        button_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignup();
            }
        });
    }

    private void Init() {

        //sendPostSignIn = new ();

        editText_nowPassword = (EditText) findViewById(R.id.editText_nowPassword);
        editText_changePassword1 = (EditText) findViewById(R.id.editText_changePassword1);
        editText_changePassword2 = (EditText) findViewById(R.id.editText_changePassword2);

        button_changePassword = (Button) findViewById(R.id.button_changePassword);

        progress_changePassword = findViewById(R.id.progress_changePassword);
        change_password_form = findViewById(R.id.change_password_form);
    }


    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    private void attemptSignup() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        editText_nowPassword.setError(null);
        editText_password1.setError(null);
        editText_password2.setError(null);

        // Store values at the time of the signup attempt.
        String nowpassword = editText_nowPassword.getText().toString();
        String password1 = editText_password1.getText().toString();
        String password2 = editText_password2.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password1) && (!isPasswordValid(password1))) {
            editText_password1.setError(("This password is too short"));
            focusView = editText_password1;
            cancel = true;
        } else if (!password1.equals(password2)) {
            Log.e(password1, password2);
            editText_password2.setError("The password2 is different from the password1");
            focusView = editText_password2;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            editText_email.setError("This field is required");
            focusView = editText_email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editText_email.setError("This email address is invalid");
            focusView = editText_email;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user signup attempt.
            showProgress(true);
            mAuthTask = new UserSignUpTask(email, password1);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            signup_form.setVisibility(show ? View.GONE : View.VISIBLE);
            signup_form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    signup_form.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progress_signup.setVisibility(show ? View.VISIBLE : View.GONE);
            progress_signup.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progress_signup.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress_signup.setVisibility(show ? View.VISIBLE : View.GONE);
            signup_form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignUpActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        editText_email.setAdapter(adapter);
    }

    public class UserSignUpTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;

        UserSignUpTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String content = null;

            try {
                // Simulate network access.
                content = executeClient();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return content;
                }
            }

            // TODO: register the new account here.
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            mAuthTask = null;
            showProgress(false);

            if (result.equals("true")) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            } else if (result.equals("fail")) {
                editText_email.setError("This email is duplicates");
                editText_email.requestFocus();
            } else {
                Toast.makeText(getApplicationContext(), "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        public String executeClient() throws IOException {

            String body = null;

            body = "user_id=" + mEmail + "&user_pw=" + mPassword + "&device_id=" + UserInfo.getInstance().deviceId + "&gcm_id=" + UserInfo.getInstance().gcmKey;

            URL url = new URL("http://133.130.113.101:7010/user/join?" + body);

            Log.e("서버에 보내는 내용", url.toString());


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

            try (
                    InputStream is = conn.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(40960);
            ) {
                // 데이터 받는 부분
                byte tmpRead[] = new byte[10240];

                while (true) {
                    int nRead = is.read(tmpRead);
                    if (nRead == -1)
                        break;
                    baos.write(tmpRead, 0, nRead);
                }

                byte readData[] = baos.toByteArray();
                String strData = new String(readData);
                Log.e("서버", strData);

                if (strData.contains("success")) {
                    Log.e("SignUp 결과", "성공");
                    return "true";
                } else if (strData.contains("-1")) {
                    return "fail";
                } else {
                    Log.e("SignUp 결과", "실패");
                    return "false";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "false";
        }
    }*/
}