package mx.com.bit01.aloha;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private boolean mIsBound = false;
    private MusicService mServ ;
    private boolean misPlaying=false, yaPaso=false;
    private boolean continuarMusica = false;
    private boolean habilitarMusica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int unicode = 0x1F33A;
        String emoji = getEmojiByUnicode(unicode);
        setTitle("Aloha "+emoji);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        habilitarMusica = prefs.getBoolean("musicSwitch",true);
        onSharedPreferenceChanged(prefs, "musicSwitch");

    }

    void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key){
        if(key.equals("musicSwitch")){
            if(sharedPreferences.getBoolean("musicSwitch", true)){
                Toast.makeText(this, "Switch True", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Switch False", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        continuarMusica=false;
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        doBindService();
        startService(music);
        misPlaying = true;
        yaPaso=true;
    }

    private ServiceConnection Scon = new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class), Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            continuarMusica = true;
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Instrucciones) {
            // Handle the camera action
        } else if (id == R.id.nav_AfirmacionAhora) {
            Intent intent = new Intent(this, MensajeDiario.class);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            continuarMusica=true;
            startActivity(intent);

        } else if (id == R.id.nav_AfirmacionOro) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        super.onPause();
        /*if (misPlaying) {
            //mServ.pauseMusic();
            misPlaying = true;
        }*/
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        super.onResume();
        SharedPreferences prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        onSharedPreferenceChanged(prefs, "musicSwitch");
        if (!misPlaying && yaPaso) {
            mServ.resumeMusic();
            misPlaying = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service

        if (misPlaying && !continuarMusica) {
            mServ.pauseMusic();
            misPlaying = false;
        }
        //doUnbindService();
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();

        if (misPlaying) {
            mServ.pauseMusic();
            misPlaying = false;
            //stopService(music);
        }
        doUnbindService();
        stopService(new Intent(this, MusicService.class));
    }

}
