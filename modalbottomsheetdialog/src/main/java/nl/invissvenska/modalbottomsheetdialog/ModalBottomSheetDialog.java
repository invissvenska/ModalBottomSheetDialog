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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ModalBottomSheetDialog extends BottomSheetDialogFragment {

    private static ModalBottomSheetDialog fragment;

    private static final String KEY_OPTIONS = "options";
    private static final String KEY_ITEM_LAYOUT = "item_layout";
    private static final String KEY_HEADER = "header";
    private static final String KEY_HEADER_LAYOUT = "header_layout";
    private static final String KEY_COLUMNS = "columns";

    private ModalBottomSheetDialog() {
        // avoiding instantiation
    }

    private static ModalBottomSheetDialog newInstance(Builder builder) {
        if (fragment == null) {
            fragment = new ModalBottomSheetDialog();
        }
        Bundle arguments = new Bundle();
        arguments.putString(KEY_HEADER, builder.header);
        arguments.putInt(KEY_HEADER_LAYOUT, builder.headerLayoutResource);
        arguments.putParcelableArrayList(KEY_OPTIONS, builder.options);
        arguments.putInt(KEY_ITEM_LAYOUT, builder.itemLayoutResource);
        arguments.putInt(KEY_COLUMNS, builder.columns);

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
        RecyclerView list = view.findViewById(R.id.list);
        Bundle arguments = getArguments();
        assert arguments != null;
        List<OptionHolder> optionHolders = arguments.getParcelableArrayList(KEY_OPTIONS);
        List<Option> options = new ArrayList<>();

        for (OptionHolder oh : optionHolders != null ? optionHolders : new ArrayList<OptionHolder>()) {
            Integer resource = oh.getResource();
            OptionRequest optionRequest = oh.getOptionRequest();
            if (resource != null) {
                inflate(resource, options);
            }
            if (optionRequest != null) {
                options.add(optionRequest.toOption(getContext()));
            }
        }

        final Adapter adapter = new Adapter(bindHost());
        adapter.setHeader(arguments.getString(KEY_HEADER));
        adapter.setHeaderLayoutResource(arguments.getInt(KEY_HEADER_LAYOUT));
        adapter.setOptions(options);
        adapter.setItemLayoutRes(arguments.getInt(KEY_ITEM_LAYOUT));
        list.setAdapter(adapter);
        final int columns = arguments.getInt(KEY_COLUMNS);
        RecyclerView.LayoutManager layoutManager;
        if (columns > 1) {
            layoutManager = new GridLayoutManager(getContext(), columns);
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return adapter.header != null && position == 0 ? columns : 1;
                }
            });
        } else {
            layoutManager = new LinearLayoutManager(getContext());
        }
        list.setLayoutManager(layoutManager);
    }

    @SuppressLint("RestrictedApi")
    private void inflate(Integer menuResource, List<Option> options) {
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
        if (getParentFragment() != null && getParentFragment() instanceof Listener) {
            return (Listener) getParentFragment();
        }
        if (getContext() instanceof Listener) {
            return (Listener) getContext();
        }
        throw new IllegalStateException("Activity or Fragment need to implements ModalBottomSheetDialog.Listener");
    }

    public static class Builder {
        private String header = null;
        private int headerLayoutResource = R.layout.bottom_sheet_fragment_header;
        private ArrayList<OptionHolder> options = new ArrayList<>();
        private int itemLayoutResource = R.layout.bottom_sheet_fragment_item;
        private int columns = 1;

        public Builder() {
        }

        public Builder setHeader(String header) {
            this.header = header;
            return this;
        }

        public Builder setHeaderLayout(@LayoutRes int headerLayoutResource) {
            this.headerLayoutResource = headerLayoutResource;
            return this;
        }

        public Builder add(@MenuRes int menuResource) {
            options.add(new OptionHolder(menuResource));
            return this;
        }

        public Builder setItemLayout(@LayoutRes int itemLayoutResource) {
            this.itemLayoutResource = itemLayoutResource;
            return this;
        }

        public Builder setColumns(int columns) {
            this.columns = columns;
            return this;
        }

        public ModalBottomSheetDialog build() {
            return newInstance(this);
        }

        public void show(FragmentManager fragmentManager, String tag) {
            ModalBottomSheetDialog dialog = build();
            dialog.show(fragmentManager, tag);
        }
    }

    public interface Listener {
        void onOptionSelected(String tag, Option option);
    }

    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int VIEW_TYPE_HEADER = 0;
        private static final int VIEW_TYPE_ITEM = 1;

        private ArrayList<Option> options = new ArrayList<>();
        private int itemLayoutResource = R.layout.bottom_sheet_fragment_item;
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
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(headerLayoutResource, parent, false));
            }
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutResource, parent, false);
                final ItemViewHolder holder = new ItemViewHolder(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = header != null ? holder.getAdapterPosition() - 1 : holder.getAdapterPosition();
                        listener.onOptionSelected(getTag(), options.get(position));
                    }
                });
                return holder;
            }
            throw new IllegalStateException("Can't recognize this type");
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int correctPosition = header == null ? position : position - 1;
            if (holder instanceof ItemViewHolder) {
                Option option = options.get(correctPosition);
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

        public void setItemLayoutRes(@LayoutRes int itemLayoutResource) {
            this.itemLayoutResource = itemLayoutResource;
        }

        public void setHeaderLayoutResource(@LayoutRes int headerLayoutResource) {
            this.headerLayoutResource = headerLayoutResource;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView icon;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.icon);
            if (text == null) {
                throw new IllegalStateException("TextView in the Alternative item resource must have the id 'title'");
            }
            if (icon == null) {
                throw new IllegalStateException("ImageView in the Alternative item resource must have the id 'icon'");
            }
        }

        public void bind(Option option) {
            text.setText(option.getTitle());
            icon.setImageDrawable(option.getIcon());
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.header);
            if (text == null) {
                throw new IllegalStateException("TextView in the Alternative header resource must have the id 'header'");
            }
        }

        public void bind(@Nullable String header) {
            text.setText(header);
        }
    }
}
