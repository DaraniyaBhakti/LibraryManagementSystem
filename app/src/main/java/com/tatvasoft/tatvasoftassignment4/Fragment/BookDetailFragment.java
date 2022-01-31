package com.tatvasoft.tatvasoftassignment4.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.tatvasoft.tatvasoftassignment4.Database.BookDatabase;
import com.tatvasoft.tatvasoftassignment4.R;
import com.tatvasoft.tatvasoftassignment4.Utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;

public class BookDetailFragment extends Fragment {
    ArrayList<HashMap<String,String>> bookList = new ArrayList<>();

    public BookDetailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);

        androidx.appcompat.widget.Toolbar toolbar= view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.book_details));

        setHasOptionsMenu(true);

        BookDatabase bookDatabase = new BookDatabase(getContext());

        int bookId = getArguments().getInt(Constant.BOOK_ID, 0);
        bookList = bookDatabase.getBookDetailById(bookId);

        TextView tvDetailBookName = view.findViewById(R.id.tvDetailBookName);
        TextView tvDetailAuthorName = view.findViewById(R.id.tvDetailAuthorName);
        TextView tvDetailBookType = view.findViewById(R.id.tvDetailBookType);
        TextView tvDetailBookGenre = view.findViewById(R.id.tvDetailBookGenre);
        TextView tvDetailLaunchDate = view.findViewById(R.id.tvDetailLaunchDate);
        TextView tvDetailAgeGroup = view.findViewById(R.id.tvDetailAgeGroup);

        tvDetailBookName.setText(bookList.get(0).get(Constant.BOOK_NAME));
        tvDetailAuthorName.setText(bookList.get(0).get(Constant.AUTHOR_NAME));
        tvDetailBookType.setText(bookList.get(0).get(Constant.BOOK_TYPE));
        tvDetailBookGenre.setText(bookList.get(0).get(Constant.BOOK_GENRE));
        tvDetailLaunchDate.setText(bookList.get(0).get(Constant.LAUNCH_DATE));
        tvDetailAgeGroup.setText(bookList.get(0).get(Constant.AGE_GROUP));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu_detail,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        AddBookFragment addBookFragment = new AddBookFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.UPDATE_BOOK_ID, Integer.parseInt(bookList.get(0).get(Constant.BOOK_ID)));
        bundle.putBoolean(Constant.ADD_ACTIVITY,false);
        addBookFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,addBookFragment)
                .addToBackStack(null).commit();

        return super.onOptionsItemSelected(item);
    }


}