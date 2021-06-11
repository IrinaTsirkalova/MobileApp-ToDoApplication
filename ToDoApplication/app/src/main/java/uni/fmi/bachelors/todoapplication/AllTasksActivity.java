package uni.fmi.bachelors.todoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import uni.fmi.bachelors.todoapplication.database.DBHelper;
import uni.fmi.bachelors.todoapplication.database.Task;

public class AllTasksActivity extends AppCompatActivity {

    TextView taskTV;
    ListView taskLV;
    Button addB;
    DBHelper dbHelper;
    Button logoutB;
    int userId = 0;

    Button accountB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        taskTV = findViewById(R.id.todoTextView);
        taskLV = findViewById(R.id.allTasksListView);
        addB = findViewById(R.id.addButton);
        logoutB = findViewById(R.id.logoutButton);
        accountB = findViewById(R.id.accountButton);

        addB.setOnClickListener(onClick);
        logoutB.setOnClickListener(onClickLogout);
        accountB.setOnClickListener(onClickAccount);

        dbHelper = new DBHelper(this);

        try{
            Bundle bUserId = getIntent().getExtras();
            userId = bUserId.getInt("userId");
        }catch (Exception e){
            Log.i("BundleUserIdError",e.getMessage());
        }

        try {
            FillListTask();
        } catch (Exception e) {
           Log.i("FillTaskError",e.getMessage());
        }

        taskLV.setOnItemClickListener(onTaskClick);
    }

    public void FillListTask() {

        ArrayList<Task> taskArrayList = null;
        try {
            taskArrayList = dbHelper.GetAllTasks(userId);//selectAllTasks
        }catch (Exception e){
            Log.i("GetAllTasksError", e.getMessage());
        }
        final ArrayList<String> nameDescription = new ArrayList<>();//array that will contain name and description
        int taskId;
        String taskName,taskDescription;
        //fill array with name and description for every task
        for(Task task: taskArrayList){
             taskId = task.getIdTask();
             taskName = task.getTaskName();
             taskDescription = task.getDescription();

             nameDescription.add(taskId +" \t "+taskName+" \t " + taskDescription );
        }

        taskLV.clearChoices();
        //set taskTextView to show all the tasks in the database/with the data in nameDescription/
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_all_tasks_list,
                R.id.taskTextView,
                nameDescription);

        taskLV.setAdapter(arrayAdapter);

    }

    View.OnClickListener onClick = new View.OnClickListener() {
        //Add Button on click
        @Override
        public void onClick(View v) {
            //catch userId from LoginActivity  and send it to CreateTaskActivity
            Intent intent = new Intent(AllTasksActivity.this,CreateTaskActivity.class);
            Bundle bundle = getIntent().getExtras();;//to catch the data from LoginActivity
            intent.putExtras(bundle);//Send data to CreateTaskActivity
            startActivity(intent);

        }
    };

    AdapterView.OnItemClickListener onTaskClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView clickedTask = view.findViewById(R.id.taskTextView);
            String selectedTask = clickedTask.getText().toString();
            String[] elements = selectedTask.split("\t");
            String taskId = elements[0];
            String taskName = elements[1];
            String taskDescription = elements[2];
            Intent intent = new Intent(AllTasksActivity.this, UpdateDeleteTaskActivity.class);
            Bundle bundle = new Bundle();//send data to UpdateDeleteActivity
            bundle.putInt("userId", userId);
            bundle.putString("taskId", taskId);
            bundle.putString("taskName",taskName);
            bundle.putString("taskDescription",taskDescription);
            intent.putExtras(bundle);
            startActivityForResult(intent, 200, bundle);
        }
    };

    View.OnClickListener onClickAccount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AllTasksActivity.this,AccountActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("userId", userId);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    View.OnClickListener onClickLogout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            dbHelper.userLogout(userId);
            finish();
            Intent intent = new Intent(AllTasksActivity.this,LoginActivity.class);
            startActivity(intent);

        }
    };
}