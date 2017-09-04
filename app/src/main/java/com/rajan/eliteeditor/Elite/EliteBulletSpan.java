package com.rajan.eliteeditor.Elite;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.BulletSpan;

/**
 * Created by rajan.kali on 9/1/2017.
 * Custom Bullet Span
 */


public class EliteBulletSpan extends BulletSpan {
    private static Path bulletPath = null;

    private int bulletRadius = 8;

    @Override
    public int getLeadingMargin(boolean first) {
        return 2 * bulletRadius + 20;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  CharSequence text, int start, int end,
                                  boolean first, Layout l) {
        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = p.getStyle();

            int oldColor = p.getColor();
            p.setColor(Color.parseColor("#444444"));
            p.setStyle(Paint.Style.FILL);

            if (c.isHardwareAccelerated()) {
                if (bulletPath == null) {
                    bulletPath = new Path();
                    // Bullet is slightly better to avoid aliasing artifacts on mdpi devices.
                    bulletPath.addCircle(0.0f, 0.0f, bulletRadius, Path.Direction.CW);
                }

                c.save();
                c.translate(x + dir * bulletRadius, (top + bottom) / 2.0f);
                c.drawPath(bulletPath, p);
                c.restore();
            } else {
                c.drawCircle(x + dir * bulletRadius, (top + bottom) / 2.0f, bulletRadius, p);
            }

            p.setColor(oldColor);
            p.setStyle(style);
        }
    }
}
