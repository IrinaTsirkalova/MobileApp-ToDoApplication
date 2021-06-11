package uni.fmi.bachelors.todoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import uni.fmi.bachelors.todoapplication.database.DBHelper;
import uni.fmi.bachelors.todoapplication.database.Task;
import uni.fmi.bachelors.todoapplication.database.User;

public class UserLogHistoryActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ListView userLogLV;
    TextView logTV;
    Button backB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log_history);

        userLogLV = findViewById(R.id.logListView);
        dbHelper = new DBHelper(this);
        logTV = findViewById(R.id.textLogTextView);
        backB = findViewById(R.id.backButton);

        backB.setOnClickListener(onClickBack);
        FillLogList();
    }


    public void FillLogList() {

        ArrayList<User> userArrayList = null;
        try {
           userArrayList = dbHelper.GetAllUsers();
        }catch (Exception e){
            Log.i("GetAllUsersError", e.getMessage());
        }
        final ArrayList<String> usersLog = new ArrayList<>();//array that will contain name and description
        String username,loginDate, logoutDate;
        for(User user: userArrayList){
           username = user.getUsername();
           loginDate = user.getUserLoginDate();
           logoutDate = user.getUserLogoutDate();
          usersLog.add("Username: "+username +"\nLast login: " +loginDate+"\nLast logout: " + logoutDate);
        }

        userLogLV.clearChoices();
        //set taskTextView to show all the tasks in the database/with the data in nameDescription/
       ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
               getApplicationContext(),
               R.layout.activity_user_log_list,
               R.id.textLogTextView,
               usersLog);

        userLogLV.setAdapter(arrayAdapter);
    }

    View.OnClickListener onClickBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}