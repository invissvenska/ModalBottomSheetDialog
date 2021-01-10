package nl.invissvenska.modalbottomsheetdialog.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import nl.invissvenska.modalbottomsheetdialog.Item;
import nl.invissvenska.modalbottomsheetdialog.ModalBottomSheetDialog;

public class ModalActivity extends AppCompatActivity implements ModalBottomSheetDialog.Listener {

    ModalBottomSheetDialog dismissibleDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.buttonWithHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissibleDialog = new ModalBottomSheetDialog.Builder()
                        .setHeader("Title of modal")
                        .add(R.menu.options)
                        .build();

                dismissibleDialog.show(getSupportFragmentManager(), "WithHeader");
            }
        });

        findViewById(R.id.buttonWithoutHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissibleDialog = new ModalBottomSheetDialog.Builder()
                        .add(R.menu.options)
                        .add(R.menu.options).build();
                dismissibleDialog.show(getSupportFragmentManager(), "WithoutHeader");
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
                        .add(R.menu.lot_of_options)
                        .show(getSupportFragmentManager(), "ScrollLayout");
            }
        });

        findViewById(R.id.buttonRounded).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModalBottomSheetDialog.Builder()
                        .setHeader("Rounded layout")
                        .setRoundedModal(true)
                        .add(R.menu.lot_of_options)
                        .show(getSupportFragmentManager(), "RoundedLayout");
            }
        });
    }

    @Override
    public void onItemSelected(String tag, Item item) {
        Toast.makeText(getApplicationContext(), "Tag: " + tag + ", clicked on: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        if (tag.equalsIgnoreCase("WithHeader") || tag.equalsIgnoreCase("WithoutHeader")) {
            dismissibleDialog.dismiss();
        }
    }
}