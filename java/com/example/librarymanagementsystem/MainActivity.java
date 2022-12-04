package com.example.librarymanagementsystem;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class BookListFragment extends Fragment {

    RecyclerView mRecyclerView;
    BookListFragment.MyAdapter mMyAdapter;
    List<Book> mBookList = new ArrayList<Book>();
    public static final int MENU_ID_ADD = 1;
    public static final int MENU_ID_UPDATE = 2;
    public static final int MENU_ID_DELETE = 3;

    private ActivityResultLauncher<Intent> addDataLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
        if(null!=result){
            Intent intent=result.getData();
            if(result.getResultCode()==InputBookListActivity.RESULT_CODE){
                Bundle bundle=intent.getExtras();
                String name=bundle.getString("name");
                int position=bundle.getInt("position");
                mBookList.add(position,new Book(name,R.drawable.book_1));
                mMyAdapter.notifyItemInserted(position);
            }
        }
    });

    private ActivityResultLauncher<Intent> updateDataLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
        if(null!=result){
            Intent intent=result.getData();
            if(result.getResultCode()==InputBookListActivity.RESULT_CODE){
                Bundle bundle=intent.getExtras();
                String name=bundle.getString("name");
                int position=bundle.getInt("position");
                mBookList.get(position).setTitle(name);
                mMyAdapter.notifyItemChanged(position);
            }
        }
    });

    private void setTitle(String name) {
    }


    public BookListFragment() {
        // Required empty public constructor
    }

    public static BookListFragment newInstance() {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_book_list, container, false);
        mRecyclerView=rootView.findViewById(R.id.recyclerview);

        Book b0=new Book("软件项目管理案例教程（第4版）", R.drawable.book_2);
        Book b1=new Book("创新工程实践", R.drawable.book_no_name);
        Book b2=new Book("信息安全数学基础（第2版）", R.drawable.book_1);
        mBookList.add(b0);
        mBookList.add(b1);
        mBookList.add(b2);

        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        return rootView;
    }
    public List<Book> getListBooks(){
        return mBookList;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch(item.getItemId()){
            case 1:
                Intent intent = new Intent(this.getContext(), InputBookListActivity.class);
                intent.putExtra("position",item.getOrder());
                addDataLauncher.launch(intent);
                break;
            case 2:
                Intent intentUpdate=new Intent(this.getContext(), InputBookListActivity.class);
                intentUpdate.putExtra("position",item.getOrder());
                intentUpdate.putExtra("name",mBookList.get(item.getOrder()).getTitle());
                updateDataLauncher.launch(intentUpdate);
                break;
            case 3:
                AlertDialog alertDialog = new AlertDialog.Builder(this.getContext())
                        .setTitle(R.string.conformation)
                        .setMessage(R.string.sure_to_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mBookList.remove(item.getOrder());
                                mMyAdapter.notifyItemRemoved(item.getOrder());
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();
                alertDialog.show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {

        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(BookListFragment.this.getContext(), R.layout.item_list, null);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            Book book = mBookList.get(position);
            holder.mImageView.setImageDrawable(getResources().getDrawable(book.coverresourceid));
            holder.mTextView.setText(book.title);
        }

        @Override
        public int getItemCount() {
            return mBookList.size();
        }
    }

    class MyViewHoder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        ImageView mImageView;
        TextView mTextView;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.textView);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo){
            contextMenu.add(0,MENU_ID_ADD,getAdapterPosition(),"Add "+getAdapterPosition());
            contextMenu.add(0,MENU_ID_UPDATE,getAdapterPosition(),"Update "+getAdapterPosition());
            contextMenu.add(0,MENU_ID_DELETE,getAdapterPosition(),"Delete "+getAdapterPosition());
        }
    }
}