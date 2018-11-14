package co.edu.udistrital.brainreactor;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Colors {
    public List<Colors> COLORS;
    private String name, color;
    private Context context;

    public Colors(Context context) {
        this.context = context;
        this.COLORS = new ArrayList<>();
    }

    private Colors(String name, String color) {
        this.name = name;
        this.color = color;
        this.COLORS = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void listColors() {
        int length = context.getResources().getStringArray(R.array.colors).length;

        for (int i = 0; i < length; i++) {
            COLORS.add(new Colors(context.getResources().getStringArray(R.array.colors_name)[i], context.getResources().getStringArray(R.array.colors)[i]));
        }
    }
}
