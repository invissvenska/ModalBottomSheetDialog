package nl.invissvenska.modalbottomsheetdialog.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import nl.invissvenska.modalbottomsheetdialog.Item;
import nl.invissvenska.modalbottomsheetdialog.ModalBottomSheetDialog;

public class ModalFragment extends Fragment implements ModalBottomSheetDialog.Listener {

    ModalBottomSheetDialog dismissibleDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.buttonWithHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissibleDialog = new ModalBottomSheetDialog.Builder()
                        .setHeader("Title of modal")
                        .add(R.menu.options)
                        .build();

                dismissibleDialog.show(getChildFragmentManager(), "WithHeader");
            }
        });

        view.findViewById(R.id.buttonWithoutHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissibleDialog = new ModalBottomSheetDialog.Builder()
                        .add(R.menu.options)
                        .add(R.menu.options).build();
                dismissibleDialog.show(getChildFragmentManager(), "WithoutHeader");
            }
        });

        view.findViewById(R.id.buttonGrid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModalBottomSheetDialog.Builder()
                        .setHeader("Grid bottom layout")
                        .add(R.menu.lot_of_options)
                        .setColumns(3)
                        .show(getChildFragmentManager(), "GridLayout");
            }
        });

        view.findViewById(R.id.buttonCustomLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModalBottomSheetDialog.Builder()
                        .setHeader("Custom title and item layouts")
                        .setHeaderLayout(R.layout.alternate_bottom_sheet_fragment_header)
                        .add(R.menu.lot_of_options)
                        .setItemLayout(R.layout.alternate_bottom_sheet_fragment_item)
                        .setColumns(3)
                        .show(getChildFragmentManager(), "CustomHeader");
            }
        });

        view.findViewById(R.id.buttonScrollableList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModalBottomSheetDialog.Builder()
                        .setHeader("Scrolling layout")
                        .add(R.menu.lot_of_options)
                        .add(R.menu.lot_of_options)
                        .show(getChildFragmentManager(), "ScrollLayout");
            }
        });

        view.findViewById(R.id.buttonRounded).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModalBottomSheetDialog.Builder()
                        .setHeader("Rounded layout")
                        .setRoundedModal(true)
                        .add(R.menu.lot_of_options)
                        .show(getChildFragmentManager(), "RoundedLayout");
            }
        });
    }

    @Override
    public void onItemSelected(String tag, Item item) {
        Toast.makeText(getContext(), "Tag: " + tag + ", clicked on: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        if (tag.equalsIgnoreCase("WithHeader") || tag.equalsIgnoreCase("WithoutHeader")) {
            dismissibleDialog.dismiss();
        }
    }

}
