package com.tatvasoft.tatvasoftassignment4.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.tatvasoft.tatvasoftassignment4.Fragment.AddBookFragment;
import com.tatvasoft.tatvasoftassignment4.Fragment.BookListFragment;
import com.tatvasoft.tatvasoftassignment4.R;
import com.tatvasoft.tatvasoftassignment4.Utils.Constant;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.book));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigationView);
        drawerLayout= findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId())
            {
                case R.id.addBook:
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    AddBookFragment addBookFragment = new AddBookFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constant.ADD_ACTIVITY,true);
                    addBookFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().add(R.id.container,addBookFragment)
                            .addToBackStack(null).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case R.id.bookList:
                    FragmentManager fragmentManager1 = getSupportFragmentManager();
                    BookListFragment bookListFragment = new BookListFragment();
                    fragmentManager1.beginTransaction().add(R.id.container,bookListFragment)
                            .addToBackStack(null).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    break;

            }

            return true;
        });

    }


}