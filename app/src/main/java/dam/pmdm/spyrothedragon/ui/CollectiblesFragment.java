package dam.pmdm.spyrothedragon.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dam.pmdm.spyrothedragon.ClickListener;
import dam.pmdm.spyrothedragon.R;
import dam.pmdm.spyrothedragon.VideoActivity;
import dam.pmdm.spyrothedragon.adapters.CollectiblesAdapter;
import dam.pmdm.spyrothedragon.databinding.FragmentCollectiblesBinding;
import dam.pmdm.spyrothedragon.models.Collectible;

public class CollectiblesFragment extends Fragment implements ClickListener {

    private FragmentCollectiblesBinding binding;
    private RecyclerView recyclerView;
    private CollectiblesAdapter adapter;
    private List<Collectible> collectiblesList;
    ReproductorVideo reproductor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCollectiblesBinding.inflate(inflater, container, false);
        recyclerView = binding.recyclerViewCollectibles;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        collectiblesList = new ArrayList<>();
        adapter = new CollectiblesAdapter(collectiblesList, this);
        recyclerView.setAdapter(adapter);
        reproductor = new ReproductorVideo();



        loadCollectibles();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadCollectibles() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.collectibles);

            // Crear un parser XML
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            Collectible currentCollectible = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = null;

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();

                        if ("collectible".equals(tagName)) {
                            currentCollectible = new Collectible();
                        } else if (currentCollectible != null) {
                            if ("name".equals(tagName)) {
                                currentCollectible.setName(parser.nextText());
                            } else if ("description".equals(tagName)) {
                                currentCollectible.setDescription(parser.nextText());
                            } else if ("image".equals(tagName)) {
                                currentCollectible.setImage(parser.nextText());
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        tagName = parser.getName();

                        if ("collectible".equals(tagName) && currentCollectible != null) {
                            collectiblesList.add(currentCollectible);
                        }
                        break;
                }

                eventType = parser.next();
            }

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(Long date) {

        reproductor.setLastTouche(date);

    }

    class ReproductorVideo{


        public ReproductorVideo() {
            touches = 0;
            lastTouche = new Date().getTime();
        }

        protected Long getLastTouche() {
            return lastTouche;
        }

        protected void setLastTouche(Long lastTouche) {
            if(touches == 0){
                touches = 1;
            }
            Long oldTouche = this.lastTouche;
            Log.d("TOUCHES", oldTouche+" "+ lastTouche);
            if(oldTouche + 1000 > lastTouche){
                touches++;
                Log.d("TOUCES", touches+"");
                if(touches == 4){
                    Log.d("REPRODUCIENDO", "");

                    reproducir();
                    touches = 0;
                }
            }
            else{
                touches = 0;
            }
            this.lastTouche = lastTouche;
        }

        private void reproducir(){

            Intent videoActivity = new Intent(requireActivity(), VideoActivity.class);
            String videoPath =   "android.resource://"+requireActivity().getPackageName()+
                    "/"+R.raw.video_pyro;
            videoActivity.putExtra("videoPath", videoPath);
            startActivity(videoActivity);

            binding.videoView.setVideoPath(
                    "android.resource://"+requireActivity().getPackageName()+
                            "/"+R.raw.video_pyro);
            }

//            binding.videoView.setVisibility(View.VISIBLE);
//            binding.videoView.bringToFront();
//            binding.videoView.start();
//            binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    binding.videoView.setVisibility(View.GONE);
//                }
//            });
        }

        private Long lastTouche;
        private int touches;

    }
}


