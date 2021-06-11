package uni.fmi.bachelors.todoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import uni.fmi.bachelors.todoapplication.database.DBHelper;
import uni.fmi.bachelors.todoapplication.database.User;

public class RegisterActivity extends AppCompatActivity {

    EditText fNameET;
    EditText lNameET;
    EditText usernameET;
    EditText passwordET;
    EditText repeatPasswordET;
    RadioGroup genderRB;
    Button registerB;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fNameET = findViewById(R.id.fNameUpdateEditText);
        lNameET = findViewById(R.id.lNameUpdateEditText);
        usernameET = findViewById(R.id.usernameEditText);
        passwordET = findViewById(R.id.passwordEditText);
        repeatPasswordET = findViewById(R.id.repeatPasswordEditText);
        genderRB = findViewById(R.id.genderRadioButton);
        registerB = findViewById(R.id.registerButton);

        registerB.setOnClickListener(onClickListener);
        dbHelper = new DBHelper(this);

    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String fName,lName,username,password,gender;

            if(fNameET.getText().length() > 0
                    && lNameET.getText().length() > 0
                    && usernameET.getText().length() > 0
                    && passwordET.getText().length() > 0
                    && genderRB.getCheckedRadioButtonId() != 0
                    && repeatPasswordET.getText().toString().equals(passwordET.getText().toString())
            ) {

                fName = fNameET.getText().toString();
                lName = lNameET.getText().toString();
                username = usernameET.getText().toString();
                password = passwordET.getText().toString();

                int checkedRB = genderRB.getCheckedRadioButtonId();
                RadioButton rb = findViewById(checkedRB);
                gender = rb.getText().toString();


                User user = new User(fName, lName, username, password, gender);
                if(dbHelper.createUser(user)){
                     dbHelper.createUser(user);
                     Toast.makeText(RegisterActivity.this,
                            "Registration is successful",
                            Toast.LENGTH_LONG).show();
                     Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                     startActivity(intent);

                }else{
                    Toast.makeText(RegisterActivity.this,
                            "Username is taken",
                            Toast.LENGTH_LONG).show();

                }
            }else{
                Toast.makeText(RegisterActivity.this,
                        "Please fill the form!",
                        Toast.LENGTH_LONG).show();
                
            }


        }
    };

}