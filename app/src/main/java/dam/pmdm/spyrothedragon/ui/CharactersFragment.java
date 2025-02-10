package dam.pmdm.spyrothedragon.ui;

import static android.view.View.VISIBLE;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dam.pmdm.spyrothedragon.ListenerClickCharacter;
import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.models.Character;
import dam.pmdm.spyrothedragon.adapters.CharactersAdapter;
import dam.pmdm.spyrothedragon.databinding.FragmentCharactersBinding;


public class CharactersFragment extends Fragment  implements ListenerClickCharacter {

    private FragmentCharactersBinding binding;

    private RecyclerView recyclerView;
    private CharactersAdapter adapter;
    private List<Character> charactersList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {

        binding = FragmentCharactersBinding.inflate(inflater, container, false);

        // Inicializamos el RecyclerView y el adaptador
        recyclerView = binding.recyclerViewCharacters;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        charactersList = new ArrayList<>();
        adapter = new CharactersAdapter(charactersList, this);
        recyclerView.setAdapter(adapter);

        // Cargamos los personajes desde el XML
        loadCharacters();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadCharacters() {
        try {
            // Cargamos el archivo XML desde res/xml (NOTA: ahora se usa R.xml.characters)
            InputStream inputStream = getResources().openRawResource(R.raw.characters);

            // Crear un parser XML
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            Character currentCharacter = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = null;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();

                        if ("character".equals(tagName)) {
                            currentCharacter = new Character();
                        } else if (currentCharacter != null) {
                            if ("name".equals(tagName)) {
                                currentCharacter.setName(parser.nextText());
                            } else if ("description".equals(tagName)) {
                                currentCharacter.setDescription(parser.nextText());
                            } else if ("image".equals(tagName)) {
                                currentCharacter.setImage(parser.nextText());
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        tagName = parser.getName();

                        if ("character".equals(tagName) && currentCharacter != null) {
                            charactersList.add(currentCharacter);
                        }
                        break;
                }

                eventType = parser.next();
            }

            adapter.notifyDataSetChanged(); // Notificamos al adaptador que los datos han cambiado
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        //creamos una vista que dibuja una llama saliendo de spyro y la pasamos la vista
        //sobre la que se ha clickado para hayar las cocordenadas de la pantalla en la que se
        //encuentra la vista
        FireBreathView fireBreathView = new FireBreathView(requireContext(), view);
        fireBreathView.setClickable(true); //hacemos que la vista sea clickable convirtiéndola así
        // en un modal e imposibilitando que se pueda interactuar con la app mientras la animación se realiza
        binding.getRoot().addView(fireBreathView);










//        ImageView iv =(ImageView) view;
//        Log.d("GETX",iv.getX()+"");
//        Log.d("GETy",iv.getY()+"");
//        Drawable fire = ResourcesCompat.getDrawable(getResources(), R.drawable.fire, null);
//        Log.d("MEASURES", fire.getIntrinsicWidth()+" "+fire.getIntrinsicHeight());
//        Drawable spyrod = ResourcesCompat.getDrawable(getResources(), R.drawable.spyro, null);
//        Bitmap bm = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bm);
//        spyrod.setBounds(0, 0, view.getWidth(), view.getHeight());
//        spyrod.draw(canvas);
//        fire.setBounds(0, view.getHeight()/2+30, view.getWidth(), view.getHeight()*2);
//        fire.draw(canvas);
//        iv.setImageBitmap(bm);

//        iv.setForeground(fire);

//        Bitmap firebm = BitmapFactory.decodeResource(getResources(), R.drawable.fire1);
//        Bitmap spyro = BitmapFactory.decodeResource(getResources(), R.drawable.spyro);
//        fire.draw(canvas);
//        iv.draw(canvas);
//        iv.invalidate();
//        canvas.drawBitmap(firebm, 0, 0, new Paint());
//        iv.setImageBitmap(spyro);


//        fire.draw(canvas);
//        iv.setImageBitmap(spyro);
//        fire.setBounds(100, 100, 120, 120);

//        view.setBackgroundResource(R.drawable.fire);
//        view.setForeground(fire);
//        AnimationDrawable a = (AnimationDrawable) view.getForeground();
//        a.start();
//        ValueAnimator animator = ValueAnimator.ofFloat(  0, 1000);
//        animator.setDuration(10000);
//        animator.addUpdateListener(ani-> fire.setBounds(10, (int)(10 + ani.getAnimatedFraction()), (int) (view.getWidth()+ ani.getAnimatedFraction()), (int)(view.getHeight()+ani.getAnimatedFraction())));
//        animator.setRepeatCount(5);
//        animator.start();


//
//        Bitmap imageBitmap = Bitmap.createBitmap(view.getWidth(),
//                                        view.getHeight(), Bitmap.Config.ARGB_8888);
//                    Canvas canvas = new Canvas(imageBitmap);
//                        Paint p = new Paint();
//       Drawable spyro = ResourcesCompat.getDrawable(getResources(), R.drawable.spyro, null);
//       Drawable fire = ResourcesCompat.getDrawable(getResources(), R.drawable.fire, null);
//       spyro.setBounds(0,0, view.getWidth(), view.getHeight());
//       fire.setBounds(50, 50, view.getWidth(), view.getHeight());
//       spyro.draw(canvas);
//       fire.draw(canvas);
//                         ((ImageView) view).setImageBitmap(imageBitmap);
//        View v = binding.viewBreath;
//        try {
////            v.setAlpha(1);
//        }
//        catch (NullPointerException e){
//            System.out.println(e.getMessage());
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//        v.invalidate();
    }
}