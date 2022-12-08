package com.example.taxi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PassengerSingInActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private static final String TAG = "PassengerSignInActivity";

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputName;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputConfirmPassword;

    private Button loginSignUpButton;
    private TextView toggleLoginSignUpPassTextView;

    private boolean isLoginModeActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_sing_in);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(PassengerSingInActivity.this,
                    PassengerMapsActivity.class));
        }

        textInputEmail = findViewById(R.id.textInputEmail);
        textInputName = findViewById(R.id.textInputName);
        textInputPassword = findViewById(R.id.textInputPassword);
        textInputConfirmPassword = findViewById(R.id.textInputConfirmPassword);

        loginSignUpButton = findViewById(R.id.loginSignUpButton);
        toggleLoginSignUpPassTextView = findViewById(R.id.toggleLoginSignUpPassTextView);

    }

    private boolean validateEmail() {

        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Please, input your email");
            return false;
        } else {
            textInputEmail.setError("");
            return true;
        }

    }

    private boolean validateName() {

        String nameInput = textInputName.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            textInputName.setError("Please, input your name");
            return false;
        } else if (nameInput.length() > 15) {
            textInputName.setError("Your name is too long");
            return false;
        } else {
            textInputName.setError("");
            return true;
        }

    }

    private boolean validatePassword() {

        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Please, input your password");
            return false;
        } else if (passwordInput.length() > 20) {
            textInputPassword.setError("Your password is too long");
            return false;
        } else if (passwordInput.length() < 7) {
            textInputPassword.setError("Your password is too short");
            return false;
        } else {
            textInputPassword.setError("");
            return true;
        }

    }

    private boolean validateConfirmPassword() {

        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        String confirmPasswordInput = textInputConfirmPassword.getEditText().getText()
                .toString().trim();

        if (!passwordInput.equals(confirmPasswordInput)) {
            textInputPassword.setError("Passwords don't match");
            return false;
        } else {
            textInputPassword.setError("");
            return true;
        }

    }

    public void loginSignUpPass(View view) {
        if (!validateEmail() | !validateName() | !validatePassword()) {
            return;
        }

        if (isLoginModeActive) {

            auth.signInWithEmailAndPassword(textInputEmail.getEditText().getText().toString().trim(),
                    textInputPassword.getEditText().getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = auth.getCurrentUser();
                                Log.d(TAG, "signInWithEmail:success");
                                startActivity(new Intent(PassengerSingInActivity.this,
                                        PassengerMapsActivity.class));
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(PassengerSingInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });

        } else {

            if (!validateEmail() | !validateName() | !validatePassword() |
                    !validateConfirmPassword()) {
                return;
            }

            auth.createUserWithEmailAndPassword(
                    textInputEmail.getEditText().getText().toString().trim(),
                    textInputPassword.getEditText().getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = auth.getCurrentUser();
                                Log.d(TAG, "createUserWithEmail:success");
                                startActivity(new Intent(PassengerSingInActivity.this,
                                        PassengerMapsActivity.class));
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(PassengerSingInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });

        }
    }

    public void toggleLoginSignUp(View view) {

        if (isLoginModeActive) {
            isLoginModeActive = false;
            loginSignUpButton.setText("Sign Up");
            toggleLoginSignUpPassTextView.setText("Or, Log In");
            textInputConfirmPassword.setVisibility(View.VISIBLE);
        } else {
            isLoginModeActive = true;
            loginSignUpButton.setText("Log In");
            toggleLoginSignUpPassTextView.setText("Or, Sign Up");
            textInputConfirmPassword.setVisibility(View.GONE);
        }

    }
}