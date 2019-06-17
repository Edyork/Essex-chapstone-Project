package uk.co.edministrator.spaceshipgame.Ships.parts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class DamageDisplay {
    private String string = "";
    private int x, y, lifetime;
    private int type;
    private static transient Paint p  = new Paint();

    public DamageDisplay(String dmg, int type, int x, int y){
        string = dmg + "";
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void Draw(Canvas canvas){
        if (lifetime < 30) {
            p.setColor(Color.RED);
            if (type == 1){
                p.setTextSize(50);
                p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
                canvas.drawText("Crit", x-25, y+50, p);
            }
            else if (type == 2 || type == 3 || type == 4){
                p.setTextSize(50);
                p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
                canvas.drawText("Strong", x-25, y+50, p);
            }
            else if (type == 5){
                p.setTextSize(50);
                p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
                canvas.drawText("Weak", x-25, y+50, p);
            }
            p.setTextSize(100);
            p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
            canvas.drawText(string, x, y, p);
            this.y -= 2;
            lifetime++;
        }
    }
}
