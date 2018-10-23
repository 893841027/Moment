package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                try {
                    connection = new SQLMan().getConnection();
                    PreparedStatement statement = connection.prepareStatement("insert into CP_log (date) values(?)");
                    statement.setString(1,"解决");
                    statement.executeUpdate();
                } catch (final Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
