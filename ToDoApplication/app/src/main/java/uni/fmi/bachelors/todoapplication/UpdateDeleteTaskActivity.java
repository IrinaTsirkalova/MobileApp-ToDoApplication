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

import static java.lang.Integer.parseInt;

public class UpdateDeleteTaskActivity extends AppCompatActivity {

    EditText taskNameUpdateET;
    EditText descriptionUpdateET;
    ScrollView descriptionUpdateSV;
    Button updateB;
    Button deleteB;
    Button cancelB;
    DBHelper dbHelper;

    String taskId;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_task);
        taskNameUpdateET = findViewById(R.id.taskNameUpdateEditText);
        descriptionUpdateSV = findViewById(R.id.descriptionUpdateScrollView);
        descriptionUpdateET = findViewById(R.id.descriptionUpdateEditText);
        updateB = findViewById(R.id.updateTaskButton);
        deleteB = findViewById(R.id.deleteTaskButton);
        cancelB = findViewById(R.id.cancelButton);


        dbHelper = new DBHelper(this);

        Bundle b = getIntent().getExtras();//get data from AllTaskActivity on Update/Delete
        if(b!=null){
            userId = b.getInt("userId");
            taskId = b.getString("taskId");
            taskNameUpdateET.setText(b.getString("taskName"));
            descriptionUpdateET.setText(b.getString("taskDescription"));
        }
        try{
            updateB.setOnClickListener(onUpdateClick);
        }catch (Exception e){
            Toast.makeText(this,"Task update failed!",Toast.LENGTH_LONG).show();
            Log.i("UpdateTaskError:",e.getMessage());
        }
        try{
            deleteB.setOnClickListener(onDeleteClick);
        }catch (Exception e){
            Toast.makeText(this,"Task delete failed!",Toast.LENGTH_LONG).show();
            Log.i("DeleteTaskError:",e.getMessage());
        }
        cancelB.setOnClickListener(onCancelClick);

    }

    View.OnClickListener onUpdateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = taskNameUpdateET.getText().toString();//getName
            String desc = descriptionUpdateET.getText().toString();//getDesc

            try {
                dbHelper.editTask(taskId, name, desc);//taskUpdate
                Toast.makeText(UpdateDeleteTaskActivity.this, "Task updated!", Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(UpdateDeleteTaskActivity.this, "Task update failed!", Toast.LENGTH_LONG).show();
                Log.i("UpdateTaskError",e.getMessage());
            }
            Bundle bundle = new Bundle();//get userId From AllTaskActivity
            bundle.putInt("userId", userId);
            finishActivity(200);
            Intent intent =new Intent(UpdateDeleteTaskActivity.this,AllTasksActivity.class);
            intent.putExtras(bundle);//send userId To AllTaskActivity
            startActivity(intent);
        }
    };

    View.OnClickListener onDeleteClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                dbHelper.deleteTask(taskId);
                Toast.makeText(UpdateDeleteTaskActivity.this,"Task deleted successfully",Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(UpdateDeleteTaskActivity.this,"Delete failed!!!",Toast.LENGTH_LONG).show();
                Log.i("DeleteError",e.getMessage());
            }

            Bundle bundle = new Bundle();//send userId to AllTaskActivity
            bundle.putInt("userId", userId);
            finishActivity(200);
            Intent intent = new Intent(UpdateDeleteTaskActivity.this,AllTasksActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    View.OnClickListener onCancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


}