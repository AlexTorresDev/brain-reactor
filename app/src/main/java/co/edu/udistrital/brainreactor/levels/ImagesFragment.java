package co.edu.udistrital.brainreactor.levels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import co.edu.udistrital.brainreactor.R;
import co.edu.udistrital.brainreactor.Thread;
import co.edu.udistrital.brainreactor.activities.GameActivity;

public class ImagesFragment extends Fragment implements Level, Runnable {

    private List<Image> IMAGES;
    private ImageView[] imagesView;
    private int mCont, localScore;
    private Thread thread;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images, container, false);

        initComponents(view);

        return view;
    }

    private void initComponents(View v) {
        localScore = 0;

        IMAGES = new ArrayList<>();
        listImages();

        mCont = 0;
        imagesView = new ImageView[6];

        imagesView[0] = v.findViewById(R.id.image1);
        imagesView[1] = v.findViewById(R.id.image2);
        imagesView[2] = v.findViewById(R.id.image3);
        imagesView[3] = v.findViewById(R.id.image4);
        imagesView[4] = v.findViewById(R.id.image5);
        imagesView[5] = v.findViewById(R.id.image6);

        thread = new Thread(this);
    }

    @Override
    public void onPause() {
        thread.stop();
        super.onPause();
    }

    @Override
    public void onResume() {
        thread.start();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        thread.stop();
        super.onDestroy();
    }

    @Override
    public void run() {
        try {
            while (!thread.isStopped()) {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mCont == 6) {
                            int container = new Random().nextInt(6);
                            int pos = new Random().nextInt(IMAGES.size());
                            imagesView[container].setImageResource(IMAGES.get(pos).getImage());
                            imagesView[container].setId(IMAGES.get(pos).getId());
                        }

                        do {
                            int container = new Random().nextInt(6);
                            int pos = new Random().nextInt(IMAGES.size());
                            if (imagesView[container].getDrawable() == null) {
                                imagesView[container].setImageResource(IMAGES.get(pos).getImage());
                                imagesView[container].setId(IMAGES.get(pos).getId());
                                mCont++;
                            }
                        } while (!emptyImagesView());
                    }
                });

                if (!thread.isPaused()) {
                    Thread.sleep(900);
                }
            }
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
    }

    private boolean emptyImagesView() {
        int i = 0;

        for (ImageView image : imagesView) {
            if (image.getDrawable() != null) i++;
        }

        return i == 6;
    }

    @Override
    public void touchPanel(CardView player, TextView text, TextView score) {
        boolean isCorrect = compare();

        int background = (isCorrect) ? R.color.colorSuccess : R.color.colorWrong;
        int message = (isCorrect) ? R.string.success : R.string.wrong;
        int s = Integer.parseInt(score.getText().toString());

        player.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), background));
        text.setText(message);

        if (isCorrect) {
            localScore++;
            s++;
        } else {
            s--;
        }

        score.setText(String.valueOf(s));

        if (localScore == 2) {
            ((GameActivity) Objects.requireNonNull(getActivity())).nextLevel();
        } else {
            resetContainers();
            mCont = 0;
        }
    }

    private void resetContainers() {
        for (ImageView image : imagesView) {
            image.setImageDrawable(null);
        }
    }

    @Override
    public int getMessage() {
        return R.string.images_message;
    }

    private boolean compare() {
        int cont = 0;

        for (int i = 0; i < imagesView.length; i++) {
            for (int j = i + 1; j < imagesView.length - 1; j++) {
                if (imagesView[i].getId() == imagesView[j].getId()) {
                    cont++;
                }

                if (cont == 2) break;
            }

            if (cont == 2) break;
        }

        return cont == 2;
    }

    private void listImages() {
        IMAGES.add(new Image(R.drawable.image_1, 1));
        IMAGES.add(new Image(R.drawable.image_2, 2));
        IMAGES.add(new Image(R.drawable.image_3, 3));
        IMAGES.add(new Image(R.drawable.image_4, 4));
        IMAGES.add(new Image(R.drawable.image_5, 5));
    }

    class Image {
        private int image;
        private int id;

        Image(int image, int id) {
            this.image = image;
            this.id = id;
        }

        public int getImage() {
            return this.image;
        }

        public int getId() {
            return this.id;
        }

    }
}
