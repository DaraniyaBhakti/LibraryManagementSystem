package com.tatvasoft.tatvasoftassignment4.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.tatvasoft.tatvasoftassignment4.Database.BookDatabase;
import com.tatvasoft.tatvasoftassignment4.R;
import com.tatvasoft.tatvasoftassignment4.Utils.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class AddBookFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private EditText etBookName, etAuthorName;
    private TextView tvLaunchDate;
    private RadioButton rbFiction, rbNonFiction;
    private CheckBox cbBelow12,cbAbove12,cbAbove18,cbAny;
    private Spinner spinnerGenre;
    private ImageView imgEditDate;
    private Button saveButton;
    private BookDatabase bookDatabase;
    private int updateBookId;
    ArrayList<HashMap<String,String>> bookList = new ArrayList<>();
    ArrayAdapter<CharSequence> adapterSpinner;
    private static Resources resources;
    boolean add=true;

    public AddBookFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_book, container, false);

        androidx.appcompat.widget.Toolbar toolbar= view.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(requireActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(getString(R.string.add_book));

        resources = getResources();

        bindId(view);
        setSpinner();
        clickToSetDate();
        setSpinner();

        assert getArguments() != null;
        if(!getArguments().getBoolean(Constant.ADD_ACTIVITY))
        {
            getBookData();
        }
        onSaveButtonClick();
        return view;
    }
    public static Resources getRes(){return resources;}
    public void bindId(View view)
    {
        etBookName = view.findViewById(R.id.etBookName);
        etAuthorName  = view.findViewById(R.id.etAuthorName);
        tvLaunchDate = view. findViewById(R.id.tvLaunchDate);
        imgEditDate = view.findViewById(R.id.imgEditDate);
        rbFiction=view.findViewById(R.id.rbFiction);
        rbNonFiction=view.findViewById(R.id.rbNonFiction);
        spinnerGenre =view.findViewById(R.id.spinnerGenre);
        cbBelow12 = view.findViewById(R.id.cbBelow12);
        cbAbove12 = view.findViewById(R.id.cbAbove12);
        cbAbove18  = view.findViewById(R.id.cbAbove18);
        cbAny = view.findViewById(R.id.cbAny);
        saveButton =view.findViewById(R.id.saveButton);

        bookDatabase = new BookDatabase(getContext());

    }

    public void clickToSetDate()
    {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,day);

            String dateString = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(calendar.getTime());
            tvLaunchDate.setText(dateString);
        };

        tvLaunchDate.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }
    public void setSpinner()
    {
        spinnerGenre.setOnItemSelectedListener(this);
        spinnerGenre.setPrompt(getString(R.string.selectGenre));

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapterSpinner = ArrayAdapter.createFromResource(getActivity(),
                R.array.genre_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapterSpinner to the spinner
        spinnerGenre.setAdapter(adapterSpinner);
    }

    public String getAgeGroup(){
        String selectedAgeGroup = "";
        if (cbBelow12.isChecked()) {
            selectedAgeGroup += ", " + cbBelow12.getText().toString();
        }
        if (cbAbove12.isChecked()) {
            selectedAgeGroup += ", " + cbAbove12.getText().toString();
        }
        if (cbAbove18.isChecked()) {
            selectedAgeGroup += ", " + cbAbove18.getText().toString();
        }
        if (cbAny.isChecked()) {
            selectedAgeGroup += ", " + cbAny.getText().toString();
        }

        return selectedAgeGroup.substring(1);
    }

    public boolean isValidData()
    {
        boolean isValid = true;

        if(TextUtils.isEmpty(etBookName.getText().toString()))
        {
            isValid=false;
            etBookName.setError(getString(R.string.err_bookName));
        }
        if(TextUtils.isEmpty(etAuthorName.getText().toString()))
        {
            isValid=false;
            etAuthorName.setError(getString(R.string.err_authorName));
        }
        if((!rbFiction.isChecked()) && (!rbNonFiction.isChecked()))
        {
            isValid=false;
            Toast.makeText(getActivity(),getString( R.string.err_bookType),Toast.LENGTH_SHORT).show();
        }

        if(!cbBelow12.isChecked()){
            if(!cbAbove12.isChecked()){
                if(!cbAbove18.isChecked()){
                    if(!cbAny.isChecked()){
                        Toast.makeText(getActivity(), getString(R.string.err_ageGroup),Toast.LENGTH_SHORT).show();
                        isValid = false;
                    }
                }
            }
        }
        return isValid;
    }
    public void onSaveButtonClick()
    {

        saveButton.setOnClickListener(view -> {
            if(isValidData())
            {
                if(add) {
                    bookDatabase.addBook(etBookName.getText().toString(),
                            etAuthorName.getText().toString(),
                            rbFiction.isChecked() ? getString(R.string.rb_fiction) : getString(R.string.rb_non_fiction),
                            adapterSpinner.getItem(spinnerGenre.getSelectedItemPosition()).toString(),
                            tvLaunchDate.getText().toString(),
                            getAgeGroup()
                    );

                    Toast.makeText(getContext(),getString( R.string.toast_bookAdded),Toast.LENGTH_SHORT).show();


                    BookListFragment bookListFragment = new BookListFragment();
                    requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,bookListFragment).commit();

                }
                else {
                    bookDatabase.updateBookById(updateBookId,
                            etBookName.getText().toString(),
                            etAuthorName.getText().toString(),
                            rbFiction.isChecked() ? getString(R.string.rb_fiction) : getString(R.string.rb_non_fiction),
                            adapterSpinner.getItem(spinnerGenre.getSelectedItemPosition()).toString(),
                            tvLaunchDate.getText().toString(),
                            getAgeGroup());
                    Toast.makeText(getContext(), getString(R.string.toast_updateBook),Toast.LENGTH_SHORT).show();

                    BookDetailFragment bookDetailFragment = new BookDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("BookId", updateBookId);
                    bookDetailFragment.setArguments(bundle);
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,bookDetailFragment)
                            .addToBackStack(null).commit();
               }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

     public void getBookData(){
        add=false;
        assert getArguments() != null;
        updateBookId = getArguments().getInt(Constant.UPDATE_BOOK_ID);

        bookList = bookDatabase.getBookDetailById(updateBookId);

        etBookName.setText(bookList.get(0).get(Constant.BOOK_NAME));
        etAuthorName.setText(bookList.get(0).get(Constant.AUTHOR_NAME));

        imgEditDate.setVisibility(View.VISIBLE);
        tvLaunchDate.setText(bookList.get(0).get(Constant.LAUNCH_DATE));

         @SuppressLint("SimpleDateFormat")
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

         Date date = new Date();
         try {
             date = simpleDateFormat.parse(tvLaunchDate.getText().toString());
             assert date != null;
             Log.d("date",simpleDateFormat.format(date));
         } catch (ParseException e) {
             e.printStackTrace();
         }

         int day1 = Integer.parseInt((String) android.text.format.DateFormat.format("dd",date));
         int month1 = Integer.parseInt((String) android.text.format.DateFormat.format("MM",date));
         int year1 = Integer.parseInt((String) android.text.format.DateFormat.format("yyyy",date));

         Calendar calendar = Calendar.getInstance();
         DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
             calendar.set(Calendar.YEAR, year);
             calendar.set(Calendar.MONTH, month);
             calendar.set(Calendar.DAY_OF_MONTH, day);

             String dateString = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(calendar.getTime());
             tvLaunchDate.setText(dateString);
         };

         tvLaunchDate.setOnClickListener(view -> {
             DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                     dateSetListener,
                     calendar.get(Calendar.YEAR),
                     calendar.get(Calendar.MONTH),
                     calendar.get(Calendar.DAY_OF_MONTH));
             datePickerDialog.updateDate(year1,month1-1,day1);
             datePickerDialog.show();
         });


         String bookType = bookList.get(0).get(Constant.BOOK_TYPE);
        assert bookType != null;
        if (bookType.equalsIgnoreCase(getString(R.string.rb_fiction))) {
            rbFiction.setChecked(true);
        } else {
            rbNonFiction.setChecked(true);
        }

        String ageGroup = bookList.get(0).get(Constant.AGE_GROUP);
        assert ageGroup != null;
        if(ageGroup.contains(cbBelow12.getText().toString())){
            cbBelow12.setChecked(true);
        }
        if(ageGroup.contains(cbAbove12.getText().toString())){
            cbAbove12.setChecked(true);
        }
        if(ageGroup.contains(cbAbove18.getText().toString())){
            cbAbove18.setChecked(true);
        }
        if(ageGroup.contains(cbAny.getText().toString())){
            cbAny.setChecked(true);
        }

        String genre = bookList.get(0).get(Constant.BOOK_GENRE);

        if(genre!=null)
        {
            int spinnerPosition = adapterSpinner.getPosition(genre);
            spinnerGenre.setSelection(spinnerPosition);
        }


        saveButton.setText(R.string.edit);

    }


}