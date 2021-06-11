package uni.fmi.bachelors.todoapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import uni.fmi.bachelors.todoapplication.database.DBHelper;
import uni.fmi.bachelors.todoapplication.database.User;

public class AccountActivity extends AppCompatActivity {

    DBHelper db;
    int userId;
    EditText fNameUET;
    EditText lNameUET;
    EditText usernameUET;
    EditText passwordUET;
    EditText repeatPasswordUET;
    Button updateB;
    Button cancelB;
    Button deleteUserB;
    RadioGroup genderURG;
    RadioButton maleURB;
    RadioButton femaleURB;
    RadioButton otherURB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        fNameUET = findViewById(R.id.fNameUpdateEditText);
        lNameUET = findViewById(R.id.lNameUpdateEditText);
        usernameUET = findViewById(R.id.usernameUpdateEditText);
        passwordUET = findViewById(R.id.passwordUpdateEditText);
        repeatPasswordUET = findViewById(R.id.repeatePasswordUpdateEditText);
        genderURG = findViewById(R.id.genderUpdateRadioGroup);
        maleURB = (RadioButton) findViewById(R.id.maleUpdateRadioButton);
        femaleURB = (RadioButton) findViewById(R.id.femaleUpdateRadioButton);
        otherURB = (RadioButton) findViewById(R.id.otherUpdateRadioButton);
        updateB = findViewById(R.id.saveUserUpdateButton);
        cancelB =findViewById(R.id.cancelUpdateUserButton);
        deleteUserB = findViewById(R.id.deleteAccountButton);

        db = new DBHelper(this);

        Bundle bUserId = getIntent().getExtras();
        userId = bUserId.getInt("userId");

        FillAccount();

        updateB.setOnClickListener(onClickUpdate);
        cancelB.setOnClickListener(onClickCancel);
        deleteUserB.setOnClickListener(onClickDeactivate);
    }

    public  void FillAccount(){
        ArrayList<User> userArrayList =  db.selectUserById(userId);

        for(User user : userArrayList){
            fNameUET.setText(user.getFName());
            lNameUET.setText(user.getLName());
            usernameUET.setText(user.getUsername());
            passwordUET.setText(user.getPassword());
            if(user.getGender().toString().equals("Male")){
                maleURB.setChecked(true);
            }else if(user.getGender().toString().equals("Female")){
               femaleURB.setChecked(true);
            }else if(user.getGender().toString().equals("Other")){
                otherURB.setChecked(true);
            }
        }
    }

    View.OnClickListener onClickUpdate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String fName,lName,username, password, repeatPassword,gender;

            fName = fNameUET.getText().toString();
            lName= lNameUET.getText().toString();
            username = usernameUET.getText().toString();
            password = passwordUET.getText().toString();
            repeatPassword = repeatPasswordUET.getText().toString();

            int checkedRB = genderURG.getCheckedRadioButtonId();
            RadioButton rb = findViewById(checkedRB);
            gender = rb.getText().toString();

            User user = new User(fName,lName,username,password,gender);
                if(password.equals(repeatPassword)){

                    db.editUser(user,userId);
                    Toast.makeText(AccountActivity.this,
                        "Account is updates",
                        Toast.LENGTH_LONG).show();
                     finish();

                 }else{
                    Toast.makeText(AccountActivity.this,
                        "Password and repeat password are different!",
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    View.OnClickListener onClickCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    View.OnClickListener onClickDeactivate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog deleteMessage = new AlertDialog.Builder(AccountActivity.this).create();
            deleteMessage.setTitle("Warning!!!");
            deleteMessage.setMessage("Are you sure you want to deactivate your profile ?");

            deleteMessage.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            deleteMessage.setButton(DialogInterface.BUTTON_POSITIVE,"Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.deleteUser(userId);
                    finish();
                    Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
            deleteMessage.show();
        }
    };
}