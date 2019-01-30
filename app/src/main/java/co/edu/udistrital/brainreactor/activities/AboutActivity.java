package co.edu.udistrital.brainreactor.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import co.edu.udistrital.brainreactor.R;
import de.psdev.licensesdialog.LicensesDialog;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.licenses).setOnClickListener(this);
        findViewById(R.id.github1).setOnClickListener(this);
        findViewById(R.id.fb1).setOnClickListener(this);
        findViewById(R.id.tw1).setOnClickListener(this);
        findViewById(R.id.github2).setOnClickListener(this);
        findViewById(R.id.fb2).setOnClickListener(this);
        findViewById(R.id.tw2).setOnClickListener(this);

        TextView version = findViewById(R.id.app_version);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version.setText(getResources().getString(R.string.version, pInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        String url = null;

        switch (v.getId()) {
            case R.id.licenses:
                new LicensesDialog.Builder(this)
                        .setNotices(R.raw.notices)
                        .build()
                        .show();
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
