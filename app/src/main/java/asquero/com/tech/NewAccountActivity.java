package asquero.com.tech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class NewAccountActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;

    private String mname;
    private String memail;
    private String mpassword;

    private FirebaseAuth mAuth;

    private ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mRegProgress = new ProgressDialog(this);

        mDisplayName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        Button mCreateButton = findViewById(R.id.createBtn);

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mname = mDisplayName.getEditText().getText().toString();
                memail = mEmail.getEditText().getText().toString();
                mpassword = mPassword.getEditText().getText().toString();

                if (!TextUtils.isEmpty(mname) || !TextUtils.isEmpty(memail) || !TextUtils.isEmpty(mpassword)) {

                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account!!");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    register_user(mname, memail, mpassword);

                }

                else Toast.makeText(NewAccountActivity.this, "Fields cannot be left black", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register_user(String mname, String memail, String mpassword) {

        mAuth.createUserWithEmailAndPassword(memail, mpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            sendVerificationEmail();
                            mRegProgress.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            mRegProgress.hide();

                            if (task.getException()instanceof FirebaseAuthWeakPasswordException){
                                Toast.makeText(NewAccountActivity.this, "Password should be more than 6 letters", Toast.LENGTH_SHORT).show();
                            }
                            else if (task.getException()instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(NewAccountActivity.this, "Please check the email", Toast.LENGTH_SHORT).show();
                            }
                            else if (task.getException()instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(NewAccountActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                Toast.makeText(NewAccountActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        // ...
                    }
                });

    }

    private void sendVerificationEmail() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(NewAccountActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();

                        Intent backIntent = new Intent(NewAccountActivity.this,SignInActivity.class);
                        startActivity(backIntent);
                        finish();
                    }
                }
            });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            // ...

            // up button
            case android.R.id.home:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}