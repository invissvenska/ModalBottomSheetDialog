package nl.invissvenska.modalbottomsheetdialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ModalBottomSheetDialog extends BottomSheetDialogFragment {

    private static ModalBottomSheetDialog fragment;

    private static final String KEY_OPTIONS = "options";
    private static final String KEY_LAYOUT = "layout";
    private static final String KEY_HEADER = "header";
    private static final String KEY_HEADER_LAYOUT = "header_layout";

    private RecyclerView list;
    private Adapter adapter;
    private Listener listener;

    private ModalBottomSheetDialog() {

    }

    private static ModalBottomSheetDialog newInstance(Builder builder) {
        if (fragment == null) {
            fragment = new ModalBottomSheetDialog();
        }
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(KEY_OPTIONS, builder.options);
        arguments.putInt(KEY_LAYOUT, builder.layoutResource);
        arguments.putString(KEY_HEADER, builder.header);
        arguments.putInt(KEY_HEADER_LAYOUT, builder.headerLayoutResource);

        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = view.findViewById(R.id.list);
        Bundle arguments = getArguments();
        ArrayList<OptionHolder> optionHolders = arguments.getParcelableArrayList(KEY_OPTIONS);
        ArrayList<Option> options = new ArrayList<>();

        for (OptionHolder oh : optionHolders) {
            Integer resource = oh.getResource();
            OptionRequest optionRequest = oh.getOptionRequest();
            if (resource != null) {
                inflate(resource, options);
            }
            if (optionRequest != null) {
                options.add(optionRequest.toOption(getContext()));
            }
        }


        adapter = new Adapter(bindHost());
        adapter.setHeader(arguments.getString(KEY_HEADER));
        adapter.setHeaderLayoutResource(arguments.getInt(KEY_HEADER_LAYOUT));
        adapter.setLayoutRes(arguments.getInt(KEY_LAYOUT));
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setOptions(options);
    }

    @SuppressLint("RestrictedApi")
    private void inflate(Integer menuResource, ArrayList<Option> options) {
        MenuBuilder menu = new MenuBuilder(getContext());
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(menuResource, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            Option option = new Option(item.getItemId(), item.getTitle(), item.getIcon());
            options.add(option);
        }
    }

    private Listener bindHost() {
        if (getParentFragment() != null) {
            if (getParentFragment() instanceof Listener) {
                return (Listener) getParentFragment();
            }
        }
        if (getContext() instanceof Listener) {
            return (Listener) getContext();
        }
        throw new IllegalStateException("Listener mist");
    }

    public static class Builder {
        private ArrayList<OptionHolder> options = new ArrayList<>();
        private int layoutResource = R.layout.bottom_sheet_fragment_item;
        private String header = null;
        private int headerLayoutResource = R.layout.bottom_sheet_fragment_header;

        public Builder() {

        }

        public Builder setLayout(@LayoutRes int layoutResource) {
            this.layoutResource = layoutResource;
            return this;
        }

        public Builder add(@MenuRes int menuResource) {
            options.add(new OptionHolder(menuResource, null));
            return this;
        }

        public Builder setHeader(String header) {
            this.header = header;
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

    public interface Listener {
        void onModalOptionSelected(String tag, Option option);
    }

    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int VIEW_TYPE_HEADER = 0;
        private static final int VIEW_TYPE_ITEM = 1;

        private ArrayList<Option> options = new ArrayList<>();
        private int layoutResource = R.layout.bottom_sheet_fragment_item;
        private int headerLayoutResource = R.layout.bottom_sheet_fragment_header;
        private String header = null;
        private Listener listener;

        public Adapter(Listener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_HEADER) {
                View view = LayoutInflater.from(parent.getContext()).inflate(headerLayoutResource, parent, false);
                return new HeaderViewHolder(view);
            }
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext()).inflate(layoutResource, parent, false);
                final ItemViewHolder holder = new ItemViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = header != null ? holder.getAdapterPosition() - 1 : holder.getAdapterPosition();
                        Option option = options.get(position);
                        listener.onModalOptionSelected(getTag(), option);
                    }
                });
                return holder;
            }
            throw new IllegalStateException("Can't recognize this type");
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int correctedPosition = header == null ? position : position - 1;
            if (holder instanceof ItemViewHolder) {
                Option option = options.get(correctedPosition);
                ((ItemViewHolder) holder).bind(option);
            } else if (holder instanceof HeaderViewHolder) {
                ((HeaderViewHolder) holder).bind(header);
            }
        }

        @Override
        public int getItemCount() {
            return header == null ? options.size() : options.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (header != null) {
                if (position == 0) {
                    return VIEW_TYPE_HEADER;
                }
            }
            return VIEW_TYPE_ITEM;
        }

        public void setOptions(List<Option> options) {
            this.options.clear();
            this.options.addAll(options);
            notifyDataSetChanged();
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public void setLayoutRes(@LayoutRes int layoutResource) {
            this.layoutResource = layoutResource;
        }

        public void setHeaderLayoutResource(@LayoutRes int headerLayoutResource) {
            this.headerLayoutResource = headerLayoutResource;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView icon;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text1);
            icon = itemView.findViewById(R.id.icon);
        }

        public void bind(Option option) {
            text.setText(option.getTitle());
            icon.setImageDrawable(option.getIcon());
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text1);
        }

        public void bind(@Nullable String header) {
            text.setText(header);
        }
    }
}
