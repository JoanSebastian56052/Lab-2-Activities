package gr11_2017lab2.compumovil.udea.edu.co.lab2activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //declaracion de variables globes
    DBHelper dbHelper;
    SQLiteDatabase db;
    Fragment about = new AboutFragment();
    Fragment eventos = new EventList();
    AddEventFragment add = new AddEventFragment();
    Fragment info = new InformationFragment();
    FragmentTransaction manager = getSupportFragmentManager().beginTransaction();
    FloatingActionButton fab;
    ///////////////

    private boolean controlSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        dbHelper = new DBHelper(this);//nueva base de datos
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);
        manager.replace(R.id.fragment_container, about);
        manager.commit();
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        manager = getSupportFragmentManager().beginTransaction();
        eventos = new EventList();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!controlSelect) {
                moveTaskToBack(true);
            } else {
                manager.replace(R.id.fragment_container, eventos);
                manager.commit();
                controlSelect = false;
                fab.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_navigation_drawer_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//opcion cerrar sesion y guarda datos en la BD
        int id = item.getItemId();
        if (id == R.id.exit) {
            db = dbHelper.getWritableDatabase();
            db.execSQL("delete from " + StatusContract.TABLE_LOGIN);
            db.close();
            Intent newActivity = new Intent(this, LoginActivity.class);
            startActivity(newActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        manager = getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();
        if (id == R.id.profile) {
            info = new InformationFragment();
            manager.replace(R.id.fragment_container, info);
            fab.setVisibility(View.INVISIBLE);
        } else if (id == R.id.events) {
            eventos = new EventList();
            manager.replace(R.id.fragment_container, eventos);
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.about) {
            about = new AboutFragment();
            manager.replace(R.id.fragment_container, about);
            fab.setVisibility(View.INVISIBLE);
        } else if (id == R.id.exit) {
            db = dbHelper.getWritableDatabase();
            db.execSQL("delete from " + StatusContract.TABLE_LOGIN);
            db.close();
            Intent newActivity = new Intent(this, LoginActivity.class);
            startActivity(newActivity);
            return true;
        }
        manager.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void AgregarLugar(View v) {
        manager = getSupportFragmentManager().beginTransaction();
        add = new AddEventFragment();
        manager.replace(R.id.fragment_container, add);
        manager.commit();
        fab.setVisibility(View.INVISIBLE);
        controlSelect = true;
    }

    public void GClic(View v) {
        add.ClickGalleryR();
    }

    public void CClic(View v) {
        add.ClickCameraR();
    }

    public void OtroLugar(View v) {
        add.ValidarEventos();
        manager = getSupportFragmentManager().beginTransaction();
        eventos = new EventList();
        manager.replace(R.id.fragment_container, eventos);
        manager.commit();
        fab.setVisibility(View.VISIBLE);
    }
}
