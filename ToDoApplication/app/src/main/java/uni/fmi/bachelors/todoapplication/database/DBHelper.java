package uni.fmi.bachelors.todoapplication.database;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    //Database
    public static final int DB_VERSION = 4;
    public static final String DB_NAME = "to-do.sqlite";

    //Table User
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_FNAME = "fname";
    public static final String COLUMN_USER_LNAME = "lname";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_USER_GENDER = "gender";
    public static final String COLUMN_USER_LOGIN = "lastLoginDate";
    public static final String COLUMN_USER_LOGOUT = "lastLogoutDate";
    //Table Task
    public static final String TABLE_TASK = "task";
    public static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_TASK_NAME = "taskName";
    public static final String COLUMN_TASK_DESCRIPTION = "description";
    public static final String COLUMN_TASK_USER_ID = "userId";

    //Constructor
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //creates table user and table task
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_USER + "(" + COLUMN_USER_ID + " integer not null primary key autoincrement," +
                COLUMN_USER_FNAME + " nvarchar(50) not null," +
                COLUMN_USER_LNAME + " nvarchar(50) not null," +
                COLUMN_USERNAME + "  nvarchar(50) not null unique," +
                COLUMN_PASSWORD + "  nvarchar(50) not null," +
                COLUMN_USER_GENDER + "  nvarchar(30) not null," +
                COLUMN_USER_LOGIN + " text, "+
                COLUMN_USER_LOGOUT +" text" + ")");
        db.execSQL("create table " + TABLE_TASK + "(" + COLUMN_TASK_ID + " integer not null primary key autoincrement," +
                COLUMN_TASK_NAME + " nvarchar(100) not null," +
                COLUMN_TASK_DESCRIPTION + "  nvarchar(3000) not null," +
                COLUMN_TASK_USER_ID + " integer not null," +
                " CONSTRAINT fk_users foreign key(" + COLUMN_TASK_USER_ID + ") references user(" + COLUMN_USER_ID + ") on delete cascade);");

    }

    //On alter table/database upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);//if
        //we want to alter table we have to delete all existing rows
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    //createUser
    public boolean createUser(User user) {
        SQLiteDatabase db = null;
        SQLiteDatabase dbSelect = null;
        try {
            db = getWritableDatabase();//open writable database connection
            //getReadableDatabese() to check if username exists
            dbSelect = getReadableDatabase();

            Cursor cursor = dbSelect.rawQuery("SELECT " + COLUMN_USERNAME + " FROM " + TABLE_USER + " WHERE " + COLUMN_USERNAME + " = ?",
                    new String[]{user.getUsername()});
            cursor.moveToFirst();

            //check if username exists
            while (!cursor.isAfterLast()) {
                if (user.getUsername().equals(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)))) {
                    return false;
                } else {
                    cursor.moveToNext();
                }
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_USER_FNAME, user.getFName());
            contentValues.put(COLUMN_USER_LNAME, user.getLName());
            contentValues.put(COLUMN_USERNAME, user.getUsername());
            contentValues.put(COLUMN_PASSWORD, user.getPassword());
            contentValues.put(COLUMN_USER_GENDER, user.getGender());

            // insert() return the row ID of the newly inserted row,
            // or -1 if an error occurred
            if(db.insert(TABLE_USER, null, contentValues) != -1){
                return true;
            }
        } catch (SQLException e) {
            Log.i("CreateUserError", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        //if insert is unsuccessful
        return false;
    }

    //createTask
    public boolean createTask(Task task) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_TASK_NAME, task.getTaskName());
            contentValues.put(COLUMN_TASK_DESCRIPTION, task.getDescription());
            contentValues.put(COLUMN_TASK_USER_ID, task.getUserId());

            return db.insert(TABLE_TASK, null, contentValues) != -1;
        } catch (SQLException e) {
            Log.i("CreateTaskError", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        //if insert is unsuccessful
        return false;
    }

    //Table Task----------------------------------------------------------------------------------------------------------
    //updateTask
    public void editTask(String taskIdT, String nameT, String descT) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put("taskName", nameT);
        content.put("description", descT);

        db.update(TABLE_TASK, content, " id= ?", new String[]{
                taskIdT});
    }

    //selectTask
    public ArrayList<Task> GetAllTasks(int userId) {
        ArrayList<Task> task = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TASK + " WHERE " + COLUMN_TASK_USER_ID + " = " + userId;

        Cursor resultSelect = db.rawQuery(query, null);
        resultSelect.moveToFirst();

        while (resultSelect.isAfterLast() == false) {
            task.add(new Task(
                    resultSelect.getInt(resultSelect.getColumnIndex(COLUMN_TASK_ID)),
                    resultSelect.getString(resultSelect.getColumnIndex(COLUMN_TASK_NAME)),
                    resultSelect.getString(resultSelect.getColumnIndex(COLUMN_TASK_DESCRIPTION)),
                    resultSelect.getInt(resultSelect.getColumnIndex(COLUMN_TASK_USER_ID))
            ));
            resultSelect.moveToNext();
        }
        return task;
    }

    public ArrayList<User> GetAllUsers() {
        ArrayList<User> user = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER ;
        Cursor resultSelect = db.rawQuery(query, null);
        resultSelect.moveToFirst();
        while (resultSelect.isAfterLast() == false) {
            user.add(new User(
                    resultSelect.getString(resultSelect.getColumnIndex(COLUMN_USERNAME)),
                    resultSelect.getString(resultSelect.getColumnIndex(COLUMN_USER_LOGIN)),
                    resultSelect.getString(resultSelect.getColumnIndex(COLUMN_USER_LOGOUT))
            ));
            resultSelect.moveToNext();
        }
        return user;
    }

    //deleteTask
    public void deleteTask(String taskId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TASK, "id=?", new String[]{
                taskId
        });
    }
    //Table User------------------------------------------------------------------------------------------------------------
    //userLogin
    public boolean userLogin(String username, String password) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = getReadableDatabase();
            cursor = db.rawQuery("select * from " + TABLE_USER +
                    " where " + COLUMN_USERNAME + " = '" + username + "' AND " +
                    COLUMN_PASSWORD + " = '" + password + "'", null);
            return cursor.moveToFirst();//This method will return false if the cursor is empty.

        } catch (SQLException e) {
            Log.i("LoginError", e.getMessage());
        } finally {
            if (db != null) {
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
            }
        }
        return false;
    }

    //select userId
    public int selectUserId(String username, String password) {
        int id;
        String[] values = {username, password};

        SQLiteDatabase db = getReadableDatabase();
        Cursor selectId = db.rawQuery("select id from user where username =? and password =?", values);
        selectId.moveToFirst();
        id = selectId.getInt(selectId.getColumnIndex(COLUMN_USER_ID));
        selectId.close();

        return id;
    }

    //select user by id
    public ArrayList <User> selectUserById(int userId){
        String fName,lName,username,password,gender;
        ArrayList<User> user = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER +" WHERE id = " + userId ;
        Cursor resultSelect = db.rawQuery(query, null);
        resultSelect.moveToFirst();
        while (resultSelect.isAfterLast() == false) {
          user.add( new User(
                    resultSelect.getString(resultSelect.getColumnIndex(COLUMN_USER_FNAME)),
                    resultSelect.getString(resultSelect.getColumnIndex(COLUMN_USER_LNAME)),
                    resultSelect.getString(resultSelect.getColumnIndex(COLUMN_USERNAME)),
                    resultSelect.getString(resultSelect.getColumnIndex(COLUMN_PASSWORD)),
                    resultSelect.getString(resultSelect.getColumnIndex(COLUMN_USER_GENDER))
          ));
            resultSelect.moveToNext();
        }
        return user;
    }

    //update user by id
    public void editUser(User user, int id){
        SQLiteDatabase db = getWritableDatabase();

        String userId = ""+id;

        ContentValues content = new ContentValues();
        content.put("fName", user.getFName());
        content.put("lName", user.getLName());
        content.put("username", user.getUsername());
        content.put("password", user.getPassword());
        content.put("gender", user.getGender());

        db.update(TABLE_USER, content, " id= ?", new String[]{
                userId});
    }

    //delete user by id
    public  void deleteUser(int id){
        SQLiteDatabase db = getWritableDatabase();
        String userId = ""+ id;
        db.delete(TABLE_USER, "id=?", new String[]{
                userId
        });
    }

    //on Login, insert login date time
    public void userLogin(int id){
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();
            db.execSQL("UPDATE user " +
                    "SET "+ COLUMN_USER_LOGIN+ " = DateTime('now','localtime')" +
                    "WHERE " +COLUMN_USER_ID+" == " + id);
        } catch (SQLException e) {
            Log.i("LogoutError", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    //on Logout insert logout date time
    public void userLogout(int id){
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();
            db.execSQL("UPDATE user " +
                    "SET "+ COLUMN_USER_LOGOUT+ " = DateTime('now','localtime')" +
                    "WHERE " +COLUMN_USER_ID+" == " + id);
        } catch (SQLException e) {
            Log.i("LogoutError", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

}

