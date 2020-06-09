package nl.invissvenska.modalbottomsheetdialog.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import nl.invissvenska.modalbottomsheetdialog.ModalBottomSheetDialog;
import nl.invissvenska.modalbottomsheetdialog.Option;

public class MainActivity extends AppCompatActivity implements ModalBottomSheetDialog.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModalBottomSheetDialog.Builder()
                        .setHeader("Title of modal")
                        .add(R.menu.options)
                        .show(getSupportFragmentManager(), "WithHeader");
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModalBottomSheetDialog.Builder()
                        .add(R.menu.options)
                        .add(R.menu.options)
                        .show(getSupportFragmentManager(), "WithoutHeader");
            }
        });
    }

    @Override
    public void onModalOptionSelected(String tag, Option option) {
        Toast.makeText(getApplicationContext(), "Tag: " + tag + ", clicked on: " + option.getTitle(), Toast.LENGTH_SHORT).show();
    }
}