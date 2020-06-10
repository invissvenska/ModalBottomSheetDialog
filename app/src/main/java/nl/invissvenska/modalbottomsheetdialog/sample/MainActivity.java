package nl.invissvenska.modalbottomsheetdialog.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import nl.invissvenska.modalbottomsheetdialog.ModalBottomSheetDialog;
import nl.invissvenska.modalbottomsheetdialog.Item;

public class MainActivity extends AppCompatActivity implements ModalBottomSheetDialog.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.buttonWithHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModalBottomSheetDialog dialog = new ModalBottomSheetDialog.Builder()
                        .setHeader("Title of modal")
                        .add(R.menu.options)
                        .build();

                dialog.show(getSupportFragmentManager(), "WithHeader");
            }
        });

        findViewById(R.id.buttonWithoutHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModalBottomSheetDialog.Builder()
                        .add(R.menu.options)
                        .add(R.menu.options)
                        .show(getSupportFragmentManager(), "WithoutHeader");
            }
        });

        findViewById(R.id.buttonGrid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModalBottomSheetDialog.Builder()
                        .setHeader("Grid bottom layout")
                        .add(R.menu.lot_of_options)
                        .setColumns(3)
                        .show(getSupportFragmentManager(), "GridLayout");
            }
        });

        findViewById(R.id.buttonCustomLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModalBottomSheetDialog.Builder()
                        .setHeader("Custom title and item layouts")
                        .setHeaderLayout(R.layout.alternate_bottom_sheet_fragment_header)
                        .add(R.menu.lot_of_options)
                        .setItemLayout(R.layout.alternate_bottom_sheet_fragment_item)
                        .setColumns(3)
                        .show(getSupportFragmentManager(), "CustomHeader");
            }
        });

        findViewById(R.id.buttonScrollableList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModalBottomSheetDialog.Builder()
                        .setHeader("Scrolling layout")
                        .add(R.menu.lot_of_options)
                        .add(R.menu.lot_of_options)
                        .show(getSupportFragmentManager(), "ScrollLayout");
            }
        });
    }

    @Override
    public void onItemSelected(String tag, Item item) {
        Toast.makeText(getApplicationContext(), "Tag: " + tag + ", clicked on: " + item.getTitle(), Toast.LENGTH_SHORT).show();
    }
}