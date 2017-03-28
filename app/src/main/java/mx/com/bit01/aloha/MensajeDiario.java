package mx.com.bit01.aloha;

        import android.media.MediaPlayer;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.ImageView;

public class MensajeDiario extends AppCompatActivity implements  Animation.AnimationListener{

    private Animation animation1;
    private Animation animation2;
    private boolean isBackOfCardShowing = false;
    private boolean animando =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje_diario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int unicode = 0x1F33A;
        String emoji = getEmojiByUnicode(unicode);
        setTitle("Aloha "+emoji);

        findViewById(R.id.textViewAfAhora).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewMensaje).setVisibility(View.INVISIBLE);

        View someView = findViewById(R.id.idFotoMensajeDiario);
        View root = someView.getRootView();
        root.setBackgroundColor(getResources().getColor(R.color.colorBackgroundPortada));

        animation1 = AnimationUtils.loadAnimation(this, R.anim.to_middle);
        animation1.setAnimationListener(this);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.from_middle);
        animation2.setAnimationListener(this);

        ImageView mensaje = (ImageView) findViewById(R.id.idFotoMensajeDiario);
        mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!animando){

                    findViewById(R.id.mensajeDiarioLayout).clearAnimation();
                    findViewById(R.id.mensajeDiarioLayout).setAnimation(animation1);
                    findViewById(R.id.mensajeDiarioLayout).startAnimation(animation1);


                }

            }
        });




    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    @Override
    public void onAnimationStart(Animation animation) {
        animando=true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (animation==animation1) {

            if (isBackOfCardShowing) {

                ((ImageView)findViewById(R.id.idFotoMensajeDiario)).setImageResource(R.drawable.m1);
                findViewById(R.id.textViewMensaje).setVisibility(View.INVISIBLE);
                findViewById(R.id.textViewAfAhora).setVisibility(View.VISIBLE);

            } else {

                ((ImageView)findViewById(R.id.idFotoMensajeDiario)).setImageResource(R.drawable.texto);
                findViewById(R.id.textViewMensaje).setVisibility(View.VISIBLE);
                findViewById(R.id.textViewAfAhora).setVisibility(View.INVISIBLE);

            }

            findViewById(R.id.mensajeDiarioLayout).clearAnimation();
            findViewById(R.id.mensajeDiarioLayout).setAnimation(animation2);
            findViewById(R.id.mensajeDiarioLayout).startAnimation(animation2);

        } else {

            isBackOfCardShowing=!isBackOfCardShowing;
            animando=false;

        }

    }

    @Override
    protected void onDestroy(){

        super.onDestroy();
    }

    @Override
    protected void onPause(){

        super.onPause();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
