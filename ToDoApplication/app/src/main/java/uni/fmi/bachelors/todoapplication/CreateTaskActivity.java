package uni.fmi.bachelors.todoapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import uni.fmi.bachelors.todoapplication.database.DBHelper;
import uni.fmi.bachelors.todoapplication.database.Task;

public class CreateTaskActivity extends AppCompatActivity {

    EditText taskNameET;
    ScrollView descriptionSV;
    EditText descriptionET;
    Button saveTaskB;

    DBHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        taskNameET = findViewById(R.id.taskNameUpdateEditText);
        descriptionSV = findViewById(R.id.descriptionUpdateScrollView);
        descriptionET = findViewById(R.id.descriptionUpdateEditText);
        saveTaskB = findViewById(R.id.saveTaskButton);


        db = new DBHelper(this);
        saveTaskB.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String taskName;
            String description;
            taskName = taskNameET.getText().toString();
            description = descriptionET.getText().toString();
            Task task;
            Intent intent = null;
            int userId = 0;

            Bundle bundle = getIntent().getExtras();//catch data from AllTaskActivity
            Bundle sendId = new Bundle();//send userId to AllTaskActivity
            if(bundle != null){
                userId = bundle.getInt("userId");
            }else{
                Toast.makeText(CreateTaskActivity.this,"Something went wrong!!!",Toast.LENGTH_LONG).show();
            }


            if(taskName.length()>0 && description.length()>0 && userId != 0){

                task = new Task(taskName,description,userId);
                try{
                    db.createTask(task);
                }catch (Exception e){
                    Log.i("CreateTaskError",e.getMessage());
                }
                intent = new Intent(CreateTaskActivity.this,AllTasksActivity.class);
                sendId.putInt("userId",userId);
                intent.putExtras(sendId);
                startActivity(intent);
            }else{
                Toast.makeText(CreateTaskActivity.this,"Something went wrong!Try again.",Toast.LENGTH_LONG).show();
            }




        }
    };


}