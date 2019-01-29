package co.edu.udistrital.brainreactor;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;

import com.google.android.material.card.MaterialCardView;

public class AboutActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        MaterialCardView licenses = findViewById(R.id.licenses);

        licenses.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String url = null;

        switch (v.getId()) {
            case R.id.licenses:
                new AlertDialog.Builder(AboutActivity.this)
                .setTitle(R.string.exit_title)
                .setMessage(R.string.exit_message)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.github1:
                url = "https://github.com/NicolasRiveros";
                break;
            case R.id.fb1:
                url = "https://facebook.com/";
                break;
            case R.id.tw1:
                url = "https://twitter.com/";
                break;
            case R.id.github2:
                url = "https://github.com/AlexTorresSk";
                break;
            case R.id.fb2:
                url = "https://facebook.com/AlexTorresSk";
                break;
            case R.id.tw2:
                url = "https://twitter.com/AlexTorresSk";
                break;
        }

        if (url != null) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

}
