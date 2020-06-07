package nl.invissvenska.modalbottomsheetdialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ModalBottomSheetDialog extends BottomSheetDialogFragment {

    private static ModalBottomSheetDialog fragment;

    private static final String KEY_OPTIONS = "options";
    private static final String KEY_HEADER = "header";
    private static final String KEY_HEADER_LAYOUT = "header_layout";

    private ModalBottomSheetDialog() {

    }

    private static ModalBottomSheetDialog newInstance(Builder builder) {
        if (fragment == null) {
            fragment = new ModalBottomSheetDialog();
        }
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(KEY_OPTIONS, builder.options);
        arguments.putString(KEY_HEADER, builder.header);
        arguments.putInt(KEY_HEADER_LAYOUT, builder.headerLayout);

        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_fragment, container, false);
    }

    public static class Builder {
        private ArrayList<OptionHolder> options = new ArrayList<>();
        private String header = null;
        private int headerLayout = R.layout.bottom_sheet_fragment_header;

        public Builder() {

        }

        public Builder add(@MenuRes int menuResource) {
            options.add(new OptionHolder(menuResource, null));
            return this;
        }

        public Builder setHeader(String header, @LayoutRes int headerLayout) {
            this.header = header;
            this.headerLayout = headerLayout;
            return this;
        }

        public ModalBottomSheetDialog build() {
            return newInstance(this);
        }

        public ModalBottomSheetDialog show(FragmentManager fragmentManager, String tag) {
            ModalBottomSheetDialog dialog = build();
            dialog.show(fragmentManager, tag);
            return dialog;
        }
    }

    interface Listener {
        void onModalOptionSelected(String tag, Option option);
    }
}
