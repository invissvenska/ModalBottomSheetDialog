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

    private static final String KEY_ITEMS = "items";
    private static final String KEY_ITEM_LAYOUT = "item_layout";
    private static final String KEY_HEADER = "header";
    private static final String KEY_HEADER_LAYOUT = "header_layout";
    private static final String KEY_COLUMNS = "columns";
    private static boolean roundedModal = false;

    private static ModalBottomSheetDialog newInstance(Builder builder) {
        if (fragment == null) {
            fragment = new ModalBottomSheetDialog();
        }
        roundedModal = builder.roundedModal;

        Bundle arguments = new Bundle();
        arguments.putString(KEY_HEADER, builder.header);
        arguments.putInt(KEY_HEADER_LAYOUT, builder.headerLayoutResource);
        arguments.putParcelableArrayList(KEY_ITEMS, builder.items);
        arguments.putInt(KEY_ITEM_LAYOUT, builder.itemLayoutResource);
        arguments.putInt(KEY_COLUMNS, builder.columns);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int getTheme() {
        return roundedModal ? R.style.BottomSheetDialogTheme : super.getTheme();
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
        List<ItemHolder> itemHolders = arguments.getParcelableArrayList(KEY_ITEMS);
        List<Item> items = new ArrayList<>();

        for (ItemHolder holder : itemHolders != null ? itemHolders : new ArrayList<ItemHolder>()) {
            Integer resource = holder.getResource();
            ItemRequest itemRequest = holder.getItemRequest();
            if (resource != null) {
                inflate(resource, items);
            }
            if (itemRequest != null) {
                items.add(itemRequest.toItem(getContext()));
            }
        }

        final Adapter adapter = new Adapter(bindHost());
        adapter.setHeader(arguments.getString(KEY_HEADER));
        adapter.setHeaderLayoutResource(arguments.getInt(KEY_HEADER_LAYOUT));
        adapter.setItems(items);
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
    private void inflate(Integer menuResource, List<Item> items) {
        MenuBuilder menu = new MenuBuilder(getContext());
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(menuResource, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            Item item = new Item(menuItem.getItemId(), menuItem.getTitle(), menuItem.getIcon());
            items.add(item);
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
        private ArrayList<ItemHolder> items = new ArrayList<>();
        private int itemLayoutResource = R.layout.bottom_sheet_fragment_item;
        private int columns = 1;
        private boolean roundedModal = false;

        public Builder setHeader(String header) {
            this.header = header;
            return this;
        }

        public Builder setHeaderLayout(@LayoutRes int headerLayoutResource) {
            this.headerLayoutResource = headerLayoutResource;
            return this;
        }

        public Builder add(@MenuRes int menuResource) {
            items.add(new ItemHolder(menuResource));
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

        public Builder setRoundedModal(boolean roundedModal) {
            this.roundedModal = roundedModal;
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
        void onItemSelected(String tag, Item item);
    }

    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int VIEW_TYPE_HEADER = 0;
        private static final int VIEW_TYPE_ITEM = 1;

        private ArrayList<Item> items = new ArrayList<>();
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
                        listener.onItemSelected(getTag(), items.get(position));
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
                Item item = items.get(correctPosition);
                ((ItemViewHolder) holder).bind(item);
            } else if (holder instanceof HeaderViewHolder) {
                ((HeaderViewHolder) holder).bind(header);
            }
        }

        @Override
        public int getItemCount() {
            return header == null ? items.size() : items.size() + 1;
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

        public void setItems(List<Item> items) {
            this.items.clear();
            this.items.addAll(items);
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
            if (text == null && icon == null) {
                throw new IllegalStateException("At least define a TextView with id 'title' or an ImageView with id 'icon' in the item resource");
            }
        }

        public void bind(Item item) {
            if (text != null) {
                text.setText(item.getTitle());
            }
            if (icon != null) {
                icon.setImageDrawable(item.getIcon());
            }
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
