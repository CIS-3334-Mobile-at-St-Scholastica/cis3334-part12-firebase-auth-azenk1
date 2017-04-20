package css.cis3334.firebaseauthentication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private TextView textViewStatus;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonGoogleLogin;
    private Button buttonCreateLogin;
    private Button buttonSignOut;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final String TAG = "CIS3334";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonGoogleLogin = (Button) findViewById(R.id.buttonGoogleLogin);
        buttonCreateLogin = (Button) findViewById(R.id.buttonCreateLogin);
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);

        //Initialize FirebaseAuth instance by storing an instance of the default FirebaseApp
        //instance in mAuth
        mAuth = FirebaseAuth.getInstance();

        //Initialize AuthStateListener and create method within
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            /**
             * Method is called when the authorization state changes
             * @param firebaseAuth
             */
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {

                //Returns the currently signed in firebase user or null if there is not one.
                FirebaseUser user = firebaseAuth.getCurrentUser();

                /**
                 *Control structure used to determine if the user has signed in our out. Removed
                 * for now as per instructions to remove log calls.

                if(user != null)
                {
                    //User signed on
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
                }else
                {
       x`x`x`             //User signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                 */
            }
        };

        /**
         * Attempts to perform a login when the buttonLogin button is pressed. Here, the signIn
         * method is called to do this. It is passed the text entered for the email address as well
         * as the text entered for the password.
         */
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /**
                 * Removed log call. This Log call shows that a normal login was performed as
                 * opposed to a google login. The CIS3334 string is used to identify the Tag.
                Log.d("CIS3334", "normal login ");
                */
                signIn(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        /**
         * Sets onClickListener for buttonCreateLogin button and calls the method createAccount
         * to create an account using the text entered for email and the text entered for password
         * as parameters.
         */
        buttonCreateLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /** Log call letting us know that an account has been created.
                Log.d("CIS3334", "Create Account ");
                */
                createAccount(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        /**
         * Sets onClickListener for buttonGoogleLogin button and then calls googleSignIn() method
         * which enables a user to sign in using their Google account.
         */
        buttonGoogleLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /**
                 * Removed Log call. Provides information in the log letting us know that a Google
                 * login has been processed versus a normal login.
                Log.d("CIS3334", "Google login ");
                */
                googleSignIn();
            }
        });

        /**
         * Sets onClickListener for the buttonSignOut button and then calls the signOut() method
         * which allows the user to sign out.
         */
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log Removed. Log message showing a sign out has occurred.
                //Log.d("CIS3334", "Logging out - signOut ");
                signOut();
            }
        });



    }

    /**
     * Attach FirebaseAuth instance at start
     * Makes call to onStart() method of super class inside overridden method.
     * AuthStateListener mAuthListener is added to mAuth to allow for the listener to observe
     * changes in authentication state.
     */
    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Remove FirebaseAuth at stop.
     * onStop() method of super class is called in overriden method.
     * When stopped if the AuthListener is not null it is then removed.
     */
    @Override
    public void onStop()
    {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Creates an account using the parameters listed below
     * @param email - Email address provided by the user.
     * @param password - Password provided by the user.
     * Instance of FirebaseAuth is the used to call createUserWithEmailAndPassword method to
     * create a user.
     */
    private void createAccount(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

            // If sign in fails, display a message to the user. If sign in succeeds
            // the auth state listener will be notified and logic to handle the
            // signed in user can be handled in the listener.
            if (!task.isSuccessful()) {

                /** Toast replaced by update to textViewStatus displaying status of authentication.
                Toast.makeText(MainActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                */
                textViewStatus.setText(R.string.failed);
            }

            //If user is created successful display success status.
            if(task.isSuccessful())
            {
                textViewStatus.setText(R.string.success);
            }

            // ...
        }
    });
    }

    /**
     * Method called to sign in user that has created account. The following parameters are used to
     * authenticate the user.
     * @param email - email that was provided by the user upon account creation
     * @param password - password that was provided by the user upon account creation.
     */
    private void signIn(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());


                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            /**
                             * Log showing exception and toast replaced with update to
                             * textViewStatus
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            */

                            textViewStatus.setText(R.string.failed);
                         }

                         //If sign  in is successful display success status
                         if(task.isSuccessful())
                         {
                             textViewStatus.setText(R.string.success);
                         }

                        // ...
                    }
                });
    }

    /**
     * Sign the user out utilizing the signOut method provided by instance of FirebaseAuth
     */
    private void signOut ()
    {
        mAuth.signOut();
    }


    /**
     * Method to be developed allowing the user to sign in using Google credentials.
     */
    private void googleSignIn() {

    }




}
