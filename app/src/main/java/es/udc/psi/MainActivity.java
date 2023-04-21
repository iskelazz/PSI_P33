package es.udc.psi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.myapp.fileprovider";
    private static final String USER_PREFS = "UserPrefs";
    private static final String USER_KEY = "user";
    private static final String USER_FILE = "user_file.txt";
    public static final String SHARED_PREFS_NAME = "UserPrefs";
    private EditText userEditText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEditText = findViewById(R.id.userEditText);
        Button saveButton = findViewById(R.id.saveButton);

        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String savedUser = sharedPreferences.getString(USER_KEY, "Usuario por defecto");
        userEditText.setText(savedUser);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userEditText.getText().toString();
                sharedPreferences.edit().putString(USER_KEY, user).apply();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String currentDate = sdf.format(new Date());
                String userLine = user + " - " + currentDate + "\n";
                saveUserToFile(userLine);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserFromPreferences();
    }

    private void saveUserToFile(String userLine) {
        File internalStorage = getFilesDir();
        File userFile = new File(internalStorage, USER_FILE);
        try (FileOutputStream fos = new FileOutputStream(userFile, true)) {
            fos.write(userLine.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share_file) {
            shareUserFile();
            return true;
        }
        if (id == R.id.open_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareUserFile() {
        File internalStorage = getFilesDir();
        File userFile = new File(internalStorage, USER_FILE);
        Uri userFileUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, userFile);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_STREAM, userFileUri);
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Compartir archivo de usuario"));
    }

    private void updateUserFromPreferences() {
        String savedUser = sharedPreferences.getString(USER_KEY, "Usuario por defecto");
        userEditText.setText(savedUser);
    }
}