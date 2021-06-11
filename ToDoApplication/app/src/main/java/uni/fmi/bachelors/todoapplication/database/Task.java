package uni.fmi.bachelors.todoapplication.database;

public class Task {

    private int idTask;
    private String taskName;
    private String description;
    private int userId;

    //Constructor needed for CreateTaskActivity
    public Task(String taskName, String description, int userId) {
        this.taskName = taskName;
        this.description = description;
        this.userId = userId;
    }

    //Constructor needed for GetAllTask() in DBHelper
    public Task(int idTask,String taskName, String description, int userId) {
        this.idTask = idTask;
        this.taskName = taskName;
        this.description = description;
        this.userId = userId;
    }

    public int getIdTask() {
        return idTask;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
