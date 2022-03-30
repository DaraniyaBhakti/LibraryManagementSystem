package com.tatvasoft.tatvasoftassignment4.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.tatvasoft.tatvasoftassignment4.Database.BookDatabase;
import com.tatvasoft.tatvasoftassignment4.R;
import com.tatvasoft.tatvasoftassignment4.Utils.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BookListFragment extends Fragment{

    private ArrayList<HashMap<String,String>> bookArrayList;
    private BookDatabase bookDatabase;
    private ListView listView;
    private EditText etBookSearch;
    private TextView tvNoBook;
    SimpleAdapter adapter;
    ArrayList<HashMap<String, String>> arrayType = new ArrayList<>();
    ArrayList<HashMap<String, String>> finalBookList = new ArrayList<>();
    public BookListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        androidx.appcompat.widget.Toolbar toolbar= view.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(requireActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(getString(R.string.book_list));

        setHasOptionsMenu(true);
        etBookSearch = view.findViewById(R.id.etBookSearch);
        listView = view.findViewById(R.id.listView);
        tvNoBook = view.findViewById(R.id.tvNoBook);
        bookArrayList = new ArrayList<>();

        bookDatabase = new BookDatabase(getContext());
        bookArrayList = bookDatabase.getBookList();
        finalBookList = bookArrayList;
        listClickEvent();
        setSearchView();


        return view;
    }

    private void setSearchView()
    {
        ArrayList<HashMap<String,String>> filteredList = new ArrayList<>();
        etBookSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                filteredList.clear();
                if(s.toString().length() > 0){
                    for(int i =0 ; i <bookArrayList.size() ; i++){
                        if(Objects.requireNonNull(bookArrayList.get(i).get(Constant.BOOK_NAME)).toLowerCase().contains(s.toString().toLowerCase())){
                            filteredList.add(bookArrayList.get(i));
                        }
                    }
                }
                if(filteredList.size() == 0 && s.toString().length() == 0){
                    filteredList.addAll(bookArrayList);
                }
                finalBookList = filteredList;
                listClickEvent();
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(etBookSearch.getText().toString().equals("")){
            finalBookList=bookArrayList;
            listClickEvent();
        }
    }

    public void listClickEvent()
    {

        adapter = new SimpleAdapter(getContext(), finalBookList,R.layout.book_list_row,new String[]{Constant.BOOK_NAME,Constant.AUTHOR_NAME},new int[]{R.id.tvBookName,R.id.tvAuthorName});
        listView.setAdapter(adapter);
        listView.setClickable(true);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            BookDetailFragment bookDetailFragment = new BookDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("BookId", Integer.parseInt(finalBookList.get(i).get(Constant.BOOK_ID)));
            bookDetailFragment.setArguments(bundle);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,bookDetailFragment)
                    .addToBackStack(null).commit();

        });
        if(finalBookList.isEmpty()) {
            tvNoBook.setVisibility(View.VISIBLE);
        }else {
            tvNoBook.setVisibility(View.GONE);
        }


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.sortByBook: {
                Collections.sort(finalBookList, new ComparatorByBook());
                adapter.notifyDataSetChanged();
                break;
            }
            case R.id.sortByDate: {
                Collections.sort(finalBookList, new ComparatorByDate());
                adapter.notifyDataSetChanged();
                break;
            }
            case R.id.filterByType:{
                filterByType();
                break;
            }
            case R.id.filterByAuthor:{
                filterByAuthor();
                break;
            }
            case R.id.filterByGenre:{
                filterByGenre();
                break;
            }

        }
        return super.onOptionsItemSelected(item);


    }


    public static class ComparatorByBook implements Comparator<Map<String,String>> {

        @Override
        public int compare(Map<String, String> book1, Map<String, String> book2) {
            return Objects.requireNonNull(book1.get(Constant.BOOK_NAME)).compareTo(Objects.requireNonNull(book2.get(Constant.BOOK_NAME)));

        }

    }
    public static class ComparatorByDate implements Comparator<Map<String,String>>{

        @Override
        public int compare(Map<String, String> book1, Map<String, String> book2) {
            return Objects.requireNonNull(book1.get(Constant.LAUNCH_DATE)).compareTo(Objects.requireNonNull(book2.get(Constant.LAUNCH_DATE)));
        }
    }

    private void filterByGenre() {

        String[] genreArray = bookDatabase.getGenreArray();

        boolean[] checkedItems = new boolean[genreArray.length];
        ArrayList<String> checkedGenre = new ArrayList<>();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireActivity());
        alertDialog.setTitle(R.string.selectGenre);
        alertDialog.setMultiChoiceItems(genreArray, checkedItems, (dialog, which, isChecked) -> {
            String genre = genreArray[which];
            if(checkedGenre.contains(genre))
                checkedGenre.remove(genre);
            else
                checkedGenre.add(genre);

        });

        alertDialog.setPositiveButton(R.string.dialog_filter, (dialogInterface, i) -> {

            ArrayList<HashMap<String, String>> bookByCheckedGenre;
            bookByCheckedGenre=bookDatabase.getBookByGenre(checkedGenre);
            finalBookList=bookByCheckedGenre;
            listClickEvent();
        });

        alertDialog.setNegativeButton(R.string.dialog_clear, (dialogInterface, i) -> {
            finalBookList = bookArrayList;
            listClickEvent();
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();

    }

    public void filterByAuthor()
    {

        String[] array = bookDatabase.getAuthorArray();
        boolean[] checkedItems = new boolean[array.length];
        ArrayList<String> checkedAuthor = new ArrayList<>();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setTitle(R.string.selectAuthor);
        alertDialog.setMultiChoiceItems(array, checkedItems, (dialog, which, isChecked) -> {
            String author = array[which];
            if(checkedAuthor.contains(author))
                checkedAuthor.remove(author);
            else
                checkedAuthor.add(author);

        });

        alertDialog.setPositiveButton(R.string.dialog_filter, (dialogInterface, i) -> {

            ArrayList<HashMap<String, String>> bookByCheckedAuthors;
            bookByCheckedAuthors = bookDatabase.getBookByAuthor(checkedAuthor);
            finalBookList=bookByCheckedAuthors;
            listClickEvent();
        });

        alertDialog.setNegativeButton(R.string.dialog_clear, (dialogInterface, i) -> {
            finalBookList=bookArrayList;
            listClickEvent();
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public void filterByType()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setTitle(R.string.SelectType);
        String[] items = {getString(R.string.rb_fiction),getString(R.string.rb_non_fiction)};
        boolean[] checkedItems = {false, false};
        alertDialog.setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
            switch (which) {
                case 0:
                    if (isChecked) {
                        arrayType = bookDatabase.getBookByBookType(getString(R.string.rb_fiction));
                    }
                    break;
                case 1:
                    if (isChecked) {
                        arrayType = bookDatabase.getBookByBookType(getString(R.string.rb_non_fiction));
                    }
                    break;
            }

        });
        alertDialog.setPositiveButton(R.string.dialog_filter, (dialogInterface, i) -> {
            finalBookList=arrayType;
            listClickEvent();

        });
        alertDialog.setNegativeButton(R.string.dialog_clear, (dialogInterface, i) -> {
            finalBookList=bookArrayList;
            listClickEvent();
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }


}