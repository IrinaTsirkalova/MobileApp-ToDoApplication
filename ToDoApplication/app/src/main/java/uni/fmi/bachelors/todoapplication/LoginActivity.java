package uni.fmi.bachelors.todoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import uni.fmi.bachelors.todoapplication.database.DBHelper;

public class LoginActivity extends AppCompatActivity {

    EditText usernameET;
    EditText passwordET;
    Button loginB;
    Button registerB;
    DBHelper dbHelper;
    TextView userLogHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.usernameEditText);
        passwordET = findViewById(R.id.passwordEditText);
        loginB = findViewById(R.id.loginButton);
        registerB = findViewById(R.id.registerButton);
        userLogHistory = findViewById(R.id.logHistoryTextView);

        dbHelper = new DBHelper(this);
        registerB.setOnClickListener(onClickListener);
        loginB.setOnClickListener(onClickListener);
        userLogHistory.setOnClickListener(onClickLog);
    }

    View.OnClickListener onClickLog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(LoginActivity.this, UserLogHistoryActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = null;
            String username;
            String password;
            int userId = 0;
            //if register is clicked go to RegisterActivity
            if(v.getId() == R.id.registerButton){
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                return;
            }

            if(usernameET.getText().length()> 0 && passwordET.getText().length()>0){
                username = usernameET.getText().toString();
                password = passwordET.getText().toString();

                Bundle bundle = new Bundle();

                try {
                    userId = dbHelper.selectUserId(username,password);
                    bundle.putInt("userId",userId);

                }catch(Exception e){
                    Log.i("UserIdError",e.getMessage());
                }

                if(dbHelper.userLogin(username,password)){
                    intent = new Intent(LoginActivity.this,AllTasksActivity.class);
                    if(userId != 0){//if userId exists then send userId to AllTasksActivity
                        dbHelper.userLogin(userId);
                        intent.putExtras(bundle);
                    }else{
                        Log.i("UserIdLogin:","is 0");
                    }
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,"Wrong username or password!",
                            Toast.LENGTH_LONG).show();
                }

            }



        }
    };
}